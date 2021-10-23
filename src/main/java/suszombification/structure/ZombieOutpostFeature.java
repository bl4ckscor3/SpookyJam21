package suszombification.structure;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class ZombieOutpostFeature extends JigsawFeature {
	public ZombieOutpostFeature(Codec<JigsawConfiguration> codec) {
		super(codec, 0, true, true);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}
}
