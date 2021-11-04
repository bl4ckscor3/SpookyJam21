package suszombification;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class SZConfiguredStructures {
	public static final StructureFeature<?,?> ZOMBIE_COVE = SZStructures.ZOMBIE_COVE.get().configured(NoFeatureConfig.INSTANCE);
	public static final StructureFeature<?,?> DESERT_ZOMBIE_COVE = SZStructures.DESERT_ZOMBIE_COVE.get().configured(NoFeatureConfig.INSTANCE);

	public static void setup() {
		Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(SuspiciousZombification.MODID, "zombie_cove"), ZOMBIE_COVE);
		Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(SuspiciousZombification.MODID, "desert_zombie_cove"), DESERT_ZOMBIE_COVE);
	}
}
