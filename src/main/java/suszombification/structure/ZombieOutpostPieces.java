package suszombification.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import suszombification.SuspiciousZombification;

public class ZombieOutpostPieces {
	public static void addPieces(StructureManager structureManager, BlockPos pos, Rotation rotation, StructurePieceAccessor pieces, Random random, NoneFeatureConfiguration config){
		pieces.addPiece(new ZombieOutpostPieces.MainPiece(structureManager, new ResourceLocation(SuspiciousZombification.MODID, "zombie_outpost/main"), pos, rotation));
	}

	public static class MainPiece extends TemplateStructurePiece {
		public MainPiece(StructureManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation) {
			super(ConfiguredStructures.ZOMBIE_OUTPOST_MAIN, 0, structureManager, location, location.toString(), makeSettings(rotation), pos);
		}

		public MainPiece(ServerLevel level, CompoundTag tag) {
			super(ConfiguredStructures.ZOMBIE_OUTPOST_MAIN, tag, level, path -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
		}

		private static StructurePlaceSettings makeSettings(Rotation rotation) {
			return new StructurePlaceSettings().setRotation(rotation).setMirror(Mirror.NONE)
					.addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR)
					.addProcessor(new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.4F), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.defaultBlockState()))))
					.addProcessor(new RuleProcessor(List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.4F), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICKS.defaultBlockState()))));
		}

		@Override
		protected void addAdditionalSaveData(ServerLevel pLevel, CompoundTag pTag) {
			super.addAdditionalSaveData(pLevel, pTag);
			pTag.putString("Rot", placeSettings.getRotation().name());
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor level, Random random, BoundingBox box) {
			if ("spawner".equals(function)) {
				level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

				BlockEntity be = level.getBlockEntity(pos);

				if (be instanceof SpawnerBlockEntity) {
					BaseSpawner spawner = ((SpawnerBlockEntity)be).getSpawner();
					EntityType<?> type = level.getBiome(pos).getBiomeCategory() == Biome.BiomeCategory.DESERT ? EntityType.HUSK : EntityType.ZOMBIE;

					spawner.setEntityId(type);
					be.setChanged();
				}
			}
		}
	}
}
