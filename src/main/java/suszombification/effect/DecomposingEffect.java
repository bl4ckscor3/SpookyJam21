package suszombification.effect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.potion.EffectType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.SZDamageSources;
import suszombification.SZLootTables;
import suszombification.entity.ZombifiedAnimal;

public class DecomposingEffect extends Effect {
	public DecomposingEffect(EffectType category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if(!entity.level.isClientSide) {
			if(entity instanceof AnimalEntity) {
				AnimalEntity animal = (AnimalEntity)entity;
				EntityType<? extends AnimalEntity> conversionType = ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.get(animal.getType());

				if(conversionType != null && ForgeEventFactory.canLivingConvert(animal, conversionType, timer -> {})) {
					MobEntity convertedAnimal = animal.convertTo(conversionType, false);

					convertedAnimal.finalizeSpawn((ServerWorld)animal.level, animal.level.getCurrentDifficultyAt(convertedAnimal.blockPosition()), SpawnReason.CONVERSION, null, null);
					((ZombifiedAnimal)convertedAnimal).readFromVanilla(animal);
					ForgeEventFactory.onLivingConvert(animal, convertedAnimal);

					if(!animal.isSilent())
						animal.level.levelEvent(null, Constants.WorldEvents.ZOMBIE_INFECT_SOUND, animal.blockPosition(), 0);
				}
				else {
					entity.hurt(SZDamageSources.DECOMPOSING, Float.MAX_VALUE);

					if(entity.isDeadOrDying())
						spawnDecomposingDrops(entity);
				}
			}
			else if(entity instanceof PlayerEntity && !((PlayerEntity)entity).abilities.instabuild) {
				entity.hurt(SZDamageSources.DECOMPOSING, Float.MAX_VALUE);
				spawnDecomposingDrops(entity);
			}
		}
	}

	private void spawnDecomposingDrops(LivingEntity entity) {
		LootTable lootTable = entity.level.getServer().getLootTables().get(SZLootTables.DEATH_BY_DECOMPOSING);
		LootContext.Builder builder = new LootContext.Builder((ServerLevel)entity.level)
				.withRandom(entity.getRandom())
				.withParameter(LootContextParams.THIS_ENTITY, entity)
				.withParameter(LootContextParams.ORIGIN, entity.position())
				.withParameter(LootContextParams.DAMAGE_SOURCE, SZDamageSources.DECOMPOSING);
		LootContext ctx = builder.create(LootContextParamSets.ENTITY);

		lootTable.getRandomItems(ctx).forEach(entity::spawnAtLocation);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration == 1;
	}
}
