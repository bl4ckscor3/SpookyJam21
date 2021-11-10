package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import suszombification.SZLootTables;
import suszombification.SuspiciousZombification;
import suszombification.glm.CatMorningGiftModifier;
import suszombification.glm.NoDecomposingDropsModifier;

public class GlobalLootModifierGenerator extends GlobalLootModifierProvider {
	public GlobalLootModifierGenerator(DataGenerator gen) {
		super(gen, SuspiciousZombification.MODID);
	}

	@Override
	protected void start() {
		add("cat_morning_gift", CatMorningGiftModifier.serializer, new CatMorningGiftModifier(new LootItemCondition[]{
				LootTableIdCondition.builder(BuiltInLootTables.CAT_MORNING_GIFT).build(),
				LootItemRandomChanceCondition.randomChance(0.5F).build()
		}));
		add("no_decomposing_drops", NoDecomposingDropsModifier.serializer, new NoDecomposingDropsModifier(new LootItemCondition[]{
				InvertedLootItemCondition.invert(LootTableIdCondition.builder(SZLootTables.DEATH_BY_DECOMPOSING)).build()
		}));
	}
}
