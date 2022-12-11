package suszombification.datagen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import suszombification.registration.SZItems;
import suszombification.registration.SZLoot;

public class GiftLootTableGenerator implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceLocation, Builder> consumer) {
		Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

		//@formatter:off
		lootTables.put(SZLoot.ZOMBIFIED_CAT_MORNING_GIFT, LootTable.lootTable()
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
		//@formatter:on
		lootTables.forEach((path, loot) -> consumer.accept(path, loot.setParamSet(LootContextParamSets.GIFT)));
	}
}
