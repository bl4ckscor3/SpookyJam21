package suszombification.registration;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;
import suszombification.SuspiciousZombification;
import suszombification.misc.SuspiciousPumpkinPieRecipe;

public class SZRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Keys.RECIPE_SERIALIZERS, SuspiciousZombification.MODID);
	public static final RegistryObject<SimpleCraftingRecipeSerializer<SuspiciousPumpkinPieRecipe>> SUSPICIOUS_PUMPKIN_PIE = RECIPE_SERIALIZERS.register("suspicious_pumpkin_pie", () -> new SimpleCraftingRecipeSerializer<>(SuspiciousPumpkinPieRecipe::new));
}
