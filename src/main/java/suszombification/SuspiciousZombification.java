package suszombification;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import suszombification.registration.SZBlockEntityTypes;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZEffects;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZItems;
import suszombification.registration.SZLoot;
import suszombification.registration.SZRecipeSerializers;

@Mod(SuspiciousZombification.MODID)
public class SuspiciousZombification {
	public static final String MODID = "suszombification";

	public SuspiciousZombification() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		SZBlocks.BLOCKS.register(modEventBus);
		SZBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
		SZCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
		SZEffects.EFFECTS.register(modEventBus);
		SZEntityTypes.ENTITY_TYPES.register(modEventBus);
		SZItems.ITEMS.register(modEventBus);
		SZLoot.LOOT_ITEM_FUNCTION_TYPES.register(modEventBus);
		SZRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
	}
}
