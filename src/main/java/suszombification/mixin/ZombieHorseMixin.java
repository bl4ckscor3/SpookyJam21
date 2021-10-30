package suszombification.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.SZEventHandler;
import suszombification.SZItems;
import suszombification.entity.ZombifiedAnimal;
import suszombification.item.SuspiciousPumpkinPieItem;

@Mixin(ZombieHorse.class)
public abstract class ZombieHorseMixin implements ZombifiedAnimal, NeutralMob {
	private int conversionTime;
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	@Inject(method="tick", at=@At("HEAD"))
	private void tick(CallbackInfo callback) {
		ZombieHorse me = (ZombieHorse)(Object)this;

		if(!me.level.isClientSide && me.isAlive() && isConverting()) {
			conversionTime -= getConversionProgress();

			if(conversionTime <= 0 && ForgeEventFactory.canLivingConvert(me, EntityType.HORSE, this::setConversionTime))
				finishConversion((ServerLevel)me.level);
		}
	}

	@Inject(method="mobInteract", at=@At("HEAD"), cancellable=true)
	private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callback) {
		ItemStack stack = player.getItemInHand(hand);

		if(stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && SuspiciousPumpkinPieItem.hasIngredient(stack, Items.GOLDEN_APPLE)) {
			ZombieHorse me = (ZombieHorse)(Object)this;

			if(me.hasEffect(MobEffects.WEAKNESS)) {
				if(!player.getAbilities().instabuild)
					stack.shrink(1);

				if(!me.level.isClientSide)
					startConverting(60);

				me.gameEvent(GameEvent.MOB_INTERACT, me.eyeBlockPosition());
				callback.setReturnValue(InteractionResult.SUCCESS);
			}

			callback.setReturnValue(InteractionResult.CONSUME);
		}
	}

	@Inject(method="handleEntityEvent", at=@At("HEAD"))
	private void handleEntityEvent(byte id, CallbackInfo callback) {
		if(id == EntityEvent.ZOMBIE_CONVERTING) {
			ZombieHorse me = (ZombieHorse)(Object)this;

			if(!me.isSilent())
				me.level.playLocalSound(me.position().x, me.getEyeY(), me.position().z, SoundEvents.ZOMBIE_VILLAGER_CURE, me.getSoundSource(), 1.0F + me.getRandom().nextFloat(), me.getRandom().nextFloat() * 0.7F + 0.3F, false);
		}
	}

	@Inject(method="getExperienceReward", at=@At("RETURN"), cancellable=true)
	private void getExperienceReward(Player player, CallbackInfoReturnable<Integer> callback) {
		callback.setReturnValue(1 + ((ZombieHorse)(Object)this).level.random.nextInt(3) + 5);
	}

	@Inject(method="isFood", at=@At("HEAD"), cancellable=true)
	private void isFood(ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
		if(stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && stack.hasTag() && stack.getTag().contains("Ingredient")) {
			CompoundTag ingredientTag = stack.getTag().getCompound("Ingredient");
			ItemStack ingredient = ItemStack.of(ingredientTag);

			callback.setReturnValue(ingredient.is(Items.LEATHER));
		}
	}

	@Inject(method="readAdditionalSaveData", at=@At("TAIL"))
	private void readAdditionalSaveData(CompoundTag tag, CallbackInfo callback) {
		if(tag.contains("ConversionTime", Tag.TAG_ANY_NUMERIC) && tag.getInt("ConversionTime") > -1)
			startConverting(tag.getInt("ConversionTime"));
	}

	@Inject(method="addAdditionalSaveData", at=@At("TAIL"))
	private void addAdditionalSaveData(CompoundTag tag, CallbackInfo callback) {
		tag.putInt("ConversionTime", isConverting() ? conversionTime : -1);
	}

	@Override
	public int getRemainingPersistentAngerTime() {
		return remainingPersistentAngerTime;
	}

	@Override
	public void setRemainingPersistentAngerTime(int time) {
		remainingPersistentAngerTime = time;
	}

	@Override
	public UUID getPersistentAngerTarget() {
		return persistentAngerTarget;
	}

	@Override
	public void setPersistentAngerTarget(UUID target) {
		persistentAngerTarget = target;
	}

	@Override
	public void startPersistentAngerTimer() {
		setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(((ZombieHorse)(Object)this).getRandom()));
	}

	@Override
	public EntityType<? extends Animal> getNormalVariant() {
		return EntityType.HORSE;
	}

	@Override
	public boolean isConverting() {
		return ((ZombieHorse)(Object)this).getEntityData().get(SZEventHandler.zombieHorseDataConvertingId);
	}

	@Override
	public void setConverting() {
		((ZombieHorse)(Object)this).getEntityData().set(SZEventHandler.zombieHorseDataConvertingId, true);
	}

	@Override
	public void setConversionTime(int conversionTime) {
		this.conversionTime = conversionTime;
	}

	//	@Override
	//	@Shadow
	//	public LivingEntity getLastHurtByMob() {
	//		throw new IllegalStateException("Shadow of getLastHurtByMob failed!");
	//	}
	//
	//	@Override
	//	@Shadow
	//	public void setLastHurtByMob(LivingEntity livingBase) {
	//		throw new IllegalStateException("Shadow of setLastHurtByMob failed!");
	//	}
	//
	//	@Override
	//	@Shadow
	//	public void setLastHurtByPlayer(Player player) {
	//		throw new IllegalStateException("Shadow of setLastHurtByPlayer failed!");
	//	}
	//
	//	@Override
	//	@Shadow
	//	public void setTarget(LivingEntity livingEntity) {
	//		throw new IllegalStateException("Shadow of setTarget failed!");
	//	}
	//
	//	@Override
	//	@Shadow
	//	public boolean canAttack(LivingEntity livingEntity) {
	//		throw new IllegalStateException("Shadow of canAttack failed!");
	//	}
	//
	//	@Override
	//	@Shadow
	//	public LivingEntity getTarget() {
	//		throw new IllegalStateException("Shadow of getTarget failed!");
	//	}
}
