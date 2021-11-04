package suszombification.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import suszombification.SZBlockEntityTypes;
import suszombification.block.entity.TrophyBlockEntity;

public class TrophyBlock extends HorizontalBlock implements ITileEntityProvider {
	private static final VoxelShape SHAPE = VoxelShapes.or(Block.box(6.0D, 7.0D, 6.0D, 10.0D, 8.0D, 10.0D), Block.box(4.0D, 0.0D, 4.0D, 12.0D, 1.0D, 12.0D), Block.box(5.0D, 1.0D, 5.0D, 11.0D, 2.0D, 11.0D), Block.box(7.0D, 2.0D, 7.0D, 9.0D, 7.0D, 9.0D));
	private final TrophyType trophyType;

	public TrophyBlock(TrophyType trophyType, Properties properties) {
		super(properties);

		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
		this.trophyType = trophyType;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader level) {
		return new TrophyBlockEntity(SZBlockEntityTypes.TROPHY.get(), trophyType);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public static enum TrophyType {
		CARROT(new ItemStack(Items.CARROT)), POTATO(new ItemStack(Items.POTATO)), IRON_INGOT(new ItemStack(Items.IRON_INGOT));

		public final ItemStack displayItem;

		TrophyType(ItemStack displayItem) {
			this.displayItem = displayItem;
		}
	}
}
