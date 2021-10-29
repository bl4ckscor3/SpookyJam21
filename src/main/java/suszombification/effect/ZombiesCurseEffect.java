package suszombification.effect;

import java.util.List;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import suszombification.SZEffects;
import suszombification.SZTags;

public class ZombiesCurseEffect extends VicinityAffectingEffect {
	public ZombiesCurseEffect(MobEffectCategory category, int color) {
		super(category, color,
				amplifier -> 15,
				e -> !e.getType().is(SZTags.EntityTypes.AFFECTED_BY_ZOMBIES_GRACE) && !e.hasEffect(SZEffects.DECOMPOSING.get()) && (e instanceof Player || e instanceof Animal),
				() -> new MobEffectInstance(SZEffects.DECOMPOSING.get(), 300));
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return List.of();
	}
}
