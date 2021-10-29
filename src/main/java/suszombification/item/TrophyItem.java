package suszombification.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import suszombification.SZEffects;
import suszombification.block.entity.TrophyBlockEntity;

public class TrophyItem extends BlockItem {
	public TrophyItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if(level.getDifficulty() != Difficulty.PEACEFUL) {
			Player player = (Player)entity;

			if(!player.getAbilities().instabuild && !player.hasEffect(SZEffects.ZOMBIES_CURSE.get()) && !stack.getOrCreateTag().getBoolean("CurseGiven")) {
				stack.getTag().putBoolean("CurseGiven", true);
				player.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 0.9F);
				player.playSound(SoundEvents.ZOMBIE_AMBIENT, 0.5F, 0.8F);
				player.addEffect(new MobEffectInstance(SZEffects.ZOMBIES_CURSE.get(), Integer.MAX_VALUE));
			}
		}
	}

	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack stack, BlockState state) {
		if(level.getBlockEntity(pos) instanceof TrophyBlockEntity trophy)
			trophy.setCurseGiven(stack.getOrCreateTag().getBoolean("CurseGiven"));

		return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
	}
}
