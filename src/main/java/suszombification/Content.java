package suszombification;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.misc.SuspiciousFoods;

public class Content {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SuspiciousZombification.MODID);

	//items
	public static final RegistryObject<Item> SUSPICIOUS_PUMPKIN_PIE = ITEMS.register("suspicious_pumpkin_pie", () -> new SuspiciousStewItem(new Item.Properties().food(SuspiciousFoods.SUSPICIOUS_PUMPKIN_PIE).tab(SuspiciousZombification.suspiciousZombificationGroup)));
}
