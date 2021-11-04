package suszombification.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.fml.ModList;
import suszombification.SZDamageSources;
import suszombification.SZEffects;
import suszombification.SZItems;
import suszombification.SZTags;
import suszombification.compat.TrickOrTreatCompat;
import suszombification.misc.SuspiciousRitual;

public class SuspiciousPumpkinPieItem extends Item {
	public static record PieEffect(Function<ItemStack, Boolean> check, Supplier<EffectInstance> mainEffect, Supplier<EffectInstance> extraEffect, TextFormatting displayColor, String messageSuffix) {}
	private static final List<PieEffect> PIE_EFFECTS = new ArrayList<>();

	static {
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZItems.SPOILED_MILK_BUCKET.get()), () -> new EffectInstance(SZEffects.AMPLIFYING.get(), 1), () -> new EffectInstance(MobEffects.CONFUSION, 100), TextFormatting.DARK_PURPLE, ""));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZItems.ROTTEN_EGG.get()), () -> new EffectInstance(SZEffects.STENCH.get(), 2400), () -> new EffectInstance(MobEffects.CONFUSION, 100), TextFormatting.DARK_PURPLE, ""));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(SZTags.Items.ROTTEN_WOOL), () -> new EffectInstance(SZEffects.CUSHION.get(), 2400), () -> new EffectInstance(MobEffects.CONFUSION, 100), TextFormatting.DARK_PURPLE, "rotten_wool"));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(Items.GOLDEN_APPLE), () -> new EffectInstance(MobEffects.REGENERATION, 200, 1), () -> new EffectInstance(MobEffects.ABSORPTION, 2400), TextFormatting.AQUA, ""));
		PIE_EFFECTS.add(new PieEffect(stack -> stack.is(Items.ROTTEN_FLESH), () -> new EffectInstance(SZEffects.DECOMPOSING.get(), 600), () -> null, TextFormatting.AQUA, ""));

		if(ModList.get().isLoaded("trickortreat"))
			TrickOrTreatCompat.addEffects(PIE_EFFECTS);
	}

	public SuspiciousPumpkinPieItem(Properties properties) {
		super(properties);
	}

	public static void saveIngredient(ItemStack suspiciousPumpkinPie, ItemStack ingredient) {
		CompoundNBT ingredientTag = new CompoundNBT();

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
	public ItemStack finishUsingItem(ItemStack stack, World level, LivingEntity entity) {
		CompoundNBT tag = stack.getTag();
		String messageSuffix = "air";
		TextFormatting color = TextFormatting.GRAY;

		if(tag != null && tag.contains("Effects", 9)) {
			ListNBT effects = tag.getList("Effects", 10);

			for(int i = 0; i < effects.size(); ++i) {
				int duration = 160;
				CompoundNBT effectTag = effects.getCompound(i);
				Effect effect = Effect.byId(effectTag.getByte("EffectId"));

				if(effectTag.contains("EffectDuration", 3))
					duration = effectTag.getInt("EffectDuration");

				if(effect != null)
					entity.addEffect(new EffectInstance(effect, duration));
			}
		}

		if(tag != null && tag.contains("Ingredient")) {
			ItemStack ingredient = ItemStack.of(tag.getCompound("Ingredient"));
			boolean foundEffect = false;

			messageSuffix = ingredient.getItem().getRegistryName().getPath();
			color = TextFormatting.GOLD;

			for(PieEffect pieEffect : PIE_EFFECTS) {
				if(pieEffect.check.apply(ingredient)) {
					EffectInstance mainEffect = pieEffect.mainEffect.get();
					EffectInstance extraEffect = pieEffect.extraEffect.get();

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
					color = TextFormatting.GOLD;
				}
				else if(ingredient.is(Items.GUNPOWDER)) {
					boolean mobGriefing = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);

					if(!level.isClientSide)
						level.explode(null, SZDamageSources.SPP_EXPLOSION, null, entity.getX(), entity.getY(), entity.getZ(), 3, false, !(entity instanceof PlayerEntity) && !mobGriefing ? Explosion.Mode.NONE : Explosion.Mode.DESTROY);
				}
				else { //vanilla mob drop
					entity.addEffect(new EffectInstance(Effects.POISON, 300));
					messageSuffix = "mob_drop";
					color = TextFormatting.DARK_GREEN;
				}
			}

			//ritual
			if(ingredient.is(Items.GOLDEN_APPLE) && entity instanceof PlayerEntity player && player.hasEffect(Effects.WEAKNESS) && SuspiciousRitual.performRitual(level, player)) {
				color = TextFormatting.AQUA;
				messageSuffix = "cured_by_ritual";
			}
		}

		if(!level.isClientSide)
			entity.sendMessage(new TranslationTextComponent("message.suszombification.suspicious_pumpkin_pie." + messageSuffix).withStyle(color), Util.NIL_UUID);

		return super.finishUsingItem(stack, level, entity);
	}
}