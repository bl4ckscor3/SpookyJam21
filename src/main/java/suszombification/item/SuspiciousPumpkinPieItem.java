package suszombification.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;

public class SuspiciousPumpkinPieItem extends Item {
	public SuspiciousPumpkinPieItem(Properties properties) {
		super(properties);
	}

	public static void saveIngredient(ItemStack suspiciousPumpkinPie, ItemStack ingredient) {
		CompoundTag ingredientTag = new CompoundTag();

		if (ingredient.getItem() instanceof CandyItem candy) {
			MobEffect effect = candy.getEffect();

			SuspiciousStewItem.saveMobEffect(suspiciousPumpkinPie, effect, candy.getEffectDuration());
		}

		ingredient.setCount(1);
		ingredient.save(ingredientTag);
		suspiciousPumpkinPie.getOrCreateTag().put("Ingredient", ingredientTag);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		ItemStack newStack = super.finishUsingItem(stack, level, entity);
		CompoundTag tag = stack.getTag();

		if (tag != null && tag.contains("Effects", 9)) {
			ListTag effects = tag.getList("Effects", 10);

			for(int i = 0; i < effects.size(); ++i) {
				int duration = 160;
				CompoundTag effectTag = effects.getCompound(i);

				if (effectTag.contains("EffectDuration", 3)) {
					duration = effectTag.getInt("EffectDuration");
				}

				MobEffect effect = MobEffect.byId(effectTag.getByte("EffectId"));

				if (effect != null) {
					entity.addEffect(new MobEffectInstance(effect, duration));
				}
			}
		}
		else if (tag != null && tag.contains("Ingredient")) {
			ItemStack ingredient = ItemStack.of(tag.getCompound("Ingredient"));

			if (ingredient.is(Items.GOLDEN_APPLE)) {
				entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
				entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400));
			}
			else if (ingredient.is(Items.ROTTEN_FLESH)) {
				entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600));
			}
			else { //Vanilla Mob Drop
				entity.addEffect(new MobEffectInstance(MobEffects.POISON, 300));
			}
		}

		return newStack;
	}
}
