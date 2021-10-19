package suszombification.effect;

import java.util.List;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StenchEffect extends MobEffect {
	public StenchEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		List<Entity> nearbyEntities = entity.level.getEntities(entity, entity.getBoundingBox().inflate(Math.max((amplifier + 1) * 3, 10)));

		for(Entity nearbyEntity : nearbyEntities) {
			if(nearbyEntity instanceof LivingEntity nearby) {
				nearby.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));
				nearby.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));
				nearby.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 5 == 0;
	}
}
