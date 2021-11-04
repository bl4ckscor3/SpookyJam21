package suszombification.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import suszombification.entity.ThrownRottenEgg;

public class RottenEggItem extends Item {
	public RottenEggItem(Properties properties) {
		super(properties);

		DispenserBlock.registerBehavior(this, new ProjectileDispenseBehavior() {
			@Override
			protected ProjectileEntity getProjectile(World level, IPosition pos, ItemStack stack) {
				return Util.make(new ThrownRottenEgg(level, pos.x(), pos.y(), pos.z()), egg -> egg.setItem(stack));
			}
		});
	}

	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);

		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

		if(!level.isClientSide) {
			ThrownRottenEgg egg = new ThrownRottenEgg(level, player);

			egg.setItem(stack);
			egg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
			level.addFreshEntity(egg);
		}

		player.awardStat(Stats.ITEM_USED.get(this));

		if(!player.getAbilities().instabuild)
			stack.shrink(1);

		return ActionResult.sidedSuccess(stack, level.isClientSide());
	}
}
