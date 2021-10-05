package suszombification.items;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;

public class CandyItem extends Item {

	private final MobEffect suspiciousPumpkinPieEffect;
	private final int effectDuration;

	public CandyItem(MobEffect effect, int effectDuration, Properties properties) {
		super(properties);
		this.suspiciousPumpkinPieEffect = effect;

		if (effect.isInstantenous()) {
			this.effectDuration = effectDuration;
		} else {
			this.effectDuration = effectDuration * 20;
		}
	}

	public MobEffect getEffect() {
		return suspiciousPumpkinPieEffect;
	}

	public int getEffectDuration() {
		return effectDuration;
	}
}
