package suszombification.misc.multiblock;

import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureArea implements StructurePart {
	private final int minX;
	private final int minY;
	private final int minZ;
	private final int maxX;
	private final int maxY;
	private final int maxZ;
	private Predicate<BlockState> check;

	public StructureArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Predicate<BlockState> check) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.check = check;
	}

	@Override
	public boolean checkPart(World level, BlockPos structureOrigin) {
		for(int x = minX; x <= maxX; x++) {
			for(int y = minY; y <= maxY; y++) {
				for(int z = minZ; z <= maxZ; z++) {
					if(!check.test(level.getBlockState(structureOrigin.offset(x, y, z))))
						return false;
				}
			}
		}

		return true;
	}
}
