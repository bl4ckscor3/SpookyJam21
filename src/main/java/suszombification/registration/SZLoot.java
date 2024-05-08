package suszombification.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import suszombification.SuspiciousZombification;

public class SZLoot {
	//gameplay
	public static final ResourceKey<LootTable> DEATH_BY_DECOMPOSING = create("gameplay/death_by_decomposing");
	public static final ResourceKey<LootTable> ZOMBIFIED_CAT_MORNING_GIFT = create("gameplay/zombified_cat_morning_gift");
	//entities
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_BROWN = create("entities/zombified_sheep/brown");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_BLUE = create("entities/zombified_sheep/blue");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_PURPLE = create("entities/zombified_sheep/purple");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_CYAN = create("entities/zombified_sheep/cyan");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_LIGHT_GRAY = create("entities/zombified_sheep/light_gray");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_GRAY = create("entities/zombified_sheep/gray");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_PINK = create("entities/zombified_sheep/pink");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_LIME = create("entities/zombified_sheep/lime");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_YELLOW = create("entities/zombified_sheep/yellow");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_LIGHT_BLUE = create("entities/zombified_sheep/light_blue");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_MAGENTA = create("entities/zombified_sheep/magenta");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_ORANGE = create("entities/zombified_sheep/orange");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_WHITE = create("entities/zombified_sheep/white");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_GREEN = create("entities/zombified_sheep/green");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_RED = create("entities/zombified_sheep/red");
	public static final ResourceKey<LootTable> ZOMBIFIED_SHEEP_BLACK = create("entities/zombified_sheep/black");
	//zombie cove
	public static final ResourceKey<LootTable> PEN_BARREL = create("zombie_cove/pen_barrel");
	public static final ResourceKey<LootTable> RITUAL_BARREL = create("zombie_cove/ritual_barrel");
	public static final ResourceKey<LootTable> TREASURE = create("zombie_cove/treasure");

	private SZLoot() {}

	private static ResourceKey<LootTable> create(String name) {
		return ResourceKey.create(Registries.LOOT_TABLE, new ResourceLocation(SuspiciousZombification.MODID, name));
	}
}
