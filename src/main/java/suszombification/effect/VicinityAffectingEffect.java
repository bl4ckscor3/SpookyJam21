package suszombification.effect;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class VicinityAffectingEffect extends MobEffect {
	private final Function<Integer,Integer> areaSize;
	private final Supplier<MobEffectInstance>[] effects;

	public VicinityAffectingEffect(MobEffectCategory category, int color, Function<Integer,Integer> areaSize, Supplier<MobEffectInstance>... effects) {
		super(category, color);

		this.areaSize = areaSize;
		this.effects = effects;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		List<Entity> nearbyEntities = entity.level.getEntities(entity, entity.getBoundingBox().inflate(areaSize.apply(amplifier)));

		for(Entity nearbyEntity : nearbyEntities) {
			if(nearbyEntity instanceof LivingEntity nearby) {
				for(Supplier<MobEffectInstance> effect : effects) {
					nearby.addEffect(effect.get());
				}
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 5 == 0;
	}
}
