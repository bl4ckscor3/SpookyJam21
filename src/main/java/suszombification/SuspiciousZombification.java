package suszombification;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import suszombification.misc.SuspiciousZombificationGroup;

@Mod(SuspiciousZombification.MODID)
public class SuspiciousZombification {
	public static final String MODID = "suszombification";
	public static CreativeModeTab suspiciousZombificationGroup = new SuspiciousZombificationGroup();

	public SuspiciousZombification() {
		Content.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
