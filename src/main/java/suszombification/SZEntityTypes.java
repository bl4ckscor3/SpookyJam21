package suszombification;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.entity.ThrownRottenEgg;
import suszombification.entity.ZombifiedChicken;
import suszombification.entity.ZombifiedCow;
import suszombification.entity.ZombifiedPig;
import suszombification.entity.ZombifiedSheep;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
public class SZEntityTypes {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SuspiciousZombification.MODID);

	public static final RegistryObject<EntityType<ZombifiedChicken>> ZOMBIFIED_CHICKEN = ENTITY_TYPES.register("zombified_chicken", () -> EntityType.Builder.of(ZombifiedChicken::new, EntityClassification.CREATURE)
			.sized(0.4F, 0.7F)
			.clientTrackingRange(10)
			.build(SuspiciousZombification.MODID + ":zombified_chicken"));
	public static final RegistryObject<EntityType<ZombifiedCow>> ZOMBIFIED_COW = ENTITY_TYPES.register("zombified_cow", () -> EntityType.Builder.of(ZombifiedCow::new, EntityClassification.CREATURE)
			.sized(0.9F, 1.4F)
			.clientTrackingRange(10)
			.build(SuspiciousZombification.MODID + ":zombified_cow"));
	public static final RegistryObject<EntityType<ZombifiedPig>> ZOMBIFIED_PIG = ENTITY_TYPES.register("zombified_pig", () -> EntityType.Builder.of(ZombifiedPig::new, EntityClassification.CREATURE)
			.sized(0.9F, 0.9F)
			.clientTrackingRange(10)
			.build(SuspiciousZombification.MODID + ":zombified_pig"));
	public static final RegistryObject<EntityType<ZombifiedSheep>> ZOMBIFIED_SHEEP = ENTITY_TYPES.register("zombified_sheep", () -> EntityType.Builder.of(ZombifiedSheep::new, EntityClassification.CREATURE)
			.sized(0.9F, 1.3F)
			.clientTrackingRange(10)
			.build(SuspiciousZombification.MODID + ":zombified_sheep"));
	public static final RegistryObject<EntityType<ThrownRottenEgg>> ROTTEN_EGG = ENTITY_TYPES.register("rotten_egg", () -> EntityType.Builder.<ThrownRottenEgg>of(ThrownRottenEgg::new, EntityClassification.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(SuspiciousZombification.MODID + ":rotten_egg"));

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(SZEntityTypes.ZOMBIFIED_CHICKEN.get(), ZombifiedChicken.createAttributes().build());
		event.put(SZEntityTypes.ZOMBIFIED_COW.get(), ZombifiedCow.createAttributes().build());
		event.put(SZEntityTypes.ZOMBIFIED_PIG.get(), ZombifiedPig.createAttributes().build());
		event.put(SZEntityTypes.ZOMBIFIED_SHEEP.get(), ZombifiedSheep.createAttributes().build());
	}
}
