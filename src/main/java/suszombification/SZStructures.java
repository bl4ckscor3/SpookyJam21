package suszombification;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.structure.ZombieOutpostFeature;

public class SZStructures {
	public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, SuspiciousZombification.MODID);

	public static final RegistryObject<StructureFeature<JigsawConfiguration>> ZOMBIE_OUTPOST = STRUCTURES.register("zombie_outpost", () -> new ZombieOutpostFeature(JigsawConfiguration.CODEC));
}
