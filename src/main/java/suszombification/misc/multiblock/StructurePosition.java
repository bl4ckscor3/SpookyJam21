package suszombification.misc.multiblock;

import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructurePosition implements StructurePart {
	private final int x;
	private final int y;
	private final int z;
	private Predicate<BlockState> check;

	public StructurePosition(int x, int y, int z, Predicate<BlockState> check) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.check = check;
	}

	@Override
	public boolean checkPart(World level, BlockPos structureOrigin) {
		return check.test(level.getBlockState(structureOrigin.offset(x, y, z)));
	}
}
