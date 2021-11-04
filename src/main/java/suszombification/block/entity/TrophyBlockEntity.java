package suszombification.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import suszombification.SZBlockEntityTypes;
import suszombification.block.TrophyBlock.TrophyType;

public class TrophyBlockEntity extends TileEntity {
	private TrophyType trophyType;
	private boolean curseGiven;

	public TrophyBlockEntity() {
		super(SZBlockEntityTypes.TROPHY.get());
	}

	public TrophyBlockEntity(TileEntityType<? extends TrophyBlockEntity> blockEntityType, TrophyType trophyType) {
		super(blockEntityType);

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
	public CompoundNBT save(CompoundNBT tag) {
		tag.putInt("TrophyType", trophyType.ordinal());
		tag.putBoolean("CurseGiven", curseGiven);
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag) {
		super.load(state, tag);

		int savedOrdinal = tag.getInt("TrophyType");

		if(savedOrdinal < 0 || savedOrdinal >= TrophyType.values().length)
			trophyType = TrophyType.CARROT;
		else
			trophyType = TrophyType.values()[savedOrdinal];

		curseGiven = tag.getBoolean("CurseGiven");
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		load(getBlockState(), packet.getTag());
	}
}
