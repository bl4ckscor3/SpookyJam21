package suszombification.entity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

public interface ZombifiedAnimal {
	Map<EntityType<?>, EntityType<? extends Animal>> VANILLA_TO_ZOMBIFIED = new HashMap<>();

	EntityType<? extends Animal> getNormalVariant();

	default void readFromVanilla(Animal animal) {}

	default void writeToVanilla(Animal animal) {}

	boolean isConverting();

	void setConverting();

	void setConversionTime(int conversionTime);

	default void startConverting(int conversionTime) {
		Animal animal = (Animal)this;

		setConversionTime(conversionTime);
		setConverting();
		animal.removeEffect(MobEffects.WEAKNESS);
		animal.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, conversionTime, Math.min(animal.level.getDifficulty().getId() - 1, 0)));
		animal.level.broadcastEntityEvent(animal, EntityEvent.ZOMBIE_CONVERTING);
	}

	default void finishConversion(ServerLevel level) {
		Animal zombifiedAnimal = (Animal)this;
		Animal vanillaAnimal = zombifiedAnimal.convertTo(getNormalVariant(), false);

		vanillaAnimal.finalizeSpawn(level, level.getCurrentDifficultyAt(vanillaAnimal.blockPosition()), MobSpawnType.CONVERSION, null, null);
		writeToVanilla(vanillaAnimal);
		vanillaAnimal.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));

		if(!zombifiedAnimal.isSilent())
			level.levelEvent(null, LevelEvent.SOUND_ZOMBIE_CONVERTED, zombifiedAnimal.blockPosition(), 0);

		ForgeEventFactory.onLivingConvert(zombifiedAnimal, vanillaAnimal);
	}

	default int getConversionProgress() {
		int progress = 1;
		Animal animal = (Animal)this;

		if(animal.getRandom().nextFloat() < 0.01F) {
			int buffCount = 0;
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

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
