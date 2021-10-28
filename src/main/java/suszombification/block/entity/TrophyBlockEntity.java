package suszombification.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import suszombification.SZBlockEntityTypes;
import suszombification.block.TrophyBlock.TrophyType;

public class TrophyBlockEntity extends BlockEntity {
	private TrophyType trophyType;

	public TrophyBlockEntity(BlockPos pos, BlockState state) {
		super(SZBlockEntityTypes.TROPHY.get(), pos, state);
	}

	public TrophyBlockEntity(BlockEntityType<? extends TrophyBlockEntity> blockEntityType, BlockPos pos, BlockState state, TrophyType trophyType) {
		super(blockEntityType, pos, state);

		this.trophyType = trophyType;
	}

	public TrophyType getTrophyType() {
		return trophyType;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.putInt("TrophyType", trophyType.ordinal());
		return super.save(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);

		int savedOrdinal = tag.getInt("TrophyType");

		if(savedOrdinal < 0 || savedOrdinal >= TrophyType.values().length)
			trophyType = TrophyType.CARROT;
		else
			trophyType = TrophyType.values()[savedOrdinal];
	}
}
