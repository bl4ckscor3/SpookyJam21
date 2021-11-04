package suszombification;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemTier;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.block.RottenWoolBlock;
import suszombification.block.TrophyBlock;
import suszombification.block.TrophyBlock.TrophyType;

public class SZBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SuspiciousZombification.MODID);

	public static final RegistryObject<Block> WHITE_ROTTEN_WOOl = BLOCKS.register("white_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.SNOW)));
	public static final RegistryObject<Block> ORANGE_ROTTEN_WOOL = BLOCKS.register("orange_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_ORANGE)));
	public static final RegistryObject<Block> MAGENTA_ROTTEN_WOOL = BLOCKS.register("magenta_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_MAGENTA)));
	public static final RegistryObject<Block> LIGHT_BLUE_ROTTEN_WOOL = BLOCKS.register("light_blue_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_LIGHT_BLUE)));
	public static final RegistryObject<Block> YELLOW_ROTTEN_WOOL = BLOCKS.register("yellow_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_YELLOW)));
	public static final RegistryObject<Block> LIME_ROTTEN_WOOL = BLOCKS.register("lime_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_LIGHT_GREEN)));
	public static final RegistryObject<Block> PINK_ROTTEN_WOOL = BLOCKS.register("pink_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_PINK)));
	public static final RegistryObject<Block> GRAY_ROTTEN_WOOL = BLOCKS.register("gray_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_GRAY)));
	public static final RegistryObject<Block> LIGHT_GRAY_ROTTEN_WOOL = BLOCKS.register("light_gray_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_LIGHT_GRAY)));
	public static final RegistryObject<Block> CYAN_ROTTEN_WOOL = BLOCKS.register("cyan_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_CYAN)));
	public static final RegistryObject<Block> PURPLE_ROTTEN_WOOL = BLOCKS.register("purple_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_PURPLE)));
	public static final RegistryObject<Block> BLUE_ROTTEN_WOOL = BLOCKS.register("blue_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_BLUE)));
	public static final RegistryObject<Block> BROWN_ROTTEN_WOOL = BLOCKS.register("brown_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_BROWN)));
	public static final RegistryObject<Block> GREEN_ROTTEN_WOOL = BLOCKS.register("green_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_GREEN)));
	public static final RegistryObject<Block> RED_ROTTEN_WOOL = BLOCKS.register("red_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_RED)));
	public static final RegistryObject<Block> BLACK_ROTTEN_WOOL = BLOCKS.register("black_rotten_wool", () -> new RottenWoolBlock(rottenWool(MaterialColor.COLOR_BLACK)));
	public static final RegistryObject<Block> CARROT_TROPHY = BLOCKS.register("carrot_trophy", () -> new TrophyBlock(TrophyType.CARROT, trophies()));
	public static final RegistryObject<Block> POTATO_TROPHY = BLOCKS.register("potato_trophy", () -> new TrophyBlock(TrophyType.POTATO, trophies()));
	public static final RegistryObject<Block> IRON_INGOT_TROPHY = BLOCKS.register("iron_ingot_trophy", () -> new TrophyBlock(TrophyType.IRON_INGOT, trophies()));

	private static AbstractBlock.Properties rottenWool(MaterialColor materialColor) {
		return AbstractBlock.Properties.of(Material.WOOL, materialColor).strength(0.8F).sound(SoundType.WOOL);
	}

	private static AbstractBlock.Properties trophies() {
		return AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL).harvestLevel(ItemTier.IRON.getLevel()).harvestTool(ToolType.PICKAXE);
	}
}
