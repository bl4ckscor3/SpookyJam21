package suszombification.datagen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fmllegacy.RegistryObject;
import suszombification.SZBlocks;
import suszombification.SZEntityTypes;
import suszombification.SZItems;
import suszombification.SZLootTables;
import suszombification.SuspiciousZombification;
import suszombification.block.TrophyBlock;
import suszombification.misc.CurseGivenFunction;

public class LootTableGenerator implements DataProvider {
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
		CompoundTag rottenFleshSppTag = new CompoundTag();
		CompoundTag weaknessPotionTag = new CompoundTag();

		rottenFleshSppTag.put("Ingredient", new ItemStack(Items.ROTTEN_FLESH).save(new CompoundTag()));
		weaknessPotionTag.putString("Potion", Potions.WEAKNESS.getRegistryName().toString());
		lootTables.put(SZLootTables.PEN_BARREL, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.SUGAR)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.PUMPKIN)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.EGG)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(4.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(SZItems.SUSPICIOUS_PUMPKIN_PIE.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(SetNbtFunction.setTag(rottenFleshSppTag)))));
		lootTables.put(SZLootTables.RITUAL_BARREL, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.SUGAR)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.PUMPKIN)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.EGG)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.GOLDEN_APPLE)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.POTION)
								.apply(SetNbtFunction.setTag(weaknessPotionTag))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(4.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
		lootTables.put(SZLootTables.TREASURE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.IRON_INGOT)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(3.0F))
						.add(LootItem.lootTableItem(Items.CARROT)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F))))
						.add(LootItem.lootTableItem(Items.POTATO)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(SZBlocks.CARROT_TROPHY.get()).setWeight(40))
						.add(LootItem.lootTableItem(SZBlocks.POTATO_TROPHY.get()).setWeight(40))
						.add(LootItem.lootTableItem(SZBlocks.IRON_INGOT_TROPHY.get()).setWeight(20)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.when(LootItemRandomChanceCondition.randomChance(0.1F))
						.add(LootItem.lootTableItem(Items.IRON_SWORD)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_AXE)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_HOE)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_SHOVEL)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_PICKAXE)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_BOOTS)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_CHESTPLATE)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_HELMET)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_LEGGINGS)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()
										.when(LootItemRandomChanceCondition.randomChance(0.25F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.when(LootItemRandomChanceCondition.randomChance(0.05F))
						.add(LootItem.lootTableItem(SZItems.CARAMEL_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.CHOCOLATE_CREAM_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.CINNAMON_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.HONEY_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.MELON_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.PEPPERMINT_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.PUMPKIN_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.VANILLA_CREAM_CANDY.get()))));
		return lootTables;
	}

	private Map<ResourceLocation, LootTable.Builder> generateEntityLootTables() {
		Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

		//gameplay
		lootTables.put(SZLootTables.DEATH_BY_DECOMPOSING, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
		//entity drops
		lootTables.put(SZEntityTypes.ZOMBIFIED_CAT.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.STRING)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_CHICKEN.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.FEATHER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_COW.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.LEATHER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_PIG.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_SHEEP.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
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
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(SZItems.CARAMEL_CANDY.get()).setWeight(10))
						.add(LootItem.lootTableItem(SZItems.CHOCOLATE_CREAM_CANDY.get()).setWeight(10))
						.add(LootItem.lootTableItem(SZItems.CINNAMON_CANDY.get()).setWeight(10))
						.add(LootItem.lootTableItem(SZItems.HONEY_CANDY.get()).setWeight(10))
						.add(LootItem.lootTableItem(SZItems.MELON_CANDY.get()).setWeight(10))
						.add(LootItem.lootTableItem(SZItems.PEPPERMINT_CANDY.get()).setWeight(10))
						.add(LootItem.lootTableItem(SZItems.PUMPKIN_CANDY.get()).setWeight(10))
						.add(LootItem.lootTableItem(SZItems.VANILLA_CREAM_CANDY.get()).setWeight(10))));
		return lootTables;
	}

	private LootTable.Builder createSheepTable(ItemLike wool) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(wool)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootTableReference.lootTableReference(SZEntityTypes.ZOMBIFIED_SHEEP.get().getDefaultLootTable())));
	}

	private final LootTable.Builder createStandardBlockLootTable(ItemLike drop)
	{
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(drop))
						.when(ExplosionCondition.survivesExplosion()));
	}

	private final LootTable.Builder createTrophyLootTable(ItemLike drop) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(drop)
								.apply(CurseGivenFunction.create()))
						.when(ExplosionCondition.survivesExplosion()));
	}

	@Override
	public void run(HashCache cache) {
		Map<ResourceLocation, LootTable> tables = new HashMap<>();

		generateBlockLootTables().forEach((path, loot) -> tables.put(path, loot.setParamSet(LootContextParamSets.BLOCK).build()));
		generateChestLootTables().forEach((path, loot) -> tables.put(path, loot.setParamSet(LootContextParamSets.CHEST).build()));
		generateEntityLootTables().forEach((path, loot) -> tables.put(path, loot.setParamSet(LootContextParamSets.ENTITY).build()));
		generateGiftLootTable().forEach((path, loot) -> tables.put(path, loot.setParamSet(LootContextParamSets.GIFT).build()));
		tables.forEach((key, lootTable) -> {
			try {
				DataProvider.save(GSON, cache, LootTables.serialize(lootTable), generator.getOutputFolder().resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json"));
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
