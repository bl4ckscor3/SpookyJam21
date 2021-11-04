package suszombification.misc.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface StructurePart {
	public boolean checkPart(World level, BlockPos structureOrigin);
}
