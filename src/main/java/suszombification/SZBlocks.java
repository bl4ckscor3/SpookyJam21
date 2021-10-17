package suszombification;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.block.RottenWoolBlock;

public class SZBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SuspiciousZombification.MODID);

	public static final RegistryObject<Block> WHITE_ROTTEN_WOOl = BLOCKS.register("white_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.SNOW).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ORANGE_ROTTEN_WOOL = BLOCKS.register("orange_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_ORANGE).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> MAGENTA_ROTTEN_WOOL = BLOCKS.register("magenta_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_MAGENTA).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> LIGHT_BLUE_ROTTEN_WOOL = BLOCKS.register("light_blue_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_LIGHT_BLUE).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> YELLOW_ROTTEN_WOOL = BLOCKS.register("yellow_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_YELLOW).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> LIME_ROTTEN_WOOL = BLOCKS.register("lime_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_LIGHT_GREEN).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> PINK_ROTTEN_WOOL = BLOCKS.register("pink_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_PINK).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> GRAY_ROTTEN_WOOL = BLOCKS.register("gray_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_GRAY).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> LIGHT_GRAY_ROTTEN_WOOL = BLOCKS.register("light_gray_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_LIGHT_GRAY).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> CYAN_ROTTEN_WOOL = BLOCKS.register("cyan_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_CYAN).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> PURPLE_ROTTEN_WOOL = BLOCKS.register("purple_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_PURPLE).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> BLUE_ROTTEN_WOOL = BLOCKS.register("blue_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_BLUE).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> BROWN_ROTTEN_WOOL = BLOCKS.register("brown_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_BROWN).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> GREEN_ROTTEN_WOOL = BLOCKS.register("green_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_GREEN).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> RED_ROTTEN_WOOL = BLOCKS.register("red_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_RED).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> BLACK_ROTTEN_WOOL = BLOCKS.register("black_rotten_wool", () -> new RottenWoolBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_BLACK).strength(0.8F).sound(SoundType.WOOL)));
}
