package suszombification;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.item.CandyItem;
import suszombification.item.RottenEggItem;

public class SZItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SuspiciousZombification.MODID);

	public static final RegistryObject<Item> SUSPICIOUS_PUMPKIN_PIE = ITEMS.register("suspicious_pumpkin_pie", () -> new SuspiciousStewItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.3F).alwaysEat().build()).tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> CARAMEL_CANDY = ITEMS.register("caramel_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.TAB))); //TODO: assign each candy a unique effect
	public static final RegistryObject<Item> CHAMOMILE_CANDY = ITEMS.register("chamomile_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> CHOCOLATE_CANDY = ITEMS.register("chocolate_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> CINNAMON_CANDY = ITEMS.register("cinnamon_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> PUMPKIN_CANDY = ITEMS.register("pumpkin_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> VANILLA_CANDY = ITEMS.register("vanilla_candy", () -> new CandyItem(MobEffects.GLOWING, 5, new Item.Properties().tab(SuspiciousZombification.TAB)));
	//TODO: MOAR CANDY
	public static final RegistryObject<Item> ROTTEN_EGG = ITEMS.register("rotten_egg", () -> new RottenEggItem(new Item.Properties().stacksTo(16).tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> ZOMBIFIED_CHICKEN_SPAWN_EGG = ITEMS.register("zombified_chicken_spawn_egg", () -> new ForgeSpawnEggItem(SZEntityTypes.ZOMBIFIED_CHICKEN, 0xA1A1A1, 0x799C65, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> ZOMBIFIED_COW_SPAWN_EGG = ITEMS.register("zombified_cow_spawn_egg", () -> new ForgeSpawnEggItem(SZEntityTypes.ZOMBIFIED_COW, 0x443626, 0x799C65, new Item.Properties().tab(SuspiciousZombification.TAB)));
}
