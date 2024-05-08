package suszombification.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.neoforged.neoforge.registries.DeferredHolder;
import suszombification.SZDamageSources;
import suszombification.SZTags;
import suszombification.misc.SuspiciousRitual;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZEffects;
import suszombification.registration.SZItems;

public class SuspiciousPumpkinPieItem extends Item {
	public static record PieEffect(Function<ItemStack, Boolean> check, Supplier<MobEffectInstance> mainEffect, Supplier<MobEffectInstance> extraEffect, ChatFormatting displayColor, String messageSuffix) {}

	private static final List<PieEffect> PIE_EFFECTS = new ArrayList<>();

	static {
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZItems.SPOILED_MILK_BUCKET.get()), () -> new MobEffectInstance(SZEffects.AMPLIFYING, 1), () -> new MobEffectInstance(MobEffects.CONFUSION, 100), ChatFormatting.DARK_PURPLE, ""));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZItems.ROTTEN_EGG.get()), () -> new MobEffectInstance(SZEffects.STENCH, 2400), () -> new MobEffectInstance(MobEffects.CONFUSION, 100), ChatFormatting.DARK_PURPLE, ""));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZTags.Items.ROTTEN_WOOL), () -> new MobEffectInstance(SZEffects.CUSHION, 2400), () -> new MobEffectInstance(MobEffects.CONFUSION, 100), ChatFormatting.DARK_PURPLE, "rotten_wool"));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(Items.GOLDEN_APPLE), () -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), () -> new MobEffectInstance(MobEffects.ABSORPTION, 2400), ChatFormatting.AQUA, ""));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(Items.ROTTEN_FLESH), () -> new MobEffectInstance(SZEffects.DECOMPOSING, 600), () -> null, ChatFormatting.AQUA, ""));
	}

	public SuspiciousPumpkinPieItem(Properties properties) {
		super(properties);
	}

	public static void saveIngredient(ItemStack suspiciousPumpkinPie, ItemStack ingredient) {
		CompoundTag ingredientTag = new CompoundTag();

		if (ingredient.getItem() instanceof CandyItem candy)
			SuspiciousStewItem.saveMobEffects(suspiciousPumpkinPie, candy.getEffects());

		ingredient.setCount(1);
		ingredient.save(ingredientTag);
		suspiciousPumpkinPie.getOrCreateTag().put("Ingredient", ingredientTag);
	}

	public static boolean hasIngredient(ItemStack pie, Item test) {
		return pie.hasTag() && pie.getTag().contains("Ingredient") && ItemStack.of(pie.getTag().getCompound("Ingredient")).is(test);
	}

	public static List<Item> getAllDifferentIngredients() {
		List<Item> ingredients = new ArrayList<>();

		for (DeferredHolder<Item, ? extends Item> holder : SZItems.ITEMS.getEntries()) {
			Item item = holder.get();

			if (item instanceof CandyItem candy)
				ingredients.add(candy);
		}

		ingredients.add(SZItems.SPOILED_MILK_BUCKET.get());
		ingredients.add(SZItems.ROTTEN_EGG.get());
		ingredients.add(SZBlocks.WHITE_ROTTEN_WOOl.get().asItem());
		ingredients.add(Items.GOLDEN_APPLE);
		ingredients.add(Items.ROTTEN_FLESH);
		ingredients.add(Items.CHICKEN); //vanilla mob drops
		return ingredients;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);

		if (flag.isCreative()) {
			List<MobEffectInstance> effects = new ArrayList<>();

			listPotionEffects(stack, effects::add, null);
			PotionUtils.addPotionTooltip(effects, tooltip, 1.0F, level == null ? 20.0F : level.tickRateManager().tickrate());
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		listPotionEffects(stack, entity::addEffect, entity);
		return super.finishUsingItem(stack, level, entity);
	}

	private static void listPotionEffects(ItemStack stack, Consumer<MobEffectInstance> consumer, LivingEntity entity) {
		CompoundTag tag = stack.getTag();
		String messageSuffix = "air";
		ChatFormatting color = ChatFormatting.GRAY;

		if (tag != null && tag.contains("Effects", 9) && tag.contains("Ingredient", 10)) { //old effect data, needs to be upgraded
			ItemStack ingredientStack = ItemStack.of(tag.getCompound("Ingredient"));

			if (!ingredientStack.isEmpty()) {
				saveIngredient(stack, ingredientStack);
				tag.remove("Effects");
			}
		}

		if (tag != null && tag.contains("effects", 9)) {
			//@formatter:off
			SuspiciousEffectHolder.EffectEntry.LIST_CODEC
					.parse(NbtOps.INSTANCE, tag.getList("effects", 10))
					.result()
					.ifPresent(effectEntryList -> effectEntryList.forEach(entry -> consumer.accept(entry.createEffectInstance())));
			//@formatter:on
		}

		if (tag != null && tag.contains("Ingredient")) {
			ItemStack ingredient = ItemStack.of(tag.getCompound("Ingredient"));
			boolean foundEffect = false;

			messageSuffix = BuiltInRegistries.ITEM.getKey(ingredient.getItem()).getPath();
			color = ChatFormatting.GOLD;

			for (PieEffect pieEffect : PIE_EFFECTS) {
				if (pieEffect.check.apply(ingredient)) {
					MobEffectInstance mainEffect = pieEffect.mainEffect.get();
					MobEffectInstance extraEffect = pieEffect.extraEffect.get();

					if (mainEffect != null)
						consumer.accept(mainEffect);

					if (extraEffect != null)
						consumer.accept(extraEffect);

					if (!pieEffect.messageSuffix.isEmpty())
						messageSuffix = pieEffect.messageSuffix;

					foundEffect = true;
					break;
				}
			}

			if (!foundEffect && !(ingredient.getItem() instanceof CandyItem)) {
				if (ingredient.is(Items.GUNPOWDER)) {
					if (entity != null && !entity.level().isClientSide)
						entity.level().explode(null, SZDamageSources.sppExplosion(entity.level().registryAccess()), null, entity.getX(), entity.getY(), entity.getZ(), 3, false, ExplosionInteraction.MOB);
				}
				else { //vanilla mob drop
					consumer.accept(new MobEffectInstance(MobEffects.POISON, 300));
					messageSuffix = "mob_drop";
					color = ChatFormatting.DARK_GREEN;
				}
			}

			//ritual
			if (ingredient.is(Items.GOLDEN_APPLE) && entity instanceof Player player && player.hasEffect(MobEffects.WEAKNESS) && SuspiciousRitual.performRitual(player.level(), player)) {
				color = ChatFormatting.AQUA;
				messageSuffix = "cured_by_ritual";
			}
		}

		if (entity != null && !entity.level().isClientSide)
			entity.sendSystemMessage(Component.translatable("message.suszombification.suspicious_pumpkin_pie." + messageSuffix).withStyle(color));
	}
}