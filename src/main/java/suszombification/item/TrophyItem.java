package suszombification.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import suszombification.SZEffects;
import suszombification.block.entity.TrophyBlockEntity;

public class TrophyItem extends BlockItem {
	public TrophyItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, World level, Entity entity, int slotId, boolean isSelected) {
		if(level.getDifficulty() != Difficulty.PEACEFUL) {
			PlayerEntity player = (PlayerEntity)entity;

			if(!player.abilities.instabuild && !player.isSpectator() && !player.hasEffect(SZEffects.ZOMBIES_CURSE.get()) && !stack.getOrCreateTag().getBoolean("CurseGiven")) {
				stack.getTag().putBoolean("CurseGiven", true);
				player.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 0.9F);
				player.playSound(SoundEvents.ZOMBIE_AMBIENT, 0.5F, 0.8F);
				player.addEffect(new EffectInstance(SZEffects.ZOMBIES_CURSE.get(), Integer.MAX_VALUE));

				if(!level.isClientSide)
					player.sendMessage(new TranslationTextComponent("message.suszombification.curse.warning").withStyle(TextFormatting.RED), Util.NIL_UUID);
			}
		}
	}

	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, World level, PlayerEntity player, ItemStack stack, BlockState state) {
		TileEntity be = level.getBlockEntity(pos);

		if(be instanceof TrophyBlockEntity)
			((TrophyBlockEntity)be).setCurseGiven(stack.getOrCreateTag().getBoolean("CurseGiven"));

		return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
	}
}
