package suszombification;

import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import suszombification.renderer.TrophyRenderer;
import suszombification.renderer.ZombieHorseRenderer;
import suszombification.renderer.ZombifiedChickenRenderer;
import suszombification.renderer.ZombifiedCowRenderer;
import suszombification.renderer.ZombifiedPigRenderer;
import suszombification.renderer.ZombifiedSheepRenderer;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class SZClientHandler {
	@SubscribeEvent
	public static void registerEntityRenderers(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ZOMBIFIED_CHICKEN.get(), ZombifiedChickenRenderer::new);
			RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ZOMBIFIED_COW.get(), ZombifiedCowRenderer::new);
			RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ZOMBIFIED_PIG.get(), ZombifiedPigRenderer::new);
			RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ZOMBIFIED_SHEEP.get(), ZombifiedSheepRenderer::new);
			RenderingRegistry.registerEntityRenderingHandler(SZEntityTypes.ROTTEN_EGG.get(), manager -> new SpriteRenderer<>(manager, event.getMinecraftSupplier().get().getItemRenderer()));
			RenderingRegistry.registerEntityRenderingHandler(EntityType.ZOMBIE_HORSE, ZombieHorseRenderer::new);
			ClientRegistry.bindTileEntityRenderer(SZBlockEntityTypes.TROPHY.get(), TrophyRenderer::new);
		});
	}
}
