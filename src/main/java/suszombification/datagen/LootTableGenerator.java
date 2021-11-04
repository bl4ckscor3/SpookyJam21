package suszombification.datagen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.EnchantRandomly;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potions;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import suszombification.SZBlocks;
import suszombification.SZEntityTypes;
import suszombification.SZItems;
import suszombification.SZLootTables;
import suszombification.SuspiciousZombification;
import suszombification.block.TrophyBlock;
import suszombification.misc.CurseGivenFunction;

public class LootTableGenerator implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private final DataGenerator generator;

	public LootTableGenerator(DataGenerator generator) {
		this.generator = generator;
	}

	private Map<ResourceLocation, LootTable.Builder> generateBlockLootTables() {
		Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

		for(RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
			Block block = ro.get();

			if(block instanceof TrophyBlock)
				lootTables.put(block.getLootTable(), createTrophyLootTable(block));
			else if(block.asItem() instanceof BlockItem)
				lootTables.put(block.getLootTable(), createStandardBlockLootTable(block));
		}

		return lootTables;
	}

	private Map<ResourceLocation, LootTable.Builder> generateChestLootTables() {
		Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();
		CompoundNBT rottenFleshSppTag = new CompoundNBT();
		CompoundNBT weaknessPotionTag = new CompoundNBT();

		rottenFleshSppTag.put("Ingredient", new ItemStack(Items.ROTTEN_FLESH).save(new CompoundNBT()));
		weaknessPotionTag.putString("Potion", Potions.WEAKNESS.getRegistryName().toString());
		lootTables.put(SZLootTables.PEN_BARREL, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.SUGAR)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.PUMPKIN)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.EGG)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(4))
						.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetCount.setCount(RandomValueRange.between(2.0F, 4.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(SZItems.SUSPICIOUS_PUMPKIN_PIE.get())
								.apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F)))
								.apply(SetNBT.setTag(rottenFleshSppTag)))));
		lootTables.put(SZLootTables.RITUAL_BARREL, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.SUGAR)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.PUMPKIN)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.EGG)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.GOLDEN_APPLE)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.POTION)
								.apply(SetNBT.setTag(weaknessPotionTag))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(4))
						.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F))))));
		lootTables.put(SZLootTables.TREASURE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.IRON_INGOT)
								.apply(SetCount.setCount(RandomValueRange.between(0.0F, 3.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(3))
						.add(ItemLootEntry.lootTableItem(Items.CARROT)
								.apply(SetCount.setCount(RandomValueRange.between(0.0F, 3.0F))))
						.add(ItemLootEntry.lootTableItem(Items.POTATO)
								.apply(SetCount.setCount(RandomValueRange.between(0.0F, 3.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(SZBlocks.CARROT_TROPHY.get()).setWeight(40))
						.add(ItemLootEntry.lootTableItem(SZBlocks.POTATO_TROPHY.get()).setWeight(40))
						.add(ItemLootEntry.lootTableItem(SZBlocks.IRON_INGOT_TROPHY.get()).setWeight(20)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.when(RandomChance.randomChance(0.1F))
						.add(ItemLootEntry.lootTableItem(Items.IRON_SWORD)
								.apply(EnchantRandomly.randomApplicableEnchantment()
										.when(RandomChance.randomChance(0.25F))))
						.add(ItemLootEntry.lootTableItem(Items.IRON_AXE)
								.apply(EnchantRandomly.randomApplicableEnchantment()
										.when(RandomChance.randomChance(0.25F))))
						.add(ItemLootEntry.lootTableItem(Items.IRON_HOE)
								.apply(EnchantRandomly.randomApplicableEnchantment()
										.when(RandomChance.randomChance(0.25F))))
						.add(ItemLootEntry.lootTableItem(Items.IRON_SHOVEL)
								.apply(EnchantRandomly.randomApplicableEnchantment()
										.when(RandomChance.randomChance(0.25F))))
						.add(ItemLootEntry.lootTableItem(Items.IRON_PICKAXE)
								.apply(EnchantRandomly.randomApplicableEnchantment()
										.when(RandomChance.randomChance(0.25F))))
						.add(ItemLootEntry.lootTableItem(Items.IRON_BOOTS)
								.apply(EnchantRandomly.randomApplicableEnchantment()
										.when(RandomChance.randomChance(0.25F))))
						.add(ItemLootEntry.lootTableItem(Items.IRON_CHESTPLATE)
								.apply(EnchantRandomly.randomApplicableEnchantment()
										.when(RandomChance.randomChance(0.25F))))
						.add(ItemLootEntry.lootTableItem(Items.IRON_HELMET)
								.apply(EnchantRandomly.randomApplicableEnchantment()
										.when(RandomChance.randomChance(0.25F))))
						.add(ItemLootEntry.lootTableItem(Items.IRON_LEGGINGS)
								.apply(EnchantRandomly.randomApplicableEnchantment()
										.when(RandomChance.randomChance(0.25F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.when(RandomChance.randomChance(0.05F))
						.add(ItemLootEntry.lootTableItem(SZItems.CARAMEL_CANDY.get()))
						.add(ItemLootEntry.lootTableItem(SZItems.CHOCOLATE_CREAM_CANDY.get()))
						.add(ItemLootEntry.lootTableItem(SZItems.CINNAMON_CANDY.get()))
						.add(ItemLootEntry.lootTableItem(SZItems.HONEY_CANDY.get()))
						.add(ItemLootEntry.lootTableItem(SZItems.MELON_CANDY.get()))
						.add(ItemLootEntry.lootTableItem(SZItems.PEPPERMINT_CANDY.get()))
						.add(ItemLootEntry.lootTableItem(SZItems.PUMPKIN_CANDY.get()))
						.add(ItemLootEntry.lootTableItem(SZItems.VANILLA_CREAM_CANDY.get()))));
		return lootTables;
	}

	private Map<ResourceLocation, LootTable.Builder> generateEntityLootTables() {
		Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

		//gameplay
		lootTables.put(SZLootTables.DEATH_BY_DECOMPOSING, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F))))));
		//entity drops

		lootTables.put(SZEntityTypes.ZOMBIFIED_CHICKEN.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.FEATHER)
								.apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F)))
								.apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH)
								.apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_COW.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.LEATHER)
								.apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F)))
								.apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F)))
								.apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_PIG.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F)))
								.apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_SHEEP.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetCount.setCount(RandomValueRange.between(1.0F, 2.0F)))
								.apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F))))));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_BLACK, createSheepTable(SZBlocks.BLACK_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_BLUE, createSheepTable(SZBlocks.BLUE_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_BROWN, createSheepTable(SZBlocks.BROWN_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_CYAN, createSheepTable(SZBlocks.CYAN_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_GRAY, createSheepTable(SZBlocks.GRAY_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_GREEN, createSheepTable(SZBlocks.GREEN_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_LIGHT_BLUE, createSheepTable(SZBlocks.LIGHT_BLUE_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_LIGHT_GRAY, createSheepTable(SZBlocks.LIGHT_GRAY_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_LIME, createSheepTable(SZBlocks.LIME_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_MAGENTA, createSheepTable(SZBlocks.MAGENTA_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_ORANGE, createSheepTable(SZBlocks.ORANGE_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_PINK, createSheepTable(SZBlocks.PINK_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_PURPLE, createSheepTable(SZBlocks.PURPLE_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_RED, createSheepTable(SZBlocks.RED_ROTTEN_WOOL.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_WHITE, createSheepTable(SZBlocks.WHITE_ROTTEN_WOOl.get()));
		lootTables.put(SZLootTables.ZOMBIFIED_SHEEP_YELLOW, createSheepTable(SZBlocks.YELLOW_ROTTEN_WOOL.get()));
		return lootTables;
	}

	private Map<ResourceLocation, LootTable.Builder> generateGiftLootTable() {
		Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

		lootTables.put(SZLootTables.ZOMBIFIED_CAT_MORNING_GIFT, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(SZItems.CARAMEL_CANDY.get()).setWeight(10))
						.add(ItemLootEntry.lootTableItem(SZItems.CHOCOLATE_CREAM_CANDY.get()).setWeight(10))
						.add(ItemLootEntry.lootTableItem(SZItems.CINNAMON_CANDY.get()).setWeight(10))
						.add(ItemLootEntry.lootTableItem(SZItems.HONEY_CANDY.get()).setWeight(10))
						.add(ItemLootEntry.lootTableItem(SZItems.MELON_CANDY.get()).setWeight(10))
						.add(ItemLootEntry.lootTableItem(SZItems.PEPPERMINT_CANDY.get()).setWeight(10))
						.add(ItemLootEntry.lootTableItem(SZItems.PUMPKIN_CANDY.get()).setWeight(10))
						.add(ItemLootEntry.lootTableItem(SZItems.VANILLA_CREAM_CANDY.get()).setWeight(10))));
		return lootTables;
	}

	private LootTable.Builder createSheepTable(IItemProvider wool) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(wool)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(TableLootEntry.lootTableReference(SZEntityTypes.ZOMBIFIED_SHEEP.get().getDefaultLootTable())));
	}

	private final LootTable.Builder createStandardBlockLootTable(IItemProvider drop)
	{
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(drop))
						.when(SurvivesExplosion.survivesExplosion()));
	}

	private final LootTable.Builder createTrophyLootTable(IItemProvider drop) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(drop)
								.apply(CurseGivenFunction.create()))
						.when(SurvivesExplosion.survivesExplosion()));
	}

	@Override
	public void run(DirectoryCache cache) {
		Map<ResourceLocation, LootTable> tables = new HashMap<>();

		generateBlockLootTables().forEach((path, loot) -> tables.put(path, loot.setParamSet(LootParameterSets.BLOCK).build()));
		generateChestLootTables().forEach((path, loot) -> tables.put(path, loot.setParamSet(LootParameterSets.CHEST).build()));
		generateEntityLootTables().forEach((path, loot) -> tables.put(path, loot.setParamSet(LootParameterSets.ENTITY).build()));
		generateGiftLootTable().forEach((path, loot) -> tables.put(path, loot.setParamSet(LootParameterSets.GIFT).build()));
		tables.forEach((key, lootTable) -> {
			try {
				IDataProvider.save(GSON, cache, LootTableManager.serialize(lootTable), generator.getOutputFolder().resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json"));
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public String getName() {
		return "Loot Tables: " + SuspiciousZombification.MODID;
	}
}
