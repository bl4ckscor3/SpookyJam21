package suszombification.datagen;

import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import suszombification.misc.SuspiciousPumpkinPieRecipe;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		SpecialRecipeBuilder.special(SuspiciousPumpkinPieRecipe.serializer).save(consumer, "suspicious_pumpkin_pie");
	}
}
