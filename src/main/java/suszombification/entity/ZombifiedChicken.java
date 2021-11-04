package suszombification.entity;

import java.util.UUID;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.ResetAngerGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import suszombification.SZEntityTypes;
import suszombification.SZItems;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.misc.AnimalUtil;

public class ZombifiedChicken extends AnimalEntity implements IAngerable, ZombifiedAnimal { //can't extend Chicken because of the hardcoded egg laying logic in Chicken#aiStep
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CHICKEN, Items.FEATHER);
	private static final DataParameter<Boolean> DATA_CONVERTING_ID = EntityDataManager.defineId(ZombifiedChicken.class, DataSerializers.BOOLEAN);
	private int conversionTime;
	public float flap;
	public float flapSpeed;
	public float previousFlapSpeed;
	public float previousFlap;
	public float flapping = 1.0F;
	public int eggTime = random.nextInt(6000) + 6000;
	public boolean isChickenJockey;
	private static final RangedInteger PERSISTENT_ANGER_TIME = TickRangeConverter.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedChicken(EntityType<? extends ZombifiedChicken> type, World level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_CONVERTING_ID, false);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(2, new SPPTemptGoal(this, 1.0D, FOOD_ITEMS, false));
		goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		goalSelector.addGoal(7, new LookRandomlyGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetAngerGoal<>(this, false));
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntitySize size) {
		return isBaby() ? size.height * 0.85F : size.height * 0.92F;
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 1.0D);
	}

	@Override
	public void tick() {
		AnimalUtil.tick(this);
		super.tick();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		previousFlap = flap;
		previousFlapSpeed = flapSpeed;
		flapSpeed = (float)(flapSpeed + (onGround ? -1 : 4) * 0.3D);
		flapSpeed = MathHelper.clamp(flapSpeed, 0.0F, 1.0F);

		if(!onGround && flapping < 1.0F)
			flapping = 1.0F;

		flapping = flapping * 0.9F;

		Vector3d deltaMovement = getDeltaMovement();

		if (!onGround && deltaMovement.y < 0.0D) {
			setDeltaMovement(deltaMovement.multiply(1.0D, 0.6D, 1.0D));
		}

		flap += flapping * 2.0F;

		if(!level.isClientSide && isAlive() && !isBaby() && !isChickenJockey() && --eggTime <= 0) {
			playSound(SoundEvents.CHICKEN_EGG, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
			spawnAtLocation(SZItems.ROTTEN_EGG.get());
			eggTime = random.nextInt(6000) + 6000;
		}
	}

	@Override
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ActionResultType returnValue = AnimalUtil.mobInteract(this, player, hand);

		if(returnValue != ActionResultType.PASS)
			return returnValue;

		return super.mobInteract(player, hand);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if(!AnimalUtil.handleEntityEvent(this, id))
			super.handleEntityEvent(id);
	}

	@Override
	public boolean causeFallDamage(float height, float mult) {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.CHICKEN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.CHICKEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.CHICKEN_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
	}

	@Override
	public float getVoicePitch() {
		return isBaby() ? (random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F : (random.nextFloat() - random.nextFloat()) * 0.2F;
	}

	@Override
	public ZombifiedChicken getBreedOffspring(ServerWorld level, AgeableEntity parent) {
		return SZEntityTypes.ZOMBIFIED_CHICKEN.get().create(level);
	}

	@Override
	protected int getExperienceReward(PlayerEntity player) {
		return (isChickenJockey() ? 10 : super.getExperienceReward(player)) + 5;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return AnimalUtil.isFood(stack, FOOD_ITEMS);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT tag) {
		super.readAdditionalSaveData(tag);
		isChickenJockey = tag.getBoolean("IsChickenJockey");

		if(tag.contains("EggLayTime"))
			this.eggTime = tag.getInt("EggLayTime");

		if(tag.contains("ConversionTime", Constants.NBT.TAG_ANY_NUMERIC) && tag.getInt("ConversionTime") > -1)
			startConverting(tag.getInt("ConversionTime"));
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("IsChickenJockey", isChickenJockey);
		tag.putInt("EggLayTime", eggTime);
		tag.putInt("ConversionTime", isConverting() ? conversionTime : -1);
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return isChickenJockey();
	}

	@Override
	public void positionRider(Entity passenger) {
		super.positionRider(passenger);

		float f = MathHelper.sin(yBodyRot * ((float)Math.PI / 180F));
		float f1 = MathHelper.cos(yBodyRot * ((float)Math.PI / 180F));

		passenger.setPos(getX() + 0.1F * f, getY(0.5D) + passenger.getMyRidingOffset() + 0.0D, getZ() - 0.1F * f1);

		if(passenger instanceof LivingEntity)
			((LivingEntity)passenger).yBodyRot = yBodyRot;
	}

	public boolean isChickenJockey() {
		return isChickenJockey;
	}

	public void setChickenJockey(boolean jockey) {
		isChickenJockey = jockey;
	}

	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.UNDEAD;
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
		return EntityType.CHICKEN;
	}

	@Override
	public void readFromVanilla(AnimalEntity animal) {
		if(animal instanceof ChickenEntity)
			setChickenJockey(((ChickenEntity)animal).isChickenJockey());
	}

	@Override
	public void writeToVanilla(AnimalEntity animal) {
		if(animal instanceof ChickenEntity)
			((ChickenEntity)animal).setChickenJockey(isChickenJockey());
	}

	@Override
	public boolean isConverting() {
		return getEntityData().get(DATA_CONVERTING_ID);
	}

	@Override
	public void setConverting() {
		getEntityData().set(DATA_CONVERTING_ID, true);
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
