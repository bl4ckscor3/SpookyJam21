package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.Inverted;
import net.minecraft.loot.conditions.RandomChance;
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
		add("cat_morning_gift", CatMorningGiftModifier.serializer, new CatMorningGiftModifier(new ILootCondition[]{
				LootTableIdCondition.builder(LootTables.CAT_MORNING_GIFT).build(),
				RandomChance.randomChance(0.5F).build()
		}));
		add("no_decomposing_drops", NoDecomposingDropsModifier.serializer, new NoDecomposingDropsModifier(new ILootCondition[]{
				Inverted.invert(LootTableIdCondition.builder(SZLootTables.DEATH_BY_DECOMPOSING)).build()
		}));
	}
}
