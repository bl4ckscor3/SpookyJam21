package suszombification.misc;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import suszombification.block.entity.TrophyBlockEntity;
import suszombification.registration.SZLoot;

public class CurseGivenFunction extends LootItemConditionalFunction {
	CurseGivenFunction(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext ctx) {
		BlockEntity be = ctx.getParamOrNull(LootContextParams.BLOCK_ENTITY);

		if (be instanceof TrophyBlockEntity trophy)
			stack.getOrCreateTag().putBoolean("CurseGiven", trophy.isCurseGiven());

		return stack;
	}

	public static LootItemConditionalFunction.Builder<?> create() {
		return simpleBuilder(CurseGivenFunction::new);
	}

	@Override
	public LootItemFunctionType getType() {
		return SZLoot.CURSE_GIVEN_LOOT_FUNCTION.get();
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<CurseGivenFunction> {
		@Override
		public void serialize(JsonObject json, CurseGivenFunction function, JsonSerializationContext ctx) {
			super.serialize(json, function, ctx);
		}

		@Override
		public CurseGivenFunction deserialize(JsonObject json, JsonDeserializationContext ctx, LootItemCondition[] conditions) {
			return new CurseGivenFunction(conditions);
		}
	}
}
