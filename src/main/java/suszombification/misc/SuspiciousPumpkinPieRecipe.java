package suszombification.misc;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import suszombification.SZTags;
import suszombification.item.CandyItem;
import suszombification.item.SuspiciousPumpkinPieItem;
import suszombification.registration.SZItems;
import suszombification.registration.SZRecipeSerializers;

public class SuspiciousPumpkinPieRecipe extends CustomRecipe {
	//@formatter:off
	private static final Ingredient INGREDIENTS = Ingredient.of(
			Items.GOLDEN_APPLE,
			Items.ROTTEN_FLESH,
			Items.CHICKEN,
			Items.FEATHER,
			Items.BEEF,
			Items.LEATHER,
			Items.PORKCHOP,
			Items.MUTTON,
			Items.STRING,
			Items.GUNPOWDER,
			SZItems.SPOILED_MILK_BUCKET.get(),
			SZItems.ROTTEN_EGG.get());
	//@formatter:on

	public SuspiciousPumpkinPieRecipe(CraftingBookCategory craftingBookCategory) {
		super(craftingBookCategory);
	}

	@Override
	public boolean matches(CraftingInput inv, Level level) {
		if (inv.width() * inv.height() < 4)
			return false;

		boolean hasSpecialIngredient = false;
		boolean hasEgg = false;
		boolean hasSugar = false;
		boolean hasPumpkin = false;

		for (int i = 0; i < inv.size(); ++i) {
			ItemStack stack = inv.getItem(i);

			if (!stack.isEmpty()) {
				if (stack.is(Items.SUGAR) && !hasSugar)
					hasSugar = true;
				else if (stack.is(Items.EGG) && !hasEgg)
					hasEgg = true;
				else if (isIngredient(stack) && !hasSpecialIngredient)
					hasSpecialIngredient = true;
				else {
					if (!stack.is(Blocks.PUMPKIN.asItem()) || hasPumpkin)
						return false;

					hasPumpkin = true;
				}
			}
		}

		return hasSpecialIngredient && hasSugar && hasEgg && hasPumpkin;
	}

	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider lookupProvider) {
		ItemStack ingredient = ItemStack.EMPTY;
		ItemStack suspiciousPumpkinPie = new ItemStack(SZItems.SUSPICIOUS_PUMPKIN_PIE.get(), 1);

		for (int i = 0; i < inv.size(); ++i) {
			ItemStack stack = inv.getItem(i);

			if (!stack.isEmpty() && isIngredient(stack)) {
				ingredient = stack.copy();
				break;
			}
		}

		SuspiciousPumpkinPieItem.saveIngredient(suspiciousPumpkinPie, ingredient);
		return suspiciousPumpkinPie;
	}

	private boolean isIngredient(ItemStack stack) {
		return stack.getItem() instanceof CandyItem || INGREDIENTS.test(stack) || stack.is(ItemTags.WOOL) || stack.is(SZTags.Items.ROTTEN_WOOL);
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SZRecipeSerializers.SUSPICIOUS_PUMPKIN_PIE.get();
	}
}
