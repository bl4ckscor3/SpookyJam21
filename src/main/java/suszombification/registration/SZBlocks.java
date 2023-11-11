package suszombification.registration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import suszombification.SuspiciousZombification;
import suszombification.block.RottenWoolBlock;
import suszombification.block.TrophyBlock;
import suszombification.block.TrophyBlock.TrophyType;

public class SZBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SuspiciousZombification.MODID);
	public static final RegistryObject<Block> WHITE_ROTTEN_WOOl = BLOCKS.register("white_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.SNOW)));
	public static final RegistryObject<Block> ORANGE_ROTTEN_WOOL = BLOCKS.register("orange_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_ORANGE)));
	public static final RegistryObject<Block> MAGENTA_ROTTEN_WOOL = BLOCKS.register("magenta_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_MAGENTA)));
	public static final RegistryObject<Block> LIGHT_BLUE_ROTTEN_WOOL = BLOCKS.register("light_blue_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_LIGHT_BLUE)));
	public static final RegistryObject<Block> YELLOW_ROTTEN_WOOL = BLOCKS.register("yellow_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_YELLOW)));
	public static final RegistryObject<Block> LIME_ROTTEN_WOOL = BLOCKS.register("lime_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_LIGHT_GREEN)));
	public static final RegistryObject<Block> PINK_ROTTEN_WOOL = BLOCKS.register("pink_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_PINK)));
	public static final RegistryObject<Block> GRAY_ROTTEN_WOOL = BLOCKS.register("gray_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_GRAY)));
	public static final RegistryObject<Block> LIGHT_GRAY_ROTTEN_WOOL = BLOCKS.register("light_gray_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_LIGHT_GRAY)));
	public static final RegistryObject<Block> CYAN_ROTTEN_WOOL = BLOCKS.register("cyan_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_CYAN)));
	public static final RegistryObject<Block> PURPLE_ROTTEN_WOOL = BLOCKS.register("purple_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_PURPLE)));
	public static final RegistryObject<Block> BLUE_ROTTEN_WOOL = BLOCKS.register("blue_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_BLUE)));
	public static final RegistryObject<Block> BROWN_ROTTEN_WOOL = BLOCKS.register("brown_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_BROWN)));
	public static final RegistryObject<Block> GREEN_ROTTEN_WOOL = BLOCKS.register("green_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_GREEN)));
	public static final RegistryObject<Block> RED_ROTTEN_WOOL = BLOCKS.register("red_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_RED)));
	public static final RegistryObject<Block> BLACK_ROTTEN_WOOL = BLOCKS.register("black_rotten_wool", () -> new RottenWoolBlock(rottenWool(MapColor.COLOR_BLACK)));
	public static final RegistryObject<Block> CARROT_TROPHY = BLOCKS.register("carrot_trophy", () -> new TrophyBlock(TrophyType.CARROT, trophies()));
	public static final RegistryObject<Block> POTATO_TROPHY = BLOCKS.register("potato_trophy", () -> new TrophyBlock(TrophyType.POTATO, trophies()));
	public static final RegistryObject<Block> IRON_INGOT_TROPHY = BLOCKS.register("iron_ingot_trophy", () -> new TrophyBlock(TrophyType.IRON_INGOT, trophies()));

	private static BlockBehaviour.Properties rottenWool(MapColor mapColor) {
		return BlockBehaviour.Properties.of().mapColor(mapColor).strength(0.8F).sound(SoundType.WOOL);
	}

	private static BlockBehaviour.Properties trophies() {
		return BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL);
	}
}
