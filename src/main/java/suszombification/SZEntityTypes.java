package suszombification;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import suszombification.entity.ZombifiedChicken;
import suszombification.renderer.ZombifiedChickenRenderer;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
public class SZEntityTypes {
	public static final EntityType<ZombifiedChicken> ZOMBIFIED_CHICKEN = EntityType.Builder.of(ZombifiedChicken::new, MobCategory.CREATURE)
			.sized(0.4F, 0.7F)
			.clientTrackingRange(10)
			.build(SuspiciousZombification.MODID + ":zombified_chicken");

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
		ZOMBIFIED_CHICKEN.setRegistryName(new ResourceLocation(SuspiciousZombification.MODID, "zombified_chicken"));
		event.getRegistry().register(ZOMBIFIED_CHICKEN);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_CHICKEN, ZombifiedChickenRenderer::new);
	}

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(SZEntityTypes.ZOMBIFIED_CHICKEN, ZombifiedChicken.createAttributes().build());
	}
}
