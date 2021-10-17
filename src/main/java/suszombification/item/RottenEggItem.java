package suszombification.item;

import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import suszombification.entity.ThrownRottenEgg;

public class RottenEggItem extends Item {
	public RottenEggItem(Properties properties) {
		super(properties);

		DispenserBlock.registerBehavior(this, new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level level, Position pos, ItemStack stack) {
				return Util.make(new ThrownRottenEgg(level, pos.x(), pos.y(), pos.z()), egg -> egg.setItem(stack));
			}
		});
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

		if(!level.isClientSide) {
			ThrownRottenEgg egg = new ThrownRottenEgg(level, player);

			egg.setItem(stack);
			egg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
			level.addFreshEntity(egg);
		}

		player.awardStat(Stats.ITEM_USED.get(this));

		if(!player.getAbilities().instabuild)
			stack.shrink(1);

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
}
