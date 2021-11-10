package suszombification.datagen;

import java.util.function.Consumer;

import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import suszombification.SuspiciousZombification;
import suszombification.misc.SuspiciousPumpkinPieRecipe;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZItems;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		CustomRecipeBuilder.special(SuspiciousPumpkinPieRecipe.serializer).save(consumer, "suspicious_pumpkin_pie");
		ShapedRecipeBuilder.shaped(SZItems.PORKCHOP_ON_A_STICK.get())
		.pattern("R ")
		.pattern(" P")
		.define('R', Items.FISHING_ROD)
		.define('P', Items.PORKCHOP)
		.unlockedBy("has_porkchop", has(Items.PORKCHOP))
		.save(consumer);

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

	protected final void addColoredWoolRecipe(Consumer<IFinishedRecipe> consumer, ITag<Item> dye, IItemProvider result) {
		ShapelessRecipeBuilder.shapeless(result)
		.group("suszombification:rotten_wool")
		.requires(dye)
		.requires(SZBlocks.WHITE_ROTTEN_WOOl.get())
		.unlockedBy("has_wool", has(SZBlocks.WHITE_ROTTEN_WOOl.get()))
		.save(consumer);
	}

	@Override
	public String getName() {
		return "Recipes: " + SuspiciousZombification.MODID;
	}
}
