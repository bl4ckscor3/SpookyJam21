package suszombification.registration;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import suszombification.SuspiciousZombification;
import suszombification.structure.ZombieCoveFeature;

public class SZStructures {
	public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, SuspiciousZombification.MODID);
	public static final RegistryObject<StructureFeature<JigsawConfiguration>> ZOMBIE_COVE = STRUCTURES.register("zombie_cove", ZombieCoveFeature::new);
}
