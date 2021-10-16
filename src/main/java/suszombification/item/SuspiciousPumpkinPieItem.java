package suszombification.item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.RegistryObject;
import suszombification.SZEffects;
import suszombification.SZItems;

public class SuspiciousPumpkinPieItem extends Item {
	private static final Map<RegistryObject<Item>, Supplier<MobEffectInstance>> CUSTOM_EFFECTS = new HashMap<>();
	private static final Map<Item, Consumer<LivingEntity>> MISC_ITEMS = new HashMap<>();

	static {
		CUSTOM_EFFECTS.put(SZItems.SPOILED_MILK_BUCKET, () -> new MobEffectInstance(SZEffects.AMPLIFYING.get(), 1));
		CUSTOM_EFFECTS.put(SZItems.ROTTEN_EGG, () -> new MobEffectInstance(SZEffects.STENCH.get(), 2400));

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

			if (CUSTOM_EFFECTS.keySet().stream().anyMatch(ro -> ingredient.getItem() == ro.get())) {
				RegistryObject<Item> ingredientSupplier = SZItems.ITEMS.getEntries().stream().filter(ro -> ingredient.getItem() == ro.get()).findFirst().orElse(null);

				entity.addEffect(CUSTOM_EFFECTS.get(ingredientSupplier).get());
				entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100));
				color = ChatFormatting.DARK_PURPLE;
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

			if (entity instanceof Player player) {
				player.displayClientMessage(new TranslatableComponent("message.suszombification.suspicious_pumpkin_pie." + itemId).withStyle(color), true);
			}
		}
		else if (entity instanceof Player player) {
			player.displayClientMessage(new TranslatableComponent("message.suszombification.suspicious_pumpkin_pie.air").withStyle(ChatFormatting.AQUA), true);
		}

		return super.finishUsingItem(stack, level, entity);
	}
}