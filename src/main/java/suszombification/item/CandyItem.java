package suszombification.item;

import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.SuspiciousStewEffects;

public class CandyItem extends Item {
	private final SuspiciousStewEffects suspiciousPumpkinPieEffects;

	public CandyItem(Holder<MobEffect> effect, int effectDuration, Properties properties) {
		super(properties);

		if (!effect.value().isInstantenous())
			effectDuration *= 20;

		suspiciousPumpkinPieEffects = new SuspiciousStewEffects(List.of(new SuspiciousStewEffects.Entry(effect, effectDuration)));
	}

	public SuspiciousStewEffects getEffects() {
		return suspiciousPumpkinPieEffects;
	}
}
