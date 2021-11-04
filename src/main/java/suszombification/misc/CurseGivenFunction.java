package suszombification.misc;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.TileEntity;
import suszombification.RegistrationHandler;
import suszombification.block.entity.TrophyBlockEntity;

public class CurseGivenFunction extends LootFunction {
	CurseGivenFunction(ILootCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext ctx) {
		TileEntity be = ctx.getParamOrNull(LootParameters.BLOCK_ENTITY);

		if(be instanceof TrophyBlockEntity)
			stack.getOrCreateTag().putBoolean("CurseGiven", ((TrophyBlockEntity)be).isCurseGiven());

		return stack;
	}

	public static LootFunction.Builder<?> create() {
		return simpleBuilder(CurseGivenFunction::new);
	}

	@Override
	public LootFunctionType getType() {
		return RegistrationHandler.CURSE_GIVEN_LOOT_FUNCTION;
	}

	public static class Serializer extends LootFunction.Serializer<CurseGivenFunction> {
		@Override
		public void serialize(JsonObject json, CurseGivenFunction function, JsonSerializationContext ctx) {
			super.serialize(json, function, ctx);
		}

		@Override
		public CurseGivenFunction deserialize(JsonObject json, JsonDeserializationContext ctx, ILootCondition[] conditions) {
			return new CurseGivenFunction(conditions);
		}
	}
}
