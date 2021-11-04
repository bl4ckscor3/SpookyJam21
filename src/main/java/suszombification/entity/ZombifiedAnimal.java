package suszombification.entity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;

public interface ZombifiedAnimal {
	Map<EntityType<?>, EntityType<? extends AnimalEntity>> VANILLA_TO_ZOMBIFIED = new HashMap<>();

	EntityType<? extends AnimalEntity> getNormalVariant();

	default void readFromVanilla(AnimalEntity animal) {}

	default void writeToVanilla(AnimalEntity animal) {}

	boolean isConverting();

	void setConverting();

	void setConversionTime(int conversionTime);

	int getConversionTime();

	default void startConverting(int conversionTime) {
		AnimalEntity animal = (AnimalEntity)this;

		setConversionTime(conversionTime);
		setConverting();
		animal.removeEffect(Effects.WEAKNESS);
		animal.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, conversionTime, Math.min(animal.level.getDifficulty().getId() - 1, 0)));
		animal.level.broadcastEntityEvent(animal, (byte)16);
	}

	default void finishConversion(ServerWorld level) {
		AnimalEntity zombifiedAnimal = (AnimalEntity)this;
		AnimalEntity vanillaAnimal = zombifiedAnimal.convertTo(getNormalVariant(), false);

		vanillaAnimal.finalizeSpawn(level, level.getCurrentDifficultyAt(vanillaAnimal.blockPosition()), SpawnReason.CONVERSION, null, null);
		writeToVanilla(vanillaAnimal);
		vanillaAnimal.addEffect(new EffectInstance(Effects.CONFUSION, 200, 0));

		if(!zombifiedAnimal.isSilent())
			level.levelEvent(null, Constants.WorldEvents.ZOMBIE_VILLAGER_CONVERTED_SOUND, zombifiedAnimal.blockPosition(), 0);

		ForgeEventFactory.onLivingConvert(zombifiedAnimal, vanillaAnimal);
	}

	default int getConversionProgress() {
		int progress = 1;
		AnimalEntity animal = (AnimalEntity)this;

		if(animal.getRandom().nextFloat() < 0.01F) {
			int buffCount = 0;
			BlockPos.Mutable pos = new BlockPos.Mutable();

			for(int x = (int)animal.getX() - 4; x < animal.getX() + 4 && buffCount < 14; ++x) {
				for(int y = (int)animal.getY() - 4; y < animal.getY() + 4 && buffCount < 14; ++y) {
					for(int z = (int)animal.getZ() - 4; z < animal.getZ() + 4 && buffCount < 14; ++z) {
						BlockState state = animal.level.getBlockState(pos.set(x, y, z));

						if(state.is(BlockTags.WOODEN_FENCES) || state.is(Blocks.HAY_BLOCK)) {
							if(animal.getRandom().nextFloat() < 0.3F)
								++progress;

							++buffCount;
						}
					}
				}
			}
		}

		return progress;
	}
}
