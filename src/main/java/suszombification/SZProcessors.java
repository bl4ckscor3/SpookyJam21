package suszombification;

import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class SZProcessors {
	public static final StructureProcessorList ZOMBIE_COVE = new StructureProcessorList(List.of(new RuleProcessor(List.of(
			new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.3F), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.defaultBlockState()),
			new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.1F), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICKS.defaultBlockState()),
			new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.4F), AlwaysTrueTest.INSTANCE, Blocks.CRACKED_STONE_BRICKS.defaultBlockState())))));

	public static void setup() {
		Registry.register(BuiltinRegistries.PROCESSOR_LIST, new ResourceLocation(SuspiciousZombification.MODID, "zombie_cove"), ZOMBIE_COVE);
	}
}
