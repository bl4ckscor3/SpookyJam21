package suszombification.misc;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
	public static final Codec<CurseGivenFunction> CODEC = RecordCodecBuilder.create(instance -> commonFields(instance).apply(instance, CurseGivenFunction::new));

	CurseGivenFunction(List<LootItemCondition> conditions) {
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
}
