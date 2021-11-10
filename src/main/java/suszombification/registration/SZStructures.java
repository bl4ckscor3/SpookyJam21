package suszombification.registration;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.SuspiciousZombification;
import suszombification.structure.ZombieCoveFeature;

public class SZStructures {
	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, SuspiciousZombification.MODID);

	public static final RegistryObject<Structure<NoFeatureConfig>> ZOMBIE_COVE = STRUCTURES.register("zombie_cove", () -> new ZombieCoveFeature(EntityType.ZOMBIE, "start", NoFeatureConfig.CODEC));
	public static final RegistryObject<Structure<NoFeatureConfig>> DESERT_ZOMBIE_COVE = STRUCTURES.register("desert_zombie_cove", () -> new ZombieCoveFeature(EntityType.HUSK, "desert_start", NoFeatureConfig.CODEC));

	public static void setup() {
		registerStructure(ZOMBIE_COVE.get(), new StructureSeparationSettings(32, 8, 46821176), true);
		registerStructure(DESERT_ZOMBIE_COVE.get(), new StructureSeparationSettings(32, 8, 46821176), true);
	}

	private static <F extends Structure<?>> void registerStructure(F structure, StructureSeparationSettings config, boolean transformSurroundingLand) {
		Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
		DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>,StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.DEFAULTS).put(structure, config).build();
		WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
			Map<Structure<?>,StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

			if(structureMap instanceof ImmutableMap) {
				Map<Structure<?>,StructureSeparationSettings> map = new HashMap<>(structureMap);

				map.put(structure, config);
				settings.getValue().structureSettings().structureConfig = map;
			}
			else
				structureMap.put(structure, config);
		});

		if(transformSurroundingLand)
			Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure).build();
	}
}
