package suszombification;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import suszombification.registration.SZBlockEntityTypes;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZDataComponents;
import suszombification.registration.SZEffects;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZItems;
import suszombification.registration.SZRecipeSerializers;

@Mod(SuspiciousZombification.MODID)
public class SuspiciousZombification {
	public static final String MODID = "suszombification";

	public SuspiciousZombification(IEventBus modEventBus) {
		SZBlocks.BLOCKS.register(modEventBus);
		SZBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
		SZCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
		SZDataComponents.DATA_COMPONENTS.register(modEventBus);
		SZEffects.EFFECTS.register(modEventBus);
		SZEntityTypes.ENTITY_TYPES.register(modEventBus);
		SZItems.ITEMS.register(modEventBus);
		SZRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
	}

	public static ResourceLocation resLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
