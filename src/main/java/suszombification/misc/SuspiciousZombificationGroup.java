package suszombification.misc;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import suszombification.Content;
import suszombification.SuspiciousZombification;

public class SuspiciousZombificationGroup extends CreativeModeTab {

	public SuspiciousZombificationGroup() {
		super(SuspiciousZombification.MODID);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(Content.SUSPICIOUS_PUMPKIN_PIE.get());
	}
}
