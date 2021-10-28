package suszombification;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.structure.ZombieCoveFeature;

public class SZStructures {
	public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, SuspiciousZombification.MODID);

	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> ZOMBIE_COVE = STRUCTURES.register("zombie_cove", () -> new ZombieCoveFeature(EntityType.ZOMBIE, "start", NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> DESERT_ZOMBIE_COVE = STRUCTURES.register("desert_zombie_cove", () -> new ZombieCoveFeature(EntityType.HUSK, "desert_start", NoneFeatureConfiguration.CODEC));

	public static void setup() {
		registerStructure(ZOMBIE_COVE.get(), new StructureFeatureConfiguration(32, 8, 46821176), true);
		registerStructure(DESERT_ZOMBIE_COVE.get(), new StructureFeatureConfiguration(32, 8, 46821176), true);
	}

	private static <F extends StructureFeature<?>> void registerStructure(F structure, StructureFeatureConfiguration config, boolean transformSurroundingLand) {
		StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
		StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>,StructureFeatureConfiguration>builder().putAll(StructureSettings.DEFAULTS).put(structure, config).build();
		BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
			Map<StructureFeature<?>,StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();

			if(structureMap instanceof ImmutableMap) {
				Map<StructureFeature<?>,StructureFeatureConfiguration> map = new HashMap<>(structureMap);

				map.put(structure, config);
				settings.getValue().structureSettings().structureConfig = map;
			}
			else
				structureMap.put(structure, config);
		});

		if(transformSurroundingLand)
			StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder().addAll(StructureFeature.NOISE_AFFECTING_FEATURES).add(structure).build();
	}
}
