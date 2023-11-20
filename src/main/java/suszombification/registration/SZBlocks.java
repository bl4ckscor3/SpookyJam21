package suszombification.registration;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import suszombification.SuspiciousZombification;
import suszombification.block.RottenWoolBlock;
import suszombification.block.TrophyBlock;
import suszombification.block.TrophyBlock.TrophyType;

public class SZBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SuspiciousZombification.MODID);
	public static final DeferredBlock<RottenWoolBlock> WHITE_ROTTEN_WOOl = BLOCKS.register("white_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.SNOW)));
	public static final DeferredBlock<RottenWoolBlock> ORANGE_ROTTEN_WOOL = BLOCKS.register("orange_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_ORANGE)));
	public static final DeferredBlock<RottenWoolBlock> MAGENTA_ROTTEN_WOOL = BLOCKS.register("magenta_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_MAGENTA)));
	public static final DeferredBlock<RottenWoolBlock> LIGHT_BLUE_ROTTEN_WOOL = BLOCKS.register("light_blue_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_LIGHT_BLUE)));
	public static final DeferredBlock<RottenWoolBlock> YELLOW_ROTTEN_WOOL = BLOCKS.register("yellow_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_YELLOW)));
	public static final DeferredBlock<RottenWoolBlock> LIME_ROTTEN_WOOL = BLOCKS.register("lime_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_LIGHT_GREEN)));
	public static final DeferredBlock<RottenWoolBlock> PINK_ROTTEN_WOOL = BLOCKS.register("pink_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_PINK)));
	public static final DeferredBlock<RottenWoolBlock> GRAY_ROTTEN_WOOL = BLOCKS.register("gray_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_GRAY)));
	public static final DeferredBlock<RottenWoolBlock> LIGHT_GRAY_ROTTEN_WOOL = BLOCKS.register("light_gray_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_LIGHT_GRAY)));
	public static final DeferredBlock<RottenWoolBlock> CYAN_ROTTEN_WOOL = BLOCKS.register("cyan_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_CYAN)));
	public static final DeferredBlock<RottenWoolBlock> PURPLE_ROTTEN_WOOL = BLOCKS.register("purple_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_PURPLE)));
	public static final DeferredBlock<RottenWoolBlock> BLUE_ROTTEN_WOOL = BLOCKS.register("blue_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_BLUE)));
	public static final DeferredBlock<RottenWoolBlock> BROWN_ROTTEN_WOOL = BLOCKS.register("brown_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_BROWN)));
	public static final DeferredBlock<RottenWoolBlock> GREEN_ROTTEN_WOOL = BLOCKS.register("green_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_GREEN)));
	public static final DeferredBlock<RottenWoolBlock> RED_ROTTEN_WOOL = BLOCKS.register("red_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_RED)));
	public static final DeferredBlock<RottenWoolBlock> BLACK_ROTTEN_WOOL = BLOCKS.register("black_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_BLACK)));
	public static final DeferredBlock<TrophyBlock> CARROT_TROPHY = BLOCKS.register("carrot_trophy", () -> new TrophyBlock(TrophyType.CARROT, trophies()));
	public static final DeferredBlock<TrophyBlock> POTATO_TROPHY = BLOCKS.register("potato_trophy", () -> new TrophyBlock(TrophyType.POTATO, trophies()));
	public static final DeferredBlock<TrophyBlock> IRON_INGOT_TROPHY = BLOCKS.register("iron_ingot_trophy", () -> new TrophyBlock(TrophyType.IRON_INGOT, trophies()));

	private static BlockBehaviour.Properties rottenWool(MapColor mapColor) {
		return BlockBehaviour.Properties.of().mapColor(mapColor).strength(0.8F).sound(SoundType.WOOL);
	}

	private static BlockBehaviour.Properties trophies() {
		return BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL);
	}
}
