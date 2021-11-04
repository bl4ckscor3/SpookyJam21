package suszombification.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import suszombification.SZEffects;

public class AmplifyingEffect extends Effect {
	public AmplifyingEffect(EffectType category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		for(EffectInstance effect : entity.getActiveEffects()) {
			if(effect.getEffect() != SZEffects.AMPLIFYING.get() && effect.amplifier <= 63)
				effect.update(new EffectInstance(effect.getEffect(), effect.getDuration(), ((effect.amplifier + 1) * (amplifier + 2)) - 1));
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
