package suszombification.registration;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import suszombification.SuspiciousZombification;

public class SZConfiguredStructures {
	public static final ConfiguredStructureFeature<?,?> ZOMBIE_COVE = SZStructures.ZOMBIE_COVE.get().configured(NoneFeatureConfiguration.INSTANCE);
	public static final ConfiguredStructureFeature<?,?> DESERT_ZOMBIE_COVE = SZStructures.DESERT_ZOMBIE_COVE.get().configured(NoneFeatureConfiguration.INSTANCE);

	public static void setup() {
		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(SuspiciousZombification.MODID, "zombie_cove"), ZOMBIE_COVE);
		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(SuspiciousZombification.MODID, "desert_zombie_cove"), DESERT_ZOMBIE_COVE);
	}
}
