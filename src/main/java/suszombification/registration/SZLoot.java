package suszombification.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import suszombification.SuspiciousZombification;
import suszombification.misc.CurseGivenFunction;

public class SZLoot {
	public static final DeferredRegister<LootItemFunctionType> LOOT_ITEM_FUNCTION_TYPES = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, SuspiciousZombification.MODID);
	public static final RegistryObject<LootItemFunctionType> CURSE_GIVEN_LOOT_FUNCTION = LOOT_ITEM_FUNCTION_TYPES.register("curse_given", () -> new LootItemFunctionType(new CurseGivenFunction.Serializer()));
	//gameplay
	public static final ResourceLocation DEATH_BY_DECOMPOSING = new ResourceLocation(SuspiciousZombification.MODID, "gameplay/death_by_decomposing");
	public static final ResourceLocation ZOMBIFIED_CAT_MORNING_GIFT = new ResourceLocation(SuspiciousZombification.MODID, "gameplay/zombified_cat_morning_gift");
	//entities
	public static final ResourceLocation ZOMBIFIED_SHEEP_BROWN = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/brown");
	public static final ResourceLocation ZOMBIFIED_SHEEP_BLUE = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/blue");
	public static final ResourceLocation ZOMBIFIED_SHEEP_PURPLE = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/purple");
	public static final ResourceLocation ZOMBIFIED_SHEEP_CYAN = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/cyan");
	public static final ResourceLocation ZOMBIFIED_SHEEP_LIGHT_GRAY = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/light_gray");
	public static final ResourceLocation ZOMBIFIED_SHEEP_GRAY = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/gray");
	public static final ResourceLocation ZOMBIFIED_SHEEP_PINK = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/pink");
	public static final ResourceLocation ZOMBIFIED_SHEEP_LIME = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/lime");
	public static final ResourceLocation ZOMBIFIED_SHEEP_YELLOW = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/yellow");
	public static final ResourceLocation ZOMBIFIED_SHEEP_LIGHT_BLUE = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/light_blue");
	public static final ResourceLocation ZOMBIFIED_SHEEP_MAGENTA = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/magenta");
	public static final ResourceLocation ZOMBIFIED_SHEEP_ORANGE = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/orange");
	public static final ResourceLocation ZOMBIFIED_SHEEP_WHITE = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/white");
	public static final ResourceLocation ZOMBIFIED_SHEEP_GREEN = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/green");
	public static final ResourceLocation ZOMBIFIED_SHEEP_RED = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/red");
	public static final ResourceLocation ZOMBIFIED_SHEEP_BLACK = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/black");
	//zombie cove
	public static final ResourceLocation PEN_BARREL = new ResourceLocation(SuspiciousZombification.MODID, "zombie_cove/pen_barrel");
	public static final ResourceLocation RITUAL_BARREL = new ResourceLocation(SuspiciousZombification.MODID, "zombie_cove/ritual_barrel");
	public static final ResourceLocation TREASURE = new ResourceLocation(SuspiciousZombification.MODID, "zombie_cove/treasure");
}
