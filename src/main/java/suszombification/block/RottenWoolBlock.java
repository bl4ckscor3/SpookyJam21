package suszombification.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RottenWoolBlock extends Block {
	public RottenWoolBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void fallOn(World level, BlockPos pos, Entity entity, float fallDistance) {
		entity.causeFallDamage(fallDistance, 0.5F);
		entity.playSound(SoundEvents.SLIME_SQUISH, 0.3F, 1.5F);
	}
}
