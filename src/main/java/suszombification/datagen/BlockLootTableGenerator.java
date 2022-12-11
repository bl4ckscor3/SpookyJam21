package suszombification.datagen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import suszombification.block.TrophyBlock;
import suszombification.misc.CurseGivenFunction;
import suszombification.registration.SZBlocks;

public class BlockLootTableGenerator implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceLocation, Builder> consumer) {
		Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

		for (RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
			Block block = ro.get();

			if (block instanceof TrophyBlock)
				lootTables.put(block.getLootTable(), createTrophyLootTable(block));
			else if (block.asItem() instanceof BlockItem)
				lootTables.put(block.getLootTable(), createStandardBlockLootTable(block));
		}

		lootTables.forEach((path, loot) -> consumer.accept(path, loot.setParamSet(LootContextParamSets.BLOCK)));
	}

	private LootTable.Builder createStandardBlockLootTable(ItemLike drop) {
		//@formatter:off
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(drop))
						.when(ExplosionCondition.survivesExplosion()));
		//@formatter:on
	}

	private LootTable.Builder createTrophyLootTable(ItemLike drop) {
		//@formatter:off
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(drop)
								.apply(CurseGivenFunction.create()))
						.when(ExplosionCondition.survivesExplosion()));
		//@formatter:on
	}
}
