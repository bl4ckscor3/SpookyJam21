package suszombification.datagen;

import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import suszombification.SuspiciousZombification;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZItems;
import suszombification.registration.SZRecipeSerializers;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		SpecialRecipeBuilder.special(SZRecipeSerializers.SUSPICIOUS_PUMPKIN_PIE.get()).save(consumer, "suspicious_pumpkin_pie");
		//@formatter:off
		ShapedRecipeBuilder.shaped(SZItems.PORKCHOP_ON_A_STICK.get())
		.pattern("R ")
		.pattern(" P")
		.define('R', Items.FISHING_ROD)
		.define('P', Items.PORKCHOP)
		.unlockedBy("has_porkchop", has(Items.PORKCHOP))
		.save(consumer);
		//@formatter:on

		addColoredWoolRecipe(consumer, Tags.Items.DYES_BLACK, SZBlocks.BLACK_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_BLUE, SZBlocks.BLUE_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_BROWN, SZBlocks.BROWN_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_CYAN, SZBlocks.CYAN_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_GRAY, SZBlocks.GRAY_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_GREEN, SZBlocks.GREEN_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_LIGHT_BLUE, SZBlocks.LIGHT_BLUE_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_LIGHT_GRAY, SZBlocks.LIGHT_GRAY_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_LIME, SZBlocks.LIME_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_MAGENTA, SZBlocks.MAGENTA_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_ORANGE, SZBlocks.ORANGE_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_PINK, SZBlocks.PINK_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_PURPLE, SZBlocks.PURPLE_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_RED, SZBlocks.RED_ROTTEN_WOOL.get());
		addColoredWoolRecipe(consumer, Tags.Items.DYES_YELLOW, SZBlocks.YELLOW_ROTTEN_WOOL.get());
	}

	protected final void addColoredWoolRecipe(Consumer<FinishedRecipe> consumer, TagKey<Item> dye, ItemLike result) {
		//@formatter:off
		ShapelessRecipeBuilder.shapeless(result)
		.group("suszombification:rotten_wool")
		.requires(dye)
		.requires(SZBlocks.WHITE_ROTTEN_WOOl.get())
		.unlockedBy("has_wool", has(SZBlocks.WHITE_ROTTEN_WOOl.get()))
		.save(consumer);
		//@formatter:on
	}

	@Override
	public String getName() {
		return "Recipes: " + SuspiciousZombification.MODID;
	}
}
