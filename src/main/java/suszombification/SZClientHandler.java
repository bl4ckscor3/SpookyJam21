package suszombification;

import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import suszombification.item.CustomSpawnEggItem;
import suszombification.registration.SZBlockEntityTypes;
import suszombification.registration.SZEntityTypes;
import suszombification.renderer.TrophyRenderer;
import suszombification.renderer.ZombifiedCatRenderer;
import suszombification.renderer.ZombifiedChickenRenderer;
import suszombification.renderer.ZombifiedCowRenderer;
import suszombification.renderer.ZombifiedPigRenderer;
import suszombification.renderer.ZombifiedSheepRenderer;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class SZClientHandler {
	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ZOMBIFIED_CAT.get(), ZombifiedCatRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ZOMBIFIED_CHICKEN.get(), ZombifiedChickenRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ZOMBIFIED_COW.get(), ZombifiedCowRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ZOMBIFIED_PIG.get(), ZombifiedPigRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ZOMBIFIED_SHEEP.get(), ZombifiedSheepRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ROTTEN_EGG.get(), manager -> new SpriteRenderer<>(manager, event.getMinecraftSupplier().get().getItemRenderer()));
		ClientRegistry.bindTileEntityRenderer(SZBlockEntityTypes.TROPHY.get(), TrophyRenderer::new);
	}

	@SubscribeEvent
	public static void onColorHandlerItem(ColorHandlerEvent.Item event) {
		CustomSpawnEggItem.SUS_EGGS.forEach(eggItem -> event.getItemColors().register((stack, tintIndex) -> eggItem.getColor(tintIndex), eggItem));
	}
}
