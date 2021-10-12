package suszombification;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import suszombification.misc.CatMorningGiftModifier;
import suszombification.misc.SuspiciousPumpkinPieRecipe;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
public class RegistrationHandler {
	@SubscribeEvent
	public static void registerRecipeSerializer(RegistryEvent.Register<RecipeSerializer<?>> event) {
		event.getRegistry().register(new SimpleRecipeSerializer<>(SuspiciousPumpkinPieRecipe::new).setRegistryName(new ResourceLocation(SuspiciousZombification.MODID, "suspicious_pumpkin_pie")));
	}

	@SubscribeEvent
	public static void registerGlobalLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
		event.getRegistry().register(new CatMorningGiftModifier.Serializer().setRegistryName(new ResourceLocation(SuspiciousZombification.MODID, "cat_morning_gift")));
	}
}
