package suszombification.glm;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import suszombification.SZDamageSources;

public class NoDecomposingDropsModifier extends LootModifier {
	public static final MapCodec<NoDecomposingDropsModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> codecStart(instance).apply(instance, NoDecomposingDropsModifier::new));

	public NoDecomposingDropsModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		var damageSource = context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);

		return damageSource != null && damageSource.is(SZDamageSources.DECOMPOSING) ? new ObjectArrayList<>() : generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}
