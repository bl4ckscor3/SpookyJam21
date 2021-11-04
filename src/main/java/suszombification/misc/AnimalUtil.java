package suszombification.misc;

import java.util.function.Predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.SZItems;
import suszombification.entity.ZombifiedAnimal;
import suszombification.item.SuspiciousPumpkinPieItem;

public class AnimalUtil {
	public static void tick(LivingEntity me) {
		if(!me.level.isClientSide && me.isAlive()) {
			ZombifiedAnimal zombifiedAnimal = (ZombifiedAnimal)me;

			if(zombifiedAnimal.isConverting()) {
				zombifiedAnimal.setConversionTime(zombifiedAnimal.getConversionTime() - zombifiedAnimal.getConversionProgress());

				if(zombifiedAnimal.getConversionTime() <= 0 && ForgeEventFactory.canLivingConvert(me, zombifiedAnimal.getNormalVariant(), zombifiedAnimal::setConversionTime))
					zombifiedAnimal.finishConversion((ServerWorld)me.level);
			}
		}
	}

	public static ActionResultType mobInteract(LivingEntity me, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if(stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && SuspiciousPumpkinPieItem.hasIngredient(stack, Items.GOLDEN_APPLE)) {
			if(me.hasEffect(Effects.WEAKNESS)) {
				if(!player.getAbilities().instabuild)
					stack.shrink(1);

				if(!me.level.isClientSide)
					((ZombifiedAnimal)me).startConverting(me.getRandom().nextInt(2401) + 3600);

				return ActionResultType.SUCCESS;
			}

			return ActionResultType.CONSUME;
		}

		return ActionResultType.PASS;
	}

	public static boolean handleEntityEvent(LivingEntity me, byte id) {
		if(id == 16) {
			if(!me.isSilent())
				me.level.playLocalSound(me.position().x, me.getEyeY(), me.position().z, SoundEvents.ZOMBIE_VILLAGER_CURE, me.getSoundSource(), 1.0F + me.getRandom().nextFloat(), me.getRandom().nextFloat() * 0.7F + 0.3F, false);

			return true;
		}

		return false;
	}

	public static boolean isFood(ItemStack stack, Ingredient foodItems) {
		return isFood(stack, foodItems, $ -> true);
	}

	public static boolean isFood(ItemStack stack, Ingredient foodItems, Predicate<ItemStack> extraTest) {
		if(stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && stack.hasTag() && stack.getTag().contains("Ingredient")) {
			CompoundNBT ingredientTag = stack.getTag().getCompound("Ingredient");
			ItemStack ingredient = ItemStack.of(ingredientTag);

			return foodItems.test(ingredient) || extraTest.test(ingredient);
		}

		return false;
	}
}
