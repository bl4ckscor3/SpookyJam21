package suszombification.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import suszombification.SZEffects;

public class AmplifyingEffect extends MobEffect {
	public AmplifyingEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		for (MobEffectInstance effect : entity.getActiveEffects()) {
			if (effect.getEffect() != SZEffects.AMPLIFYING.get() && effect.amplifier <= 63) {
				effect.update(new MobEffectInstance(effect.getEffect(), effect.getDuration(), ((effect.amplifier + 1) * (amplifier + 2)) - 1));
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration == 1;
	}

	@Override
	public boolean isInstantenous() {
		return true;
	}
}
