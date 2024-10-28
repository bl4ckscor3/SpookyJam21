package suszombification.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import suszombification.registration.SZEffects;

public class AmplifyingEffect extends MobEffect {
	public AmplifyingEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
		for (MobEffectInstance effect : entity.getActiveEffects()) {
			if (effect.getEffect() != SZEffects.AMPLIFYING.get() && effect.amplifier <= 63)
				effect.update(new MobEffectInstance(effect.getEffect(), effect.getDuration(), ((effect.amplifier + 1) * (amplifier + 2)) - 1));
		}

		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return duration == 1;
	}

	@Override
	public boolean isInstantenous() {
		return true;
	}
}
