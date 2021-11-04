package suszombification.compat;

import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import samebutdifferent.trickortreat.registry.ModEffects;
import samebutdifferent.trickortreat.registry.ModItems;
import suszombification.misc.PieEffect;

public class TrickOrTreatCompat {
	public static void addEffects(List<PieEffect> pieEffects) {
		pieEffects.add(new PieEffect(stack -> stack.getItem() == ModItems.FIREFINGERS.get(), () -> new EffectInstance(ModEffects.FIREFINGER.get(), 450), () -> null, TextFormatting.GOLD, "trickortreat"));
		pieEffects.add(new PieEffect(stack -> stack.getItem() == ModItems.DEADISH_FISH.get(), () -> new EffectInstance(ModEffects.WATERBOLT.get(), 300), () -> null, TextFormatting.GOLD, "trickortreat"));
		pieEffects.add(new PieEffect(stack -> stack.getItem() == ModItems.SCREAMBURSTS.get(), () -> new EffectInstance(ModEffects.SCARY.get(), 300), () -> null, TextFormatting.GOLD, "trickortreat"));
		pieEffects.add(new PieEffect(stack -> stack.getItem() == ModItems.MEMBRANE_BUTTER_CUPS.get(), () -> new EffectInstance(ModEffects.LIFE_LEECH.get(), 450), () -> null, TextFormatting.GOLD, "trickortreat"));
		pieEffects.add(new PieEffect(stack -> stack.getItem() == ModItems.BONEBREAKER.get(), () -> new EffectInstance(ModEffects.BONE_BREAKING.get(), 450), () -> null, TextFormatting.GOLD, "trickortreat"));
		pieEffects.add(new PieEffect(stack -> stack.getItem() == ModItems.SLIME_GUM.get(), () -> new EffectInstance(ModEffects.BOUNCY.get(), 450), () -> null, TextFormatting.GOLD, "trickortreat"));
		pieEffects.add(new PieEffect(stack -> stack.getItem() == ModItems.CHOCOLATE_SPIDER_EYE.get(), () -> new EffectInstance(ModEffects.CLIMBING.get(), 750), () -> null, TextFormatting.GOLD, "trickortreat"));
		pieEffects.add(new PieEffect(stack -> stack.getItem() == ModItems.SOUR_PATCH_ZOMBIES.get(), () -> new EffectInstance(ModEffects.ROTTEN_BITE.get(), 450), () -> new EffectInstance(Effects.HUNGER, 600), TextFormatting.GOLD, "trickortreat"));
	}

	public static Ingredient getCandies() {
		return Ingredient.of(ModItems.FIREFINGERS.get(),
				ModItems.FIZZLERS.get(),
				ModItems.DEADISH_FISH.get(),
				ModItems.PEARL_POP.get(),
				ModItems.SCREAMBURSTS.get(),
				ModItems.EYECE_CREAM.get(),
				ModItems.MEMBRANE_BUTTER_CUPS.get(),
				ModItems.BONEBREAKER.get(),
				ModItems.SLIME_GUM.get(),
				ModItems.CHOCOLATE_SPIDER_EYE.get(),
				ModItems.SOUR_PATCH_ZOMBIES.get());
	}

	public static boolean attemptCandyEffect(LivingEntity entity, World level, ItemStack stack) {
		if(stack.getItem() instanceof samebutdifferent.trickortreat.item.CandyItem) {
			//trick or treat uses this event to add effects for a few of their candies
			MinecraftForge.EVENT_BUS.post(new LivingEntityUseItemEvent.Finish(entity, stack.copy(), entity.getUseItemRemainingTicks(), stack.finishUsingItem(level, entity)));
			return true;
		}

		return false;
	}
}
