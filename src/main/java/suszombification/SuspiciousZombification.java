package suszombification;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SuspiciousZombification.MODID)
public class SuspiciousZombification {
	public static final String MODID = "suszombification";
	public static final CreativeModeTab TAB = new CreativeModeTab(SuspiciousZombification.MODID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Content.SUSPICIOUS_PUMPKIN_PIE.get());
		}
	};

	public SuspiciousZombification() {
		Content.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
