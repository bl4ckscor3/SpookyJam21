package suszombification;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SuspiciousZombification.MODID)
public class SuspiciousZombification {
	public static final String MODID = "suszombification";
	public static final ItemGroup TAB = new ItemGroup(SuspiciousZombification.MODID) {
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
		SZStructures.STRUCTURES.register(modEventBus);
	}
}
