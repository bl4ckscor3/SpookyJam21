package suszombification.misc;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class SuspiciousRitual {
	private static final Predicate<BlockState> WOODEN_FENCE = state -> state.is(BlockTags.WOODEN_FENCES);
	private static final Predicate<BlockState> CHISELED_STONE_BRICKS = state -> state.is(Blocks.CHISELED_STONE_BRICKS);
	private static final Predicate<BlockState> ANY_OTHER_STONE_BRICKS = state -> state.is(BlockTags.STONE_BRICKS) && !CHISELED_STONE_BRICKS.test(state);
	private static final Predicate<BlockState> PUMPKIN = state -> state.is(Blocks.PUMPKIN);
	private static final Predicate<BlockState> REDSTONE_TORCH = state -> state.is(Blocks.REDSTONE_TORCH);
	private static final BiPredicate<BlockState, Direction> CARVED_PUMPKIN = (state, dir) -> state.is(Blocks.CARVED_PUMPKIN) && state.getValue(BlockStateProperties.HORIZONTAL_FACING) == dir;
	private static final List<StructurePart> STRUCTURE_PARTS = List.of(
			new StructurePosition(0, 0, 0, WOODEN_FENCE),
			new StructurePosition(0, -1, 0, CHISELED_STONE_BRICKS),
			new StructureArea(-2, -1, -2, 2, -1, -1, ANY_OTHER_STONE_BRICKS),
			new StructureArea(-2, -1, 0, -1, -1, 0, ANY_OTHER_STONE_BRICKS),
			new StructureArea(1, -1, 0, 2, -1, 0, ANY_OTHER_STONE_BRICKS),
			new StructureArea(-2, -1, 1, 2, -1, 2, ANY_OTHER_STONE_BRICKS),
			new StructurePosition(-2, 0, -2, PUMPKIN),
			new StructurePosition(-2, 0, 2, PUMPKIN),
			new StructurePosition(2, 0, -2, PUMPKIN),
			new StructurePosition(2, 0, 2, PUMPKIN),
			new StructurePosition(-2, 1, -2, REDSTONE_TORCH),
			new StructurePosition(-2, 1, 2, REDSTONE_TORCH),
			new StructurePosition(2, 1, -2, REDSTONE_TORCH),
			new StructurePosition(2, 1, 2, REDSTONE_TORCH),
			new StructurePosition(-3, 0, 0, CHISELED_STONE_BRICKS),
			new StructurePosition(3, 0, 0, CHISELED_STONE_BRICKS),
			new StructurePosition(0, 0, -3, CHISELED_STONE_BRICKS),
			new StructurePosition(0, 0, 3, CHISELED_STONE_BRICKS),
			new StructurePosition(-3, 1, 0, state -> CARVED_PUMPKIN.test(state, Direction.EAST)),
			new StructurePosition(3, 1, 0, state -> CARVED_PUMPKIN.test(state, Direction.WEST)),
			new StructurePosition(0, 1, -3, state -> CARVED_PUMPKIN.test(state, Direction.SOUTH)),
			new StructurePosition(0, 1, 3, state -> CARVED_PUMPKIN.test(state, Direction.NORTH))
			);

	/**
	 * Checks if the block structure of the suspicious ritual is built correctly at the given position.
	 *
	 * @param level The level the structure is built in
	 * @param structureOrigin The origin block position of the structure, in this case the position of the fence block in the middle
	 * @return true if the structure has been built correctly, false otherwise
	 */
	public static final boolean isStructurePresent(Level level, BlockPos structureOrigin) {
		return STRUCTURE_PARTS.stream().allMatch(part -> part.checkPart(level, structureOrigin));
	}

	private static interface StructurePart {
		public boolean checkPart(Level level, BlockPos structureOrigin);
	}

	private static record StructurePosition(int x, int y, int z, Predicate<BlockState> check) implements StructurePart {
		@Override
		public boolean checkPart(Level level, BlockPos structureOrigin) {
			return check.test(level.getBlockState(structureOrigin.offset(x, y, z)));
		}
	}

	private static record StructureArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Predicate<BlockState> check) implements StructurePart {
		@Override
		public boolean checkPart(Level level, BlockPos structureOrigin) {
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
}
