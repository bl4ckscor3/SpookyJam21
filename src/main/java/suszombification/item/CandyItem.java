package suszombification.item;

import java.util.List;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

public class CandyItem extends Item {
	private final List<SuspiciousEffectHolder.EffectEntry> suspiciousPumpkinPieEffect;

	public CandyItem(MobEffect effect, int effectDuration, Properties properties) {
		super(properties);

		if (!effect.isInstantenous())
			effectDuration = effectDuration * 20;

		this.suspiciousPumpkinPieEffect = List.of(new SuspiciousEffectHolder.EffectEntry(effect, effectDuration));
	}

	public List<SuspiciousEffectHolder.EffectEntry> getEffect() {
		return suspiciousPumpkinPieEffect;
	}
}
