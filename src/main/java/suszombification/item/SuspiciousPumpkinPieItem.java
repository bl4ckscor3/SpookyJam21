package suszombification.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

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
	private static final List<Function<ItemStack, MobEffectInstance>> CUSTOM_EFFECTS = new ArrayList<>();
	private static final List<Function<ItemStack, String>> CUSTOM_TRANSLATION_KEYS = new ArrayList<>();
	private static final Map<Item, Consumer<LivingEntity>> MISC_ITEMS = new HashMap<>();

	static {
		CUSTOM_EFFECTS.add(stack -> stack.is(SZItems.SPOILED_MILK_BUCKET.get()) ? new MobEffectInstance(SZEffects.AMPLIFYING.get(), 1) : null);
		CUSTOM_EFFECTS.add(stack -> stack.is(SZItems.ROTTEN_EGG.get()) ? new MobEffectInstance(SZEffects.STENCH.get(), 2400) : null);
		CUSTOM_EFFECTS.add(stack -> stack.is(SZTags.Items.ROTTEN_WOOL) ? new MobEffectInstance(SZEffects.CUSHION.get(), 2400) : null);

		CUSTOM_TRANSLATION_KEYS.add(stack -> stack.is(SZTags.Items.ROTTEN_WOOL) ? "rotten_wool" : "");

		MISC_ITEMS.put(Items.GOLDEN_APPLE, entity -> {
			entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
			entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400));
		});
		MISC_ITEMS.put(Items.ROTTEN_FLESH, entity -> entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600)));
	}

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

	public static boolean hasIngredient(ItemStack pie, Item test) {
		if (pie.hasTag() && pie.getTag().contains("Ingredient")) {
			ItemStack ingredient = ItemStack.of(pie.getTag().getCompound("Ingredient"));
			return ingredient.getItem() == test;
		}

		return false;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
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

		if (tag != null && tag.contains("Ingredient")) {
			ItemStack ingredient = ItemStack.of(tag.getCompound("Ingredient"));
			String itemId = ingredient.getItem().getRegistryName().getPath();
			ChatFormatting color = ChatFormatting.GOLD;

			if (CUSTOM_EFFECTS.stream().anyMatch(f -> f.apply(ingredient) != null)) {
				MobEffectInstance effect = CUSTOM_EFFECTS.stream().map(f -> f.apply(ingredient)).filter(Objects::nonNull).findFirst().orElse(null);

				if (effect != null) {
					entity.addEffect(effect);
					entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100));
					color = ChatFormatting.DARK_PURPLE;
				}
			}
			else if (MISC_ITEMS.containsKey(ingredient.getItem())) {
				MISC_ITEMS.get(ingredient.getItem()).accept(entity);
				color = ChatFormatting.AQUA;
			}
			else if (!(ingredient.getItem() instanceof CandyItem)){ //Vanilla Mob Drop
				entity.addEffect(new MobEffectInstance(MobEffects.POISON, 300));
				itemId = "mob_drop";
				color = ChatFormatting.DARK_GREEN;
			}

			if (CUSTOM_TRANSLATION_KEYS.stream().anyMatch(f -> !f.apply(ingredient).isEmpty())) {
				itemId = CUSTOM_TRANSLATION_KEYS.stream().map(f -> f.apply(ingredient)).filter(s -> !s.isEmpty()).findFirst().orElse("mob_drop");
			}

			if (level.isClientSide) {
				SZEventHandler.ACTIONBAR_TEXTS.put(new TranslatableComponent("message.suszombification.suspicious_pumpkin_pie." + itemId).withStyle(color), 80);
			}
		}
		else if (level.isClientSide) {
			SZEventHandler.ACTIONBAR_TEXTS.put(new TranslatableComponent("message.suszombification.suspicious_pumpkin_pie.air").withStyle(ChatFormatting.AQUA), 80);
		}

		return super.finishUsingItem(stack, level, entity);
	}
}