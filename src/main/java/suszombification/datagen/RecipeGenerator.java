package suszombification.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import suszombification.misc.SuspiciousPumpkinPieRecipe;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZItems;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		SpecialRecipeBuilder.special(SuspiciousPumpkinPieRecipe::new).save(recipeOutput, "suspicious_pumpkin_pie");
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, SZItems.PORKCHOP_ON_A_STICK.get())
		.pattern("R ")
		.pattern(" P")
		.define('R', Items.FISHING_ROD)
		.define('P', Items.PORKCHOP)
		.unlockedBy("has_porkchop", has(Items.PORKCHOP))
		.save(recipeOutput);
		//@formatter:on

		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_BLACK, SZBlocks.BLACK_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_BLUE, SZBlocks.BLUE_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_BROWN, SZBlocks.BROWN_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_CYAN, SZBlocks.CYAN_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_GRAY, SZBlocks.GRAY_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_GREEN, SZBlocks.GREEN_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_LIGHT_BLUE, SZBlocks.LIGHT_BLUE_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_LIGHT_GRAY, SZBlocks.LIGHT_GRAY_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_LIME, SZBlocks.LIME_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_MAGENTA, SZBlocks.MAGENTA_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_ORANGE, SZBlocks.ORANGE_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_PINK, SZBlocks.PINK_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_PURPLE, SZBlocks.PURPLE_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_RED, SZBlocks.RED_ROTTEN_WOOL.get());
		addColoredWoolRecipe(recipeOutput, Tags.Items.DYES_YELLOW, SZBlocks.YELLOW_ROTTEN_WOOL.get());
	}

	protected final void addColoredWoolRecipe(RecipeOutput recipeOutput, TagKey<Item> dye, ItemLike result) {
		//@formatter:off
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result)
		.group("suszombification:rotten_wool")
		.requires(dye)
		.requires(SZBlocks.WHITE_ROTTEN_WOOl.get())
		.unlockedBy("has_wool", has(SZBlocks.WHITE_ROTTEN_WOOl.get()))
		.save(recipeOutput);
		//@formatter:on
	}
}
