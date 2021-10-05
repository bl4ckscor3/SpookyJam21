package suszombification;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import suszombification.datagen.RecipeGenerator;
import suszombification.misc.SuspiciousPumpkinPieRecipe;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
public class RegistrationHandler {
	@SubscribeEvent
	public static void registerRecipeSerializer(RegistryEvent.Register<RecipeSerializer<?>> event) {
		event.getRegistry().register(new SimpleRecipeSerializer<>(SuspiciousPumpkinPieRecipe::new).setRegistryName(new ResourceLocation(SuspiciousZombification.MODID, "suspicious_pumpkin_pie_recipe")));
	}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();

		generator.addProvider(new RecipeGenerator(generator));
	}
}
