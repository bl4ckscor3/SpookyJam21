package suszombification.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;

public class SpoiledMilkBucketItem extends MilkBucketItem {
	public SpoiledMilkBucketItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World level, LivingEntity entity) {
		entity.addEffect(new EffectInstance(Effects.CONFUSION, 300));
		entity.addEffect(new EffectInstance(Effects.POISON, 300, 2));

		if(entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity)entity;
			CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
			player.awardStat(Stats.ITEM_USED.get(this));
		}

		if(entity instanceof PlayerEntity && !((PlayerEntity)entity).getAbilities().instabuild)
			stack.shrink(1);

		return stack.isEmpty() ? new ItemStack(Items.BUCKET) : stack;
	}
}
