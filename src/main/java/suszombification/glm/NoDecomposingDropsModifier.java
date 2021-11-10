package suszombification.glm;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ObjectHolder;
import suszombification.SZDamageSources;
import suszombification.SuspiciousZombification;

public class NoDecomposingDropsModifier extends LootModifier {
	@ObjectHolder(SuspiciousZombification.MODID + ":no_decomposing_drops")
	public static GlobalLootModifierSerializer<NoDecomposingDropsModifier> serializer = null;

	public NoDecomposingDropsModifier(ILootCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		return context.getParamOrNull(LootParameters.DAMAGE_SOURCE) == SZDamageSources.DECOMPOSING ? new ArrayList<>() : generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<NoDecomposingDropsModifier> {
		@Override
		public NoDecomposingDropsModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditions) {
			return new NoDecomposingDropsModifier(conditions);
		}

		@Override
		public JsonObject write(NoDecomposingDropsModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
