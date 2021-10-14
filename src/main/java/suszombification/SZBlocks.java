package suszombification;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SZBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SuspiciousZombification.MODID);

	public static final RegistryObject<Block> ROTTEN_WHITE_WOOL = BLOCKS.register("rotten_white_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.SNOW).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_ORANGE_WOOL = BLOCKS.register("rotten_orange_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_ORANGE).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_MAGENTA_WOOL = BLOCKS.register("rotten_magenta_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_MAGENTA).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_LIGHT_BLUE_WOOL = BLOCKS.register("rotten_light_blue_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_LIGHT_BLUE).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_YELLOW_WOOL = BLOCKS.register("rotten_yellow_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_YELLOW).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_LIME_WOOL = BLOCKS.register("rotten_lime_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_LIGHT_GREEN).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_PINK_WOOL = BLOCKS.register("rotten_pink_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_PINK).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_GRAY_WOOL = BLOCKS.register("rotten_gray_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_GRAY).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_LIGHT_GRAY_WOOL = BLOCKS.register("rotten_light_gray_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_LIGHT_GRAY).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_CYAN_WOOL = BLOCKS.register("rotten_cyan_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_CYAN).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_PURPLE_WOOL = BLOCKS.register("rotten_purple_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_PURPLE).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_BLUE_WOOL = BLOCKS.register("rotten_blue_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_BLUE).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_BROWN_WOOL = BLOCKS.register("rotten_brown_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_BROWN).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_GREEN_WOOL = BLOCKS.register("rotten_green_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_GREEN).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_RED_WOOL = BLOCKS.register("rotten_red_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_RED).strength(0.8F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> ROTTEN_BLACK_WOOL = BLOCKS.register("rotten_black_wool", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_BLACK).strength(0.8F).sound(SoundType.WOOL)));
}
