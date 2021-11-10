package suszombification.glm;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ObjectHolder;
import suszombification.SZLootTables;
import suszombification.SuspiciousZombification;

public class CatMorningGiftModifier extends LootModifier {
	@ObjectHolder(SuspiciousZombification.MODID + ":cat_morning_gift")
	public static GlobalLootModifierSerializer<CatMorningGiftModifier> serializer = null;

	public CatMorningGiftModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		return Lists.newArrayList(context.getLootTable(SZLootTables.ZOMBIFIED_CAT_MORNING_GIFT).getRandomItems(context));
	}

	public static class Serializer extends GlobalLootModifierSerializer<CatMorningGiftModifier> {
		@Override
		public CatMorningGiftModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditions) {
			return new CatMorningGiftModifier(conditions);
		}

		@Override
		public JsonObject write(CatMorningGiftModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
