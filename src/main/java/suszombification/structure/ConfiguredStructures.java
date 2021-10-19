package suszombification.structure;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import suszombification.SZStructures;
import suszombification.SuspiciousZombification;

public class ConfiguredStructures {
	public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> CONFIGURED_ZOMBIE_OUTPOST = SZStructures.ZOMBIE_OUTPOST.get().configured(NoneFeatureConfiguration.INSTANCE);
	public static final StructurePieceType ZOMBIE_OUTPOST_MAIN = ZombieOutpostPieces.MainPiece::new;

	public static void register() {
		Registry<ConfiguredStructureFeature<?, ?>> structureRegistry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
		Registry<StructurePieceType> pieceRegistry = Registry.STRUCTURE_PIECE;

		StructureFeature.STRUCTURES_REGISTRY.put("zombie_outpost", SZStructures.ZOMBIE_OUTPOST.get());
		Registry.register(structureRegistry, new ResourceLocation(SuspiciousZombification.MODID, "configured_zombie_outpost"), CONFIGURED_ZOMBIE_OUTPOST);
		Registry.register(pieceRegistry, new ResourceLocation(SuspiciousZombification.MODID, "zomm"), ZOMBIE_OUTPOST_MAIN);

		FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(SZStructures.ZOMBIE_OUTPOST.get(), CONFIGURED_ZOMBIE_OUTPOST);
	}
}
