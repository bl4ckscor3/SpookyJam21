package suszombification.structure;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureStart;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import suszombification.SuspiciousZombification;

public class ZombieCoveFeature extends StructureFeature<NoneFeatureConfiguration> {
	private final List<SpawnerData> structureMonsters;
	private final String startPiece;

	public ZombieCoveFeature(EntityType<?> typeToSpawn, String startPiece, Codec<NoneFeatureConfiguration> codec) {
		super(codec);

		structureMonsters = ImmutableList.of(new SpawnerData(typeToSpawn, 100, 3, 6));
		this.startPiece = startPiece;
	}

	@Override
	public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
		return Start::new;
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public List<SpawnerData> getDefaultSpawnList() {
		return structureMonsters;
	}

	public class Start extends NoiseAffectingStructureStart<NoneFeatureConfiguration> {
		public Start(StructureFeature<NoneFeatureConfiguration> structure, ChunkPos chunkPos, int reference, long seed) {
			super(structure, chunkPos, reference, seed);
		}

		@Override
		public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, NoneFeatureConfiguration config, LevelHeightAccessor heightAccessor) {
			JigsawPlacement.addPieces(registryAccess,
					new JigsawConfiguration(() -> registryAccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(SuspiciousZombification.MODID, "zombie_cove/" + startPiece)), 10),
					PoolElementStructurePiece::new, chunkGenerator, structureManager,
					new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ()),
					this, random, false, true, heightAccessor);
		}
	}
}
