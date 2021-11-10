package suszombification.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import suszombification.SZDamageSources;
import suszombification.SZTags;
import suszombification.compat.TrickOrTreatCompat;
import suszombification.misc.SuspiciousRitual;
import suszombification.registration.SZEffects;
import suszombification.registration.SZItems;

public class SuspiciousPumpkinPieItem extends Item {
	public static record PieEffect(Function<ItemStack, Boolean> check, Supplier<MobEffectInstance> mainEffect, Supplier<MobEffectInstance> extraEffect, ChatFormatting displayColor, String messageSuffix) {}
	private static final List<PieEffect> PIE_EFFECTS = new ArrayList<>();

	static {
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZItems.SPOILED_MILK_BUCKET.get()), () -> new MobEffectInstance(SZEffects.AMPLIFYING.get(), 1), () -> new MobEffectInstance(MobEffects.CONFUSION, 100), ChatFormatting.DARK_PURPLE, ""));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZItems.ROTTEN_EGG.get()), () -> new MobEffectInstance(SZEffects.STENCH.get(), 2400), () -> new MobEffectInstance(MobEffects.CONFUSION, 100), ChatFormatting.DARK_PURPLE, ""));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZTags.Items.ROTTEN_WOOL), () -> new MobEffectInstance(SZEffects.CUSHION.get(), 2400), () -> new MobEffectInstance(MobEffects.CONFUSION, 100), ChatFormatting.DARK_PURPLE, "rotten_wool"));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(Items.GOLDEN_APPLE), () -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), () -> new MobEffectInstance(MobEffects.ABSORPTION, 2400), ChatFormatting.AQUA, ""));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(Items.ROTTEN_FLESH), () -> new MobEffectInstance(SZEffects.DECOMPOSING.get(), 600), () -> null, ChatFormatting.AQUA, ""));

		if(ModList.get().isLoaded("trickortreat"))
			TrickOrTreatCompat.addEffects(PIE_EFFECTS);
	}

	public SuspiciousPumpkinPieItem(Properties properties) {
		super(properties);
	}

	public static void saveIngredient(ItemStack suspiciousPumpkinPie, ItemStack ingredient) {
		CompoundTag ingredientTag = new CompoundTag();

		if(ingredient.getItem() instanceof CandyItem candy)
			SuspiciousStewItem.saveMobEffect(suspiciousPumpkinPie, candy.getEffect(), candy.getEffectDuration());

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
		String messageSuffix = "air";
		ChatFormatting color = ChatFormatting.GRAY;

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
			boolean foundEffect = false;

			messageSuffix = ingredient.getItem().getRegistryName().getPath();
			color = ChatFormatting.GOLD;

			for(PieEffect pieEffect : PIE_EFFECTS) {
				if(pieEffect.check.apply(ingredient)) {
					MobEffectInstance mainEffect = pieEffect.mainEffect.get();
					MobEffectInstance extraEffect = pieEffect.extraEffect.get();

					if(mainEffect != null)
						entity.addEffect(mainEffect);

					if(extraEffect != null)
						entity.addEffect(extraEffect);

					if(!pieEffect.messageSuffix.isEmpty())
						messageSuffix = pieEffect.messageSuffix;

					foundEffect = true;
					break;
				}
			}

			if(!foundEffect && !(ingredient.getItem() instanceof CandyItem)) {
				if(ModList.get().isLoaded("trickortreat") && TrickOrTreatCompat.attemptCandyEffect(entity, level, ingredient)) {
					messageSuffix = "trickortreat";
					color = ChatFormatting.GOLD;
				}
				else if(ingredient.is(Items.GUNPOWDER)) {
					boolean mobGriefing = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);

					if(!level.isClientSide)
						level.explode(null, SZDamageSources.SPP_EXPLOSION, null, entity.getX(), entity.getY(), entity.getZ(), 3, false, !(entity instanceof Player) && !mobGriefing ? Explosion.BlockInteraction.NONE : Explosion.BlockInteraction.DESTROY);
				}
				else { //vanilla mob drop
					entity.addEffect(new MobEffectInstance(MobEffects.POISON, 300));
					messageSuffix = "mob_drop";
					color = ChatFormatting.DARK_GREEN;
				}
			}

			//ritual
			if(ingredient.is(Items.GOLDEN_APPLE) && entity instanceof Player player && player.hasEffect(MobEffects.WEAKNESS) && SuspiciousRitual.performRitual(level, player)) {
				color = ChatFormatting.AQUA;
				messageSuffix = "cured_by_ritual";
			}
		}

		if(!level.isClientSide)
			entity.sendMessage(new TranslatableComponent("message.suszombification.suspicious_pumpkin_pie." + messageSuffix).withStyle(color), Util.NIL_UUID);

		return super.finishUsingItem(stack, level, entity);
	}
}