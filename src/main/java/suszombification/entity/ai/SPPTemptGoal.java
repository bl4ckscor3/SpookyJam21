package suszombification.entity.ai;

import java.util.function.Predicate;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import suszombification.SZItems;

public class SPPTemptGoal extends TemptGoal {
	private final Ingredient ingredients;
	private final Predicate<ItemStack> ingredientPredicate;

	public SPPTemptGoal(PathfinderMob mob, double speedModifier, Ingredient ingredients, boolean canScare) {
		this(mob, speedModifier, ingredients, canScare, stack -> false);
	}

	public SPPTemptGoal(PathfinderMob mob, double speedModifier, Ingredient ingredients, boolean canScare, Predicate<ItemStack> ingredientPredicate) {
		super(mob, speedModifier, Ingredient.of(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()), canScare);
		this.ingredients = ingredients;
		this.ingredientPredicate = ingredientPredicate;
	}

	@Override
	public boolean shouldFollow(LivingEntity entity) {
		for(InteractionHand hand : InteractionHand.values()) {
			ItemStack stack = entity.getItemInHand(hand);

			if(stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && stack.hasTag() && stack.getTag().contains("Ingredient")) {
				CompoundTag ingredientTag = stack.getTag().getCompound("Ingredient");
				ItemStack ingredient = ItemStack.of(ingredientTag);

				return ingredients.test(ingredient) || ingredientPredicate.test(ingredient);
			}
		}

		return false;
	}
}
