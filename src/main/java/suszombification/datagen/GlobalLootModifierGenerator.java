package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import suszombification.SuspiciousZombification;
import suszombification.misc.CatMorningGiftModifier;

public class GlobalLootModifierGenerator extends GlobalLootModifierProvider {
	public GlobalLootModifierGenerator(DataGenerator gen) {
		super(gen, SuspiciousZombification.MODID);
	}

	@Override
	protected void start() {
		add("cat_morning_gift", CatMorningGiftModifier.serializer, new CatMorningGiftModifier(new LootItemCondition[]{}));
	}
}
