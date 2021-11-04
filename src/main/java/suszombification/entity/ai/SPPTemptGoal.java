package suszombification.entity.ai;

import java.util.function.Predicate;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import suszombification.SZItems;

public class SPPTemptGoal extends TemptGoal {
	private final Ingredient ingredients;
	private final Predicate<ItemStack> ingredientPredicate;

	public SPPTemptGoal(CreatureEntity mob, double speedModifier, Ingredient ingredients, boolean canScare) {
		this(mob, speedModifier, ingredients, canScare, stack -> false);
	}

	public SPPTemptGoal(CreatureEntity mob, double speedModifier, Ingredient ingredients, boolean canScare, Predicate<ItemStack> ingredientPredicate) {
		super(mob, speedModifier, Ingredient.of(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()), canScare);
		this.ingredients = ingredients;
		this.ingredientPredicate = ingredientPredicate;
	}

	@Override
	public boolean shouldFollow(LivingEntity entity) {
		for(Hand hand : Hand.values()) {
			ItemStack stack = entity.getItemInHand(hand);

			if(stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && stack.hasTag() && stack.getTag().contains("Ingredient")) {
				CompoundNBT ingredientTag = stack.getTag().getCompound("Ingredient");
				ItemStack ingredient = ItemStack.of(ingredientTag);

				return ingredients.test(ingredient) || ingredientPredicate.test(ingredient);
			}
		}

		return false;
	}
}
