package suszombification.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.SZItems;
import suszombification.entity.ZombifiedAnimal;
import suszombification.item.SuspiciousPumpkinPieItem;

@Mixin(ZombieHorse.class)
public class ZombieHorseMixin extends AbstractHorse implements ZombifiedAnimal, NeutralMob {
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombieHorse.class, EntityDataSerializers.BOOLEAN);
	private int conversionTime;
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	protected ZombieHorseMixin(EntityType<? extends AbstractHorse> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_CONVERTING_ID, false);
	}

	@Override
	public void tick() {
		if(!level.isClientSide && isAlive() && isConverting()) {
			conversionTime -= getConversionProgress();

			if(conversionTime <= 0 && ForgeEventFactory.canLivingConvert(this, EntityType.HORSE, this::setConversionTime))
				finishConversion((ServerLevel)level);
		}

		super.tick();
	}

	@Inject(method="mobInteract", at=@At("HEAD"), cancellable=true)
	private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callback) {
		ItemStack stack = player.getItemInHand(hand);

		if(stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && SuspiciousPumpkinPieItem.hasIngredient(stack, Items.GOLDEN_APPLE)) {
			if(hasEffect(MobEffects.WEAKNESS)) {
				if(!player.getAbilities().instabuild)
					stack.shrink(1);

				if(!level.isClientSide)
					startConverting(random.nextInt(2401) + 3600);

				gameEvent(GameEvent.MOB_INTERACT, eyeBlockPosition());
				callback.setReturnValue(InteractionResult.SUCCESS);
			}

			callback.setReturnValue(InteractionResult.CONSUME);
		}
	}

	@Override
	public void handleEntityEvent(byte id) {
		if(id == EntityEvent.ZOMBIE_CONVERTING) {
			if(!isSilent())
				level.playLocalSound(position().x, getEyeY(), position().z, SoundEvents.ZOMBIE_VILLAGER_CURE, getSoundSource(), 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
		}
		else
			super.handleEntityEvent(id);
	}

	@Override
	public int getExperienceReward(Player player) {
		return super.getExperienceReward(player) + 5;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		if(stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && stack.hasTag() && stack.getTag().contains("Ingredient")) {
			CompoundTag ingredientTag = stack.getTag().getCompound("Ingredient");
			ItemStack ingredient = ItemStack.of(ingredientTag);

			return ingredient.is(Items.LEATHER);
		}

		return super.isFood(stack);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		if(tag.contains("ConversionTime", Tag.TAG_ANY_NUMERIC) && tag.getInt("ConversionTime") > -1)
			startConverting(tag.getInt("ConversionTime"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
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
		return entityData.get(DATA_CONVERTING_ID);
	}

	@Override
	public void setConverting() {
		entityData.set(DATA_CONVERTING_ID, true);
	}

	@Override
	public void setConversionTime(int conversionTime) {
		this.conversionTime = conversionTime;
	}
}
