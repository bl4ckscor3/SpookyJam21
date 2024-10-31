package suszombification.datagen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import suszombification.entity.ZombifiedSheep;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZLoot;

public record EntityLootTableGenerator(HolderLookup.Provider lookupProvider) implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
		Map<ResourceKey<LootTable>, LootTable.Builder> lootTables = new HashMap<>();

		//@formatter:off
		//gameplay
		lootTables.put(SZLoot.DEATH_BY_DECOMPOSING, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
		//entity drops
		lootTables.put(lootTableOf(SZEntityTypes.ZOMBIFIED_CAT), LootTable.lootTable().withPool(rottenFleshDrop(2.0F)));
		lootTables.put(lootTableOf(SZEntityTypes.ZOMBIFIED_CHICKEN), LootTable.lootTable().withPool(rottenFleshDrop(1.0F))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.FEATHER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookupProvider, UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(lootTableOf(SZEntityTypes.ZOMBIFIED_COW), LootTable.lootTable().withPool(rottenFleshDrop(3.0F))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.LEATHER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookupProvider, UniformGenerator.between(0.0F, 1.0F))))));
		//@formatter:on
		lootTables.put(lootTableOf(SZEntityTypes.ZOMBIFIED_PIG), LootTable.lootTable().withPool(rottenFleshDrop(3.0F)));
		lootTables.put(lootTableOf(SZEntityTypes.ZOMBIFIED_SHEEP), LootTable.lootTable().withPool(rottenFleshDrop(3.0F)).withPool(EntityLootSubProvider.createSheepDispatchPool(SZLoot.ZOMBIFIED_SHEEP_BY_DYE)));
		lootTables.put(SZLoot.SHEAR_ZOMBIFIED_SHEEP, LootTable.lootTable().withPool(EntityLootSubProvider.createSheepDispatchPool(SZLoot.SHEAR_ZOMBIFIED_SHEEP_BY_DYE)));
		ZombifiedSheep.ITEM_BY_DYE.forEach((dye, wool) -> {
			lootTables.put(SZLoot.SHEAR_ZOMBIFIED_SHEEP_BY_DYE.get(dye), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 3.0F)).add(LootItem.lootTableItem(wool))));
			lootTables.put(SZLoot.ZOMBIFIED_SHEEP_BY_DYE.get(dye), LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(wool))));
		});
		lootTables.forEach((path, loot) -> consumer.accept(path, loot.setParamSet(LootContextParamSets.ENTITY)));
	}

	private LootPool.Builder rottenFleshDrop(float max) {
		//@formatter:off
		return LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, max)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookupProvider, UniformGenerator.between(0.0F, 1.0F))));
		//@formatter:on
	}

	protected ResourceKey<LootTable> lootTableOf(Holder<EntityType<?>> entityTypeHolder) {
		EntityType<?> entityType = entityTypeHolder.value();

		return entityType.getDefaultLootTable().orElseThrow(() -> new IllegalStateException("Entity " + entityType + " has no loot table"));
	}
}
