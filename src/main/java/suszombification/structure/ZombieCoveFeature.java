package suszombification.structure;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.PillagerOutpostFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ZombieCoveFeature extends StructureFeature<JigsawConfiguration> {
	public static final Codec<JigsawConfiguration> CODEC = RecordCodecBuilder.create((codec) -> {
		return codec.group(
				StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool),
				Codec.intRange(0, 30).fieldOf("size").forGetter(JigsawConfiguration::maxDepth)
				).apply(codec, JigsawConfiguration::new);
	});

	public ZombieCoveFeature() {
		super(CODEC, ZombieCoveFeature::createPiecesGenerator, PostPlacementProcessor.NONE);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> ctx) {
		if(!PillagerOutpostFeature.checkLocation(ctx))
			return Optional.empty();

		return JigsawPlacement.addPieces(ctx, PoolElementStructurePiece::new, ctx.chunkPos().getMiddleBlockPosition(0), false, true);
	}
}
