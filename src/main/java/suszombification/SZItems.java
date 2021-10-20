package suszombification;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.item.CandyItem;
import suszombification.item.PorkchopOnAStickItem;
import suszombification.item.RottenEggItem;
import suszombification.item.SpoiledMilkBucketItem;
import suszombification.item.SuspiciousPumpkinPieItem;

public class SZItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SuspiciousZombification.MODID);

	public static final RegistryObject<Item> SUSPICIOUS_PUMPKIN_PIE = ITEMS.register("suspicious_pumpkin_pie", () -> new SuspiciousPumpkinPieItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.3F).alwaysEat().build()).tab(SuspiciousZombification.TAB)));
	//candies
	public static final RegistryObject<Item> CARAMEL_CANDY = ITEMS.register("caramel_candy", () -> new CandyItem(MobEffects.SLOW_FALLING, 20, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> CHOCOLATE_CREAM_CANDY = ITEMS.register("chocolate_cream_candy", () -> new CandyItem(MobEffects.DIG_SLOWDOWN, 20, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> CINNAMON_CANDY = ITEMS.register("cinnamon_candy", () -> new CandyItem(MobEffects.GLOWING, 20, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> HONEY_CANDY = ITEMS.register("honey_candy", () -> new CandyItem(MobEffects.DAMAGE_BOOST, 20, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> MELON_CANDY = ITEMS.register("melon_candy", () -> new CandyItem(MobEffects.WATER_BREATHING, 20, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> PEPPERMINT_CANDY = ITEMS.register("peppermint_candy", () -> new CandyItem(MobEffects.LEVITATION, 20, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> PUMPKIN_CANDY = ITEMS.register("pumpkin_candy", () -> new CandyItem(MobEffects.INVISIBILITY, 20, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> VANILLA_CREAM_CANDY = ITEMS.register("vanilla_cream_candy", () -> new CandyItem(MobEffects.DIG_SPEED, 20, new Item.Properties().tab(SuspiciousZombification.TAB)));
	//TODO: maybe more candy flavours?
	//other items
	public static final RegistryObject<Item> SPOILED_MILK_BUCKET = ITEMS.register("spoiled_milk_bucket", () -> new SpoiledMilkBucketItem(new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET).tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> ROTTEN_EGG = ITEMS.register("rotten_egg", () -> new RottenEggItem(new Item.Properties().stacksTo(16).tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> PORKCHOP_ON_A_STICK = ITEMS.register("porkchop_on_a_stick", () -> new PorkchopOnAStickItem(new Item.Properties().durability(50).tab(SuspiciousZombification.TAB)));
	//spawn eggs
	public static final RegistryObject<Item> ZOMBIFIED_CHICKEN_SPAWN_EGG = ITEMS.register("zombified_chicken_spawn_egg", () -> new ForgeSpawnEggItem(SZEntityTypes.ZOMBIFIED_CHICKEN, 0xA1A1A1, 0x799C65, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> ZOMBIFIED_COW_SPAWN_EGG = ITEMS.register("zombified_cow_spawn_egg", () -> new ForgeSpawnEggItem(SZEntityTypes.ZOMBIFIED_COW, 0x443626, 0x799C65, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> ZOMBIFIED_PIG_SPAWN_EGG = ITEMS.register("zombified_pig_spawn_egg", () -> new ForgeSpawnEggItem(SZEntityTypes.ZOMBIFIED_PIG, 0xF0A5A2, 0x799C65, new Item.Properties().tab(SuspiciousZombification.TAB)));
	public static final RegistryObject<Item> ZOMBIFIED_SHEEP_SPAWN_EGG = ITEMS.register("zombified_sheep_spawn_egg", () -> new ForgeSpawnEggItem(SZEntityTypes.ZOMBIFIED_SHEEP, 0xE7E7E7, 0x799C65, new Item.Properties().tab(SuspiciousZombification.TAB)));
}
