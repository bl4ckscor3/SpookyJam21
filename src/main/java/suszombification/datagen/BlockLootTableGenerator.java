package suszombification.datagen;

import java.util.Set;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import suszombification.block.TrophyBlock;
import suszombification.misc.CurseGivenFunction;
import suszombification.registration.SZBlocks;

public class BlockLootTableGenerator extends BlockLootSubProvider {
	protected BlockLootTableGenerator() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	public void generate() {
		for (DeferredHolder<Block, ? extends Block> holder : SZBlocks.BLOCKS.getEntries()) {
			Block block = holder.get();

			if (block instanceof TrophyBlock)
				add(block, createTrophyLootTable(block));
			else if (block.asItem() instanceof BlockItem)
				dropSelf(block);
		}
	}

	private LootTable.Builder createTrophyLootTable(ItemLike drop) {
		//TODO: Curse Given component
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
