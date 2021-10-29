package suszombification.effect;

import java.util.List;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import suszombification.SZEffects;

public class ZombiesCurseEffect extends VicinityAffectingEffect {
	public ZombiesCurseEffect(MobEffectCategory category, int color) {
		super(category, color, amplifier -> 15, () -> new MobEffectInstance(SZEffects.DECOMPOSING.get(), 300));
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return List.of();
	}
}
