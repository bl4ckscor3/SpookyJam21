package suszombification;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
	public static final CreativeModeTab TAB = new CreativeModeTab(SuspiciousZombification.MODID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(SZItems.SUSPICIOUS_PUMPKIN_PIE.get());
		}
	};

	public SuspiciousZombification() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		SZBlocks.BLOCKS.register(modEventBus);
		SZBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
		SZEffects.EFFECTS.register(modEventBus);
		SZEntityTypes.ENTITY_TYPES.register(modEventBus);
		SZItems.ITEMS.register(modEventBus);
		SZLoot.LOOT_ITEM_FUNCTION_TYPES.register(modEventBus);
		SZRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
	}
}
