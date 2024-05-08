package suszombification.datagen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZLoot;

public class EntityLootTableGenerator implements LootTableSubProvider {
	@Override
	public void generate(HolderLookup.Provider lookupProvider, BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
		Map<ResourceKey<LootTable>, LootTable.Builder> lootTables = new HashMap<>();

		//@formatter:off
		//gameplay
		lootTables.put(SZLoot.DEATH_BY_DECOMPOSING, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
		//entity drops
		lootTables.put(SZEntityTypes.ZOMBIFIED_CAT.get().getDefaultLootTable(), LootTable.lootTable().withPool(rottenFleshDrop(2.0F)));
		lootTables.put(SZEntityTypes.ZOMBIFIED_CHICKEN.get().getDefaultLootTable(), LootTable.lootTable().withPool(rottenFleshDrop(1.0F))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.FEATHER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_COW.get().getDefaultLootTable(), LootTable.lootTable().withPool(rottenFleshDrop(3.0F))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.LEATHER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
		//@formatter:on
		lootTables.put(SZEntityTypes.ZOMBIFIED_PIG.get().getDefaultLootTable(), LootTable.lootTable().withPool(rottenFleshDrop(3.0F)));
		lootTables.put(SZEntityTypes.ZOMBIFIED_SHEEP.get().getDefaultLootTable(), LootTable.lootTable().withPool(rottenFleshDrop(3.0F)));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_BLACK, createSheepTable(SZBlocks.BLACK_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_BLUE, createSheepTable(SZBlocks.BLUE_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_BROWN, createSheepTable(SZBlocks.BROWN_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_CYAN, createSheepTable(SZBlocks.CYAN_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_GRAY, createSheepTable(SZBlocks.GRAY_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_GREEN, createSheepTable(SZBlocks.GREEN_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_LIGHT_BLUE, createSheepTable(SZBlocks.LIGHT_BLUE_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_LIGHT_GRAY, createSheepTable(SZBlocks.LIGHT_GRAY_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_LIME, createSheepTable(SZBlocks.LIME_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_MAGENTA, createSheepTable(SZBlocks.MAGENTA_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_ORANGE, createSheepTable(SZBlocks.ORANGE_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_PINK, createSheepTable(SZBlocks.PINK_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_PURPLE, createSheepTable(SZBlocks.PURPLE_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_RED, createSheepTable(SZBlocks.RED_ROTTEN_WOOL.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_WHITE, createSheepTable(SZBlocks.WHITE_ROTTEN_WOOl.get()));
		lootTables.put(SZLoot.ZOMBIFIED_SHEEP_YELLOW, createSheepTable(SZBlocks.YELLOW_ROTTEN_WOOL.get()));

		lootTables.forEach((path, loot) -> consumer.accept(path, loot.setParamSet(LootContextParamSets.ENTITY)));
	}

	private LootTable.Builder createSheepTable(ItemLike wool) {
		//@formatter:off
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(wool)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(NestedLootTable.lootTableReference(SZEntityTypes.ZOMBIFIED_SHEEP.get().getDefaultLootTable())));
		//@formatter:on
	}

	private LootPool.Builder rottenFleshDrop(float max) {
		//@formatter:off
		return LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, max)))
						.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))));
		//@formatter:on
	}
}
