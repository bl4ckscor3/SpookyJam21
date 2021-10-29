package suszombification.effect;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class VicinityAffectingEffect extends MobEffect {
	private final Function<Integer,Integer> areaSize;
	private final Predicate<LivingEntity> filter;
	private final Supplier<MobEffectInstance>[] effects;

	@SafeVarargs
	public VicinityAffectingEffect(MobEffectCategory category, int color, Function<Integer,Integer> areaSize, Predicate<LivingEntity> filter, Supplier<MobEffectInstance>... effects) {
		super(category, color);

		this.areaSize = areaSize;
		this.filter = filter;
		this.effects = effects;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		List<Entity> nearbyEntities = entity.level.getEntities(entity, entity.getBoundingBox().inflate(areaSize.apply(amplifier)), e -> e instanceof LivingEntity target && filter.test(target));

		for(Entity nearbyEntity : nearbyEntities) {
			LivingEntity nearby = (LivingEntity)nearbyEntity; //the filter parameter in getEntities makes sure this is true

			for(Supplier<MobEffectInstance> effect : effects) {
				nearby.addEffect(effect.get());
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 5 == 0;
	}
}
