package suszombification.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import suszombification.block.TrophyBlock.TrophyType;
import suszombification.registration.SZBlockEntityTypes;

public class TrophyBlockEntity extends BlockEntity {
	private TrophyType trophyType;
	private boolean curseGiven;

	public TrophyBlockEntity(BlockPos pos, BlockState state) {
		super(SZBlockEntityTypes.TROPHY.get(), pos, state);
	}

	public TrophyBlockEntity(BlockEntityType<? extends TrophyBlockEntity> blockEntityType, BlockPos pos, BlockState state, TrophyType trophyType) {
		super(blockEntityType, pos, state);

		this.trophyType = trophyType;
	}

	public void setCurseGiven(boolean curseGiven) {
		this.curseGiven = curseGiven;
	}

	public boolean isCurseGiven() {
		return curseGiven;
	}

	public TrophyType getTrophyType() {
		return trophyType;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.putInt("TrophyType", trophyType.ordinal());
		tag.putBoolean("CurseGiven", curseGiven);
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

		curseGiven = tag.getBoolean("CurseGiven");
	}

	@Override
	public CompoundTag getUpdateTag() {
		return save(new CompoundTag());
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		load(packet.getTag());
	}
}
