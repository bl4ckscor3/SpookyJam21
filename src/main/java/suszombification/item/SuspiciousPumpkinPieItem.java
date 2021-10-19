package suszombification.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import suszombification.SZEffects;
import suszombification.SZEventHandler;
import suszombification.SZItems;
import suszombification.SZTags;

public class SuspiciousPumpkinPieItem extends Item {
	private record PieEffect(Function<ItemStack, Boolean> check, Supplier<MobEffectInstance> mainEffect, Supplier<MobEffectInstance> extraEffect, ChatFormatting displayColor) {}
	private static final List<PieEffect> PIE_EFFECTS = new ArrayList<>();

	static {
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZItems.SPOILED_MILK_BUCKET.get()), () -> new MobEffectInstance(SZEffects.AMPLIFYING.get(), 1), () -> new MobEffectInstance(MobEffects.CONFUSION, 100), ChatFormatting.DARK_PURPLE));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZItems.ROTTEN_EGG.get()), () -> new MobEffectInstance(SZEffects.STENCH.get(), 2400), () -> new MobEffectInstance(MobEffects.CONFUSION, 100), ChatFormatting.DARK_PURPLE));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZTags.Items.ROTTEN_WOOL), () -> new MobEffectInstance(SZEffects.CUSHION.get(), 2400), () -> new MobEffectInstance(MobEffects.CONFUSION, 100), ChatFormatting.DARK_PURPLE));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(Items.GOLDEN_APPLE), () -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), () -> new MobEffectInstance(MobEffects.ABSORPTION, 2400), ChatFormatting.AQUA));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(Items.ROTTEN_FLESH), () -> new MobEffectInstance(MobEffects.HUNGER, 600), () -> null, ChatFormatting.AQUA));
	}

	public SuspiciousPumpkinPieItem(Properties properties) {
		super(properties);
	}

	public static void saveIngredient(ItemStack suspiciousPumpkinPie, ItemStack ingredient) {
		CompoundTag ingredientTag = new CompoundTag();

		if(ingredient.getItem() instanceof CandyItem candy) {
			MobEffect effect = candy.getEffect();

			SuspiciousStewItem.saveMobEffect(suspiciousPumpkinPie, effect, candy.getEffectDuration());
		}

		ingredient.setCount(1);
		ingredient.save(ingredientTag);
		suspiciousPumpkinPie.getOrCreateTag().put("Ingredient", ingredientTag);
	}

	public static boolean hasIngredient(ItemStack pie, Item test) {
		if(pie.hasTag() && pie.getTag().contains("Ingredient")) {
			ItemStack ingredient = ItemStack.of(pie.getTag().getCompound("Ingredient"));
			return ingredient.getItem() == test;
		}

		return false;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		CompoundTag tag = stack.getTag();

		if(tag != null && tag.contains("Effects", 9)) {
			ListTag effects = tag.getList("Effects", 10);

			for(int i = 0; i < effects.size(); ++i) {
				int duration = 160;
				CompoundTag effectTag = effects.getCompound(i);
				MobEffect effect = MobEffect.byId(effectTag.getByte("EffectId"));

				if(effectTag.contains("EffectDuration", 3))
					duration = effectTag.getInt("EffectDuration");

				if(effect != null)
					entity.addEffect(new MobEffectInstance(effect, duration));
			}
		}

		if(tag != null && tag.contains("Ingredient")) {
			ItemStack ingredient = ItemStack.of(tag.getCompound("Ingredient"));
			String itemId = ingredient.getItem().getRegistryName().getPath();
			ChatFormatting color = ChatFormatting.GOLD;
			boolean foundEffect = false;

			for(PieEffect pieEffect : PIE_EFFECTS) {
				if(pieEffect.check.apply(ingredient)) {
					MobEffectInstance mainEffect = pieEffect.mainEffect.get();
					MobEffectInstance extraEffect = pieEffect.extraEffect.get();

					if(mainEffect != null)
						entity.addEffect(mainEffect);

					if(extraEffect != null)
						entity.addEffect(extraEffect);

					if(ingredient.is(SZTags.Items.ROTTEN_WOOL))
						itemId = "rotten_wool";

					foundEffect = true;
					break;
				}
			}

			if(!foundEffect && !(ingredient.getItem() instanceof CandyItem)) { //Vanilla Mob Drop
				entity.addEffect(new MobEffectInstance(MobEffects.POISON, 300));
				itemId = "mob_drop";
				color = ChatFormatting.DARK_GREEN;
			}

			if(level.isClientSide)
				SZEventHandler.ACTIONBAR_TEXTS.put(new TranslatableComponent("message.suszombification.suspicious_pumpkin_pie." + itemId).withStyle(color), 80);
		}
		else if(level.isClientSide)
			SZEventHandler.ACTIONBAR_TEXTS.put(new TranslatableComponent("message.suszombification.suspicious_pumpkin_pie.air").withStyle(ChatFormatting.AQUA), 80);

		return super.finishUsingItem(stack, level, entity);
	}
}