package suszombification.structure;

import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import suszombification.SZStructures;
import suszombification.SuspiciousZombification;

public class ConfiguredStructures {
	public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> CONFIGURED_ZOMBIE_OUTPOST = SZStructures.ZOMBIE_OUTPOST.get().configured(new JigsawConfiguration(() -> ZombieOutpostPools.START, 7));

	public static final StructureProcessorList ZOMBIE_OUTPOST = new StructureProcessorList(List.of(new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.3F), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.defaultBlockState()),
			new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.1F), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICKS.defaultBlockState()),
			new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.4F), AlwaysTrueTest.INSTANCE, Blocks.CRACKED_STONE_BRICKS.defaultBlockState())))));

	public static void register() {
		Registry<ConfiguredStructureFeature<?, ?>> structureRegistry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
		Registry<StructureProcessorList> processorRegistry = BuiltinRegistries.PROCESSOR_LIST;

		StructureFeature.STRUCTURES_REGISTRY.put("zombie_outpost", SZStructures.ZOMBIE_OUTPOST.get());
		Registry.register(structureRegistry, new ResourceLocation(SuspiciousZombification.MODID, "configured_zombie_outpost"), CONFIGURED_ZOMBIE_OUTPOST);
		Registry.register(processorRegistry, new ResourceLocation(SuspiciousZombification.MODID, "zombie_outpost"), ZOMBIE_OUTPOST);

		FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(SZStructures.ZOMBIE_OUTPOST.get(), CONFIGURED_ZOMBIE_OUTPOST);
	}
}
