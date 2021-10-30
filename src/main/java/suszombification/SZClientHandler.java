package suszombification;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import suszombification.renderer.TrophyRenderer;
import suszombification.renderer.ZombieHorseRenderer;
import suszombification.renderer.ZombifiedChickenRenderer;
import suszombification.renderer.ZombifiedCowRenderer;
import suszombification.renderer.ZombifiedPigRenderer;
import suszombification.renderer.ZombifiedSheepRenderer;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class SZClientHandler {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_CHICKEN.get(), ZombifiedChickenRenderer::new);
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_COW.get(), ZombifiedCowRenderer::new);
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_PIG.get(), ZombifiedPigRenderer::new);
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_SHEEP.get(), ZombifiedSheepRenderer::new);
		event.registerEntityRenderer(SZEntityTypes.ROTTEN_EGG.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(EntityType.ZOMBIE_HORSE, ZombieHorseRenderer::new);
		event.registerBlockEntityRenderer(SZBlockEntityTypes.TROPHY.get(), TrophyRenderer::new);
	}
}
