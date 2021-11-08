package suszombification.structure;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.PillagerOutpostStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import suszombification.SuspiciousZombification;

public class ZombieCoveFeature extends Structure<NoFeatureConfig> {
	private final List<Spawners> structureMonsters;
	private final String startPiece;

	public ZombieCoveFeature(EntityType<?> typeToSpawn, String startPiece, Codec<NoFeatureConfig> codec) {
		super(codec);

		structureMonsters = ImmutableList.of(new Spawners(typeToSpawn, 100, 3, 6));
		this.startPiece = startPiece;
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return Start::new;
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public List<Spawners> getDefaultSpawnList() {
		return structureMonsters;
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider biomeProvider, long seed, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos potentialPos, NoFeatureConfig config) {
		return !((PillagerOutpostStructure)PILLAGER_OUTPOST).isNearVillage(generator, seed, random, chunkX, chunkZ);
	}

	public class Start extends StructureStart<NoFeatureConfig> {
		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed) {
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}

		@Override
		public void generatePieces(DynamicRegistries registryAccess, ChunkGenerator chunkGenerator, TemplateManager structureManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			JigsawManager.addPieces(registryAccess,
					new VillageConfig(() -> registryAccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(SuspiciousZombification.MODID, "zombie_cove/" + startPiece)), 10),
					AbstractVillagePiece::new, chunkGenerator, structureManager,
					new BlockPos(chunkX * 16, 0, chunkZ * 16),
					pieces, random, false, true);
			calculateBoundingBox();
		}
	}
}
