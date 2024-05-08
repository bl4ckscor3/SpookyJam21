package suszombification.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import suszombification.misc.SuspiciousPumpkinPieRecipe;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZItems;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
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

		List<TagKey<Item>> dyes = List.of(
				Tags.Items.DYES_BLACK,
				Tags.Items.DYES_BLUE,
				Tags.Items.DYES_BROWN,
				Tags.Items.DYES_CYAN,
				Tags.Items.DYES_GRAY,
				Tags.Items.DYES_GREEN,
				Tags.Items.DYES_LIGHT_BLUE,
				Tags.Items.DYES_LIGHT_GRAY,
				Tags.Items.DYES_LIME,
				Tags.Items.DYES_MAGENTA,
				Tags.Items.DYES_ORANGE,
				Tags.Items.DYES_PINK,
				Tags.Items.DYES_PURPLE,
				Tags.Items.DYES_RED,
				Tags.Items.DYES_YELLOW,
				Tags.Items.DYES_WHITE);
		List<Item> woolBlocks = Stream.of(
				SZBlocks.BLACK_ROTTEN_WOOL,
				SZBlocks.BLUE_ROTTEN_WOOL,
				SZBlocks.BROWN_ROTTEN_WOOL,
				SZBlocks.CYAN_ROTTEN_WOOL,
				SZBlocks.GRAY_ROTTEN_WOOL,
				SZBlocks.GREEN_ROTTEN_WOOL,
				SZBlocks.LIGHT_BLUE_ROTTEN_WOOL,
				SZBlocks.LIGHT_GRAY_ROTTEN_WOOL,
				SZBlocks.LIME_ROTTEN_WOOL,
				SZBlocks.MAGENTA_ROTTEN_WOOL,
				SZBlocks.ORANGE_ROTTEN_WOOL,
				SZBlocks.PINK_ROTTEN_WOOL,
				SZBlocks.PURPLE_ROTTEN_WOOL,
				SZBlocks.RED_ROTTEN_WOOL,
				SZBlocks.YELLOW_ROTTEN_WOOL,
				SZBlocks.WHITE_ROTTEN_WOOL
		).map(DeferredHolder::get).map(ItemLike::asItem).toList();
		//@formatter:on

		for (int i = 0; i < dyes.size(); i++) {
			TagKey<Item> dye = dyes.get(i);
			Item wool = woolBlocks.get(i).asItem();

			//@formatter:off
			ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, wool)
			.group("suszombification:rotten_wool")
			.requires(dye)
			.requires(Ingredient.of(woolBlocks.stream().filter(check -> !check.equals(wool)).map(ItemStack::new)))
			.unlockedBy("has_needed_dye", has(dye))
			.save(recipeOutput, "dye_" + getItemName(wool));
   			//@formatter:on
		}
	}
}
