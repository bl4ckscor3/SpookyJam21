package suszombification.misc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ObjectHolder;
import suszombification.SZItems;
import suszombification.SuspiciousZombification;
import suszombification.item.CandyItem;
import suszombification.item.SuspiciousPumpkinPieItem;

public class SuspiciousPumpkinPieRecipe extends CustomRecipe {
	@ObjectHolder(SuspiciousZombification.MODID + ":suspicious_pumpkin_pie")
	public static SimpleRecipeSerializer<SuspiciousPumpkinPieRecipe> serializer = null;
	private static final Ingredient INGREDIENTS = Ingredient.of(Items.GOLDEN_APPLE, Items.ROTTEN_FLESH, Items.CHICKEN, Items.FEATHER, Items.BEEF, Items.LEATHER, Items.PORKCHOP, Items.MUTTON); //TODO: add rotten items

	public SuspiciousPumpkinPieRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer inv, Level level) {
		boolean hasSpecialIngredient = false;
		boolean hasEgg = false;
		boolean hasSugar = false;
		boolean hasPumpkin = false;

		for(int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack stack = inv.getItem(i);

			if (!stack.isEmpty()) {
				if (stack.is(Items.SUGAR) && !hasSugar) {
					hasSugar = true;
				} else if (stack.is(Items.EGG) && !hasEgg) {
					hasEgg = true;
				} else if (isIngredient(stack) && !hasSpecialIngredient) {
					hasSpecialIngredient = true;
				} else {
					if (!stack.is(Blocks.PUMPKIN.asItem()) || hasPumpkin) {
						return false;
					}

					hasPumpkin = true;
				}
			}
		}

		return hasSpecialIngredient && hasSugar && hasEgg && hasPumpkin;
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		ItemStack ingredient = ItemStack.EMPTY;
		ItemStack suspiciousPumpkinPie = new ItemStack(SZItems.SUSPICIOUS_PUMPKIN_PIE.get(), 1);

		for(int i = 0; i < inv.getContainerSize(); ++i) {
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
		return stack.getItem() instanceof CandyItem || INGREDIENTS.test(stack) || ItemTags.WOOL.contains(stack.getItem());
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 2 && height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return serializer;
	}
}
