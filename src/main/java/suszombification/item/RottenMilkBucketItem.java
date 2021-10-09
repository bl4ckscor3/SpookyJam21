package suszombification.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;

public class RottenMilkBucketItem extends MilkBucketItem {
	public RottenMilkBucketItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600));
		entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600));

		if (entity instanceof ServerPlayer player) {
			CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
			player.awardStat(Stats.ITEM_USED.get(this));
		}

		if (entity instanceof Player player && !player.getAbilities().instabuild) {
			stack.shrink(1);
		}

		return stack.isEmpty() ? new ItemStack(Items.BUCKET) : stack;
	}
}
