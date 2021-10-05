package suszombification;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.items.CandyItem;
import suszombification.misc.SuspiciousFoods;

public class Content {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SuspiciousZombification.MODID);

	//items
	public static final RegistryObject<Item> SUSPICIOUS_PUMPKIN_PIE = ITEMS.register("suspicious_pumpkin_pie", () -> new SuspiciousStewItem(new Item.Properties().food(SuspiciousFoods.SUSPICIOUS_PUMPKIN_PIE).tab(SuspiciousZombification.suspiciousZombificationGroup)));
	public static final RegistryObject<Item> CARAMEL_CANDY = ITEMS.register("caramel_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.suspiciousZombificationGroup))); //TODO: assign each candy a unique effect
	public static final RegistryObject<Item> CHAMOMILE_CANDY = ITEMS.register("chamomile_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.suspiciousZombificationGroup)));
	public static final RegistryObject<Item> CHOCOLATE_CANDY = ITEMS.register("chocolate_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.suspiciousZombificationGroup)));
	public static final RegistryObject<Item> CINNAMON_CANDY = ITEMS.register("cinnamon_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.suspiciousZombificationGroup)));
	public static final RegistryObject<Item> PUMPKIN_CANDY = ITEMS.register("pumpkin_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.suspiciousZombificationGroup)));
	public static final RegistryObject<Item> VANILLA_CANDY = ITEMS.register("vanilla_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.suspiciousZombificationGroup)));
	//TODO: MOAR CANDY
}
