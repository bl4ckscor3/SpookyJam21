package suszombification.structure;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class ZombieOutpostFeature extends StructureFeature<NoneFeatureConfiguration> {
	public ZombieOutpostFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
		return ZombieOutpostStart::new;
	}

	public static class ZombieOutpostStart extends StructureStart<NoneFeatureConfiguration> {
		public ZombieOutpostStart(StructureFeature<NoneFeatureConfiguration> feature, ChunkPos pos, int references, long seed) {
			super(feature, pos, references, seed);
		}

		@Override
		public void generatePieces(RegistryAccess registry, ChunkGenerator generator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, NoneFeatureConfiguration configuration, LevelHeightAccessor level) {
			BlockPos pos = new BlockPos(chunkPos.getMinBlockX(), 90, chunkPos.getMinBlockZ());
			Rotation rotation = Rotation.getRandom(random);

			ZombieOutpostPieces.addPieces(structureManager, pos, rotation, this, random, configuration);
		}
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}
}
