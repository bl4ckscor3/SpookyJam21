package suszombification.item;

import net.minecraft.item.Item;
import net.minecraft.potion.Effect;

public class CandyItem extends Item {
	private final Effect suspiciousPumpkinPieEffect;
	private final int effectDuration;

	public CandyItem(Effect effect, int effectDuration, Properties properties) {
		super(properties);
		this.suspiciousPumpkinPieEffect = effect;

		if(effect.isInstantenous())
			this.effectDuration = effectDuration;
		else
			this.effectDuration = effectDuration * 20;
	}

	public Effect getEffect() {
		return suspiciousPumpkinPieEffect;
	}

	public int getEffectDuration() {
		return effectDuration;
	}
}
