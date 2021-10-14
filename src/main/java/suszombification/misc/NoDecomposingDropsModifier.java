package suszombification.misc;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ObjectHolder;
import suszombification.SZDamageSources;
import suszombification.SuspiciousZombification;

public class NoDecomposingDropsModifier extends LootModifier {
	@ObjectHolder(SuspiciousZombification.MODID + ":no_decomposing_drops")
	public static GlobalLootModifierSerializer<NoDecomposingDropsModifier> serializer = null;

	public NoDecomposingDropsModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		DamageSource source = context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);

		return source == SZDamageSources.DECOMPOSING ? List.of() : generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<NoDecomposingDropsModifier> {
		@Override
		public NoDecomposingDropsModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditions) {
			return new NoDecomposingDropsModifier(conditions);
		}

		@Override
		public JsonObject write(NoDecomposingDropsModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
