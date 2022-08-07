package suszombification.compat;

import java.util.List;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import suszombification.item.SuspiciousPumpkinPieItem.PieEffect;

public class TrickOrTreatCompat {
	public static void addEffects(List<PieEffect> pieEffects) {
		//		pieEffects.add(new PieEffect(stack -> stack.is(ModItems.FIREFINGERS.get()), () -> new MobEffectInstance(ModEffects.FIREFINGER.get(), 450), () -> null, ChatFormatting.GOLD, "trickortreat"));
		//		pieEffects.add(new PieEffect(stack -> stack.is(ModItems.DEADISH_FISH.get()), () -> new MobEffectInstance(ModEffects.WATERBOLT.get(), 300), () -> null, ChatFormatting.GOLD, "trickortreat"));
		//		pieEffects.add(new PieEffect(stack -> stack.is(ModItems.SCREAMBURSTS.get()), () -> new MobEffectInstance(ModEffects.SCARY.get(), 300), () -> null, ChatFormatting.GOLD, "trickortreat"));
		//		pieEffects.add(new PieEffect(stack -> stack.is(ModItems.MEMBRANE_BUTTER_CUPS.get()), () -> new MobEffectInstance(ModEffects.LIFE_LEECH.get(), 450), () -> null, ChatFormatting.GOLD, "trickortreat"));
		//		pieEffects.add(new PieEffect(stack -> stack.is(ModItems.BONEBREAKER.get()), () -> new MobEffectInstance(ModEffects.BONE_BREAKING.get(), 450), () -> null, ChatFormatting.GOLD, "trickortreat"));
		//		pieEffects.add(new PieEffect(stack -> stack.is(ModItems.SLIME_GUM.get()), () -> new MobEffectInstance(ModEffects.BOUNCY.get(), 450), () -> null, ChatFormatting.GOLD, "trickortreat"));
		//		pieEffects.add(new PieEffect(stack -> stack.is(ModItems.CHOCOLATE_SPIDER_EYE.get()), () -> new MobEffectInstance(ModEffects.CLIMBING.get(), 750), () -> null, ChatFormatting.GOLD, "trickortreat"));
		//		pieEffects.add(new PieEffect(stack -> stack.is(ModItems.SOUR_PATCH_ZOMBIES.get()), () -> new MobEffectInstance(ModEffects.ROTTEN_BITE.get(), 450), () -> new MobEffectInstance(MobEffects.HUNGER, 600), ChatFormatting.GOLD, "trickortreat"));
	}

	public static Ingredient getCandies() {
		return Ingredient.of(/*ModItems.FIREFINGERS.get(),
				ModItems.FIZZLERS.get(),
				ModItems.DEADISH_FISH.get(),
				ModItems.PEARL_POP.get(),
				ModItems.SCREAMBURSTS.get(),
				ModItems.EYECE_CREAM.get(),
				ModItems.MEMBRANE_BUTTER_CUPS.get(),
				ModItems.BONEBREAKER.get(),
				ModItems.SLIME_GUM.get(),
				ModItems.CHOCOLATE_SPIDER_EYE.get(),
				ModItems.SOUR_PATCH_ZOMBIES.get()*/);
	}

	public static boolean attemptCandyEffect(LivingEntity entity, Level level, ItemStack stack) {
		//		if(stack.getItem() instanceof samebutdifferent.trickortreat.item.CandyItem) {
		//			//trick or treat uses this event to add effects for a few of their candies
		//			MinecraftForge.EVENT_BUS.post(new LivingEntityUseItemEvent.Finish(entity, stack.copy(), entity.getUseItemRemainingTicks(), stack.finishUsingItem(level, entity)));
		//			return true;
		//		}

		return false;
	}
}
