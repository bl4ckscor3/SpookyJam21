package suszombification.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import suszombification.SuspiciousZombification;
import suszombification.misc.SuspiciousPumpkinPieRecipe;

public class SZRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, SuspiciousZombification.MODID);
	public static final DeferredHolder<RecipeSerializer<?>, CustomRecipe.Serializer<SuspiciousPumpkinPieRecipe>> SUSPICIOUS_PUMPKIN_PIE = RECIPE_SERIALIZERS.register("suspicious_pumpkin_pie", () -> new CustomRecipe.Serializer<>(SuspiciousPumpkinPieRecipe::new));

	private SZRecipeSerializers() {}
}
