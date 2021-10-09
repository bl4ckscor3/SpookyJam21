package suszombification;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.entity.ZombifiedChicken;
import suszombification.entity.ZombifiedCow;
import suszombification.renderer.ZombifiedChickenRenderer;
import suszombification.renderer.ZombifiedCowRenderer;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
public class SZEntityTypes {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SuspiciousZombification.MODID);

	public static final RegistryObject<EntityType<ZombifiedChicken>> ZOMBIFIED_CHICKEN = ENTITY_TYPES.register("zombified_chicken", () -> EntityType.Builder.of(ZombifiedChicken::new, MobCategory.CREATURE)
			.sized(0.4F, 0.7F)
			.clientTrackingRange(10)
			.build(SuspiciousZombification.MODID + ":zombified_chicken"));
	public static final RegistryObject<EntityType<ZombifiedCow>> ZOMBIFIED_COW = ENTITY_TYPES.register("zombified_cow", () -> EntityType.Builder.of(ZombifiedCow::new, MobCategory.CREATURE)
			.sized(0.9F, 1.4F)
			.clientTrackingRange(10)
			.build(SuspiciousZombification.MODID + ":zombified_cow"));

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_CHICKEN.get(), ZombifiedChickenRenderer::new);
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_COW.get(), ZombifiedCowRenderer::new);
	}

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(SZEntityTypes.ZOMBIFIED_CHICKEN.get(), ZombifiedChicken.createAttributes().build());
		event.put(SZEntityTypes.ZOMBIFIED_COW.get(), ZombifiedCow.createAttributes().build());
	}
}
