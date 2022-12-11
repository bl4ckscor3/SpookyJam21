package suszombification;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import suszombification.registration.SZBlockEntityTypes;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZEffects;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZItems;

@Mod(SuspiciousZombification.MODID)
public class SuspiciousZombification {
	public static final String MODID = "suszombification";
	public static CreativeModeTab tab;

	public SuspiciousZombification() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		SZBlocks.BLOCKS.register(modEventBus);
		SZBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
		SZEffects.EFFECTS.register(modEventBus);
		SZEntityTypes.ENTITY_TYPES.register(modEventBus);
		SZItems.ITEMS.register(modEventBus);
	}
}
