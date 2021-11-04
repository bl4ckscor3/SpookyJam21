package suszombification.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.ResetAngerGoal;
import net.minecraft.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import suszombification.entity.ZombifiedAnimal;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.misc.AnimalUtil;

@Mixin(ZombieHorseEntity.class)
public class ZombieHorseMixin extends AbstractHorseEntity implements ZombifiedAnimal, IAngerable {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.LEATHER);
	private static final DataParameter<Boolean> DATA_CONVERTING_ID = EntityDataManager.defineId(ZombieHorseEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> DATA_ID_TYPE_VARIANT = EntityDataManager.defineId(ZombieHorseEntity.class, DataSerializers.INT);
	private int conversionTime;
	private static final RangedInteger PERSISTENT_ANGER_TIME = TickRangeConverter.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	protected ZombieHorseMixin(EntityType<? extends AbstractHorseEntity> type, World level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2D));
		goalSelector.addGoal(2, new BreedGoal(this, 1.0D, AbstractHorseEntity.class));
		goalSelector.addGoal(3, new SPPTemptGoal(this, 1.0D, Ingredient.of(Items.LEATHER), false));
		goalSelector.addGoal(4, new FollowParentGoal(this, 1.0D));
		goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.7D));
		goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		goalSelector.addGoal(8, new LookRandomlyGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetAngerGoal<>(this, false));
	}

	@Inject(method="createAttributes", at=@At("HEAD"), cancellable=true)
	private static void createAttributes(CallbackInfoReturnable<AttributeModifierMap.MutableAttribute> callback) {
		callback.setReturnValue(createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 2.0F));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_CONVERTING_ID, false);
		entityData.define(DATA_ID_TYPE_VARIANT, 0);
	}

	@Override
	public void tick() {
		AnimalUtil.tick(this);
		super.tick();
	}

	@Inject(method="mobInteract", at=@At("HEAD"), cancellable=true)
	private void mobInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> callback) {
		ActionResultType returnValue = AnimalUtil.mobInteract(this, player, hand);

		if(returnValue != ActionResultType.PASS)
			callback.setReturnValue(returnValue);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if(!AnimalUtil.handleEntityEvent(this, id))
			super.handleEntityEvent(id);
	}

	@Override
	public int getExperienceReward(PlayerEntity player) {
		return super.getExperienceReward(player) + 5;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return AnimalUtil.isFood(stack, FOOD_ITEMS) || super.isFood(stack);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT tag) {
		super.readAdditionalSaveData(tag);

		if(tag.contains("ConversionTime", Constants.NBT.TAG_ANY_NUMERIC) && tag.getInt("ConversionTime") > -1)
			startConverting(tag.getInt("ConversionTime"));

		entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ConversionTime", isConverting() ? conversionTime : -1);
		tag.putInt("Variant", entityData.get(DATA_ID_TYPE_VARIANT));
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
		setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(random));
	}

	@Override
	public EntityType<? extends AnimalEntity> getNormalVariant() {
		return EntityType.HORSE;
	}

	@Override
	public void readFromVanilla(AnimalEntity animal) {
		if(animal instanceof HorseEntity)
			entityData.set(DATA_ID_TYPE_VARIANT, ((HorseEntity)animal).getTypeVariant());
	}

	@Override
	public void writeToVanilla(AnimalEntity animal) {
		if(animal instanceof HorseEntity)
			((HorseEntity)animal).setTypeVariant(entityData.get(DATA_ID_TYPE_VARIANT));
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

	@Override
	public int getConversionTime() {
		return conversionTime;
	}
}
