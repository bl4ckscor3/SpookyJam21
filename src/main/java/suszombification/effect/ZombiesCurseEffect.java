package suszombification.effect;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import suszombification.SZEffects;
import suszombification.SZTags;

public class ZombiesCurseEffect extends VicinityAffectingEffect {
	public ZombiesCurseEffect(EffectType category, int color) {
		super(category, color,
				amplifier -> 15,
				e -> { return
						!e.getType().is(SZTags.EntityTypes.AFFECTED_BY_ZOMBIES_GRACE)
						&& !e.hasEffect(SZEffects.DECOMPOSING.get())
						&& (e instanceof PlayerEntity || (e instanceof AnimalEntity
								&& !e.hasCustomName() //don't affect animals with name tags
								&& (!(e instanceof TameableEntity) || !((TameableEntity)e).isTame()) //don't affect tamed animals
								&& (!(e instanceof AbstractFishEntity) || !((AbstractFishEntity)e).fromBucket()))); //don't affect animals that were spawned from a bucket
				},
				() -> new EffectInstance(SZEffects.DECOMPOSING.get(), 300));
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return new ArrayList<>();
	}
}
