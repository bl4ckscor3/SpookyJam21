package suszombification.structure;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.levelgen.feature.structures.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import suszombification.SuspiciousZombification;

public class ZombieOutpostPools {
	public static final StructureTemplatePool START = Pools.register(new StructureTemplatePool(new ResourceLocation(SuspiciousZombification.MODID, "zombie_outpost/main_rooms"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(ZombieOutpostPiece.legacy(new ResourceLocation(SuspiciousZombification.MODID, "zombie_outpost/main")), 1)), StructureTemplatePool.Projection.RIGID));

	static {
		Pools.register(new StructureTemplatePool(new ResourceLocation("zombie_outpost/feature_plates"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("zombie_outpost/feature_plate"), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
		Pools.register(new StructureTemplatePool(new ResourceLocation("zombie_outpost/features"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("zombie_outpost/feature_ritual_room"), 1), Pair.of(StructurePoolElement.legacy("zombie_outpost/feature_zombification_room"), 1), Pair.of(ZombieOutpostPiece.legacy("zombie_outpost/feature_storage_room"), 1), Pair.of(StructurePoolElement.empty(), 6)), StructureTemplatePool.Projection.RIGID));
	}

	public static class ZombieOutpostPiece extends LegacySinglePoolElement {
		public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(ResourceLocation path) {
			return legacy(path, ConfiguredStructures.ZOMBIE_OUTPOST);
		}

		public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(ResourceLocation path, StructureProcessorList processorList) {
			return projection -> new ZombieOutpostPiece(Either.left(path), () -> processorList, projection);
		}

		protected ZombieOutpostPiece(Either<ResourceLocation, StructureTemplate> template, Supplier<StructureProcessorList> processorList, StructureTemplatePool.Projection projection) {
			super(template, processorList, projection);
		}

		@Override
		protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox box, boolean keepJigsaws) {
			StructurePlaceSettings settings = super.getSettings(rotation, box, keepJigsaws);

			settings.popProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR); //do not ignore structure blocks, we need them for the spawner placement
			return settings;
		}

		@Override
		public void handleDataMarker(LevelAccessor level, StructureTemplate.StructureBlockInfo structureBlockInfo, BlockPos pos, Rotation rotation, Random random, BoundingBox box) {
			String function = structureBlockInfo.nbt.getString("metadata");

			if ("spawner".equals(function)) {
				level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

				BlockEntity be = level.getBlockEntity(pos);

				if (be instanceof SpawnerBlockEntity spawnerBE) {
					BaseSpawner spawner = spawnerBE.getSpawner();
					EntityType<?> type = level.getBiome(pos).getBiomeCategory() == Biome.BiomeCategory.DESERT ? EntityType.HUSK : EntityType.ZOMBIE;

					spawner.setEntityId(type);
					be.setChanged();
				}
			}
		}
	}
}
