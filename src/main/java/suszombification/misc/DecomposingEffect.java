package suszombification.misc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.entity.ZombifiedAnimal;

public class DecomposingEffect extends MobEffect {
	public DecomposingEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		ItemStack rottenFlesh = new ItemStack(Items.ROTTEN_FLESH);

		if (!entity.level.isClientSide) {
			if (entity instanceof Animal animal) {
				EntityType<? extends Animal> conversionType = ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.get(animal.getType());

				if (conversionType != null && ForgeEventFactory.canLivingConvert(animal, conversionType, timer -> {})) {
					Mob convertedAnimal = animal.convertTo(conversionType, false);

					convertedAnimal.finalizeSpawn((ServerLevel)animal.level, animal.level.getCurrentDifficultyAt(convertedAnimal.blockPosition()), MobSpawnType.CONVERSION, null, null);
					((ZombifiedAnimal)convertedAnimal).readFromVanilla(animal);
					ForgeEventFactory.onLivingConvert(animal, convertedAnimal);

					if (!animal.isSilent()) {
						animal.level.levelEvent(null, LevelEvent.SOUND_ZOMBIE_INFECTED, animal.blockPosition(), 0);
					}
				}
				else {
					entity.hurt(SZDamageSources.DECOMPOSING, entity.getHealth() * 2);

					if (entity.isDeadOrDying())
						entity.level.addFreshEntity(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), rottenFlesh));
				}
			}
			else if (entity instanceof Player player) {
				if (!player.getAbilities().instabuild) {
					entity.hurt(SZDamageSources.DECOMPOSING, entity.getHealth() * 2);
					entity.level.addFreshEntity(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), rottenFlesh));
				}
			}
		}

	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration == 1;
	}
}
