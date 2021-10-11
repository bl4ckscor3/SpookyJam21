package suszombification.entity;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import suszombification.SZEntityTypes;
import suszombification.SZItems;
import suszombification.entity.ai.NearestAttackableEntityTypeGoal;
import suszombification.entity.ai.SPPTemptGoal;

public class ZombifiedChicken extends Animal implements NeutralMob, ZombifiedAnimal { //can't extend Chicken because of the hardcoded egg laying logic in Chicken#aiStep
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CHICKEN, Items.FEATHER);
	public float flap;
	public float flapSpeed;
	public float oFlapSpeed;
	public float oFlap;
	public float flapping = 1.0F;
	private float nextFlap = 1.0F;
	public int eggTime = random.nextInt(6000) + 6000;
	public boolean isChickenJockey;
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedChicken(EntityType<? extends ZombifiedChicken> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(3, new SPPTemptGoal(this, 1.0D, FOOD_ITEMS, false));
		goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableEntityTypeGoal<>(this, EntityType.CHICKEN, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return isBaby() ? size.height * 0.85F : size.height * 0.92F;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 1.0D);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		oFlap = flap;
		oFlapSpeed = flapSpeed;
		flapSpeed = (float)(flapSpeed + (onGround ? -1 : 4) * 0.3D);
		flapSpeed = Mth.clamp(flapSpeed, 0.0F, 1.0F);

		if (!onGround && flapping < 1.0F) {
			flapping = 1.0F;
		}

		flapping = flapping * 0.9F;

		Vec3 vec3 = getDeltaMovement();

		if (!onGround && vec3.y < 0.0D) {
			setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
		}

		flap += flapping * 2.0F;

		if (!level.isClientSide && isAlive() && !isBaby() && !isChickenJockey() && --eggTime <= 0) {
			playSound(SoundEvents.CHICKEN_EGG, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
			spawnAtLocation(SZItems.ROTTEN_EGG.get());
			eggTime = random.nextInt(6000) + 6000;
		}
	}

	@Override
	protected boolean isFlapping() {
		return flyDist > nextFlap;
	}

	@Override
	protected void onFlap() {
		this.nextFlap = flyDist + flapSpeed / 2.0F;
	}

	@Override
	public boolean causeFallDamage(float height, float mult, DamageSource source) {
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
	public ZombifiedChicken getBreedOffspring(ServerLevel level, AgeableMob parent) {
		return SZEntityTypes.ZOMBIFIED_CHICKEN.get().create(level);
	}

	@Override
	protected int getExperienceReward(Player player) {
		return (isChickenJockey() ? 10 : super.getExperienceReward(player)) + 5;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		if (stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && stack.hasTag() && stack.getTag().contains("Ingredient")) {
			CompoundTag ingredientTag = stack.getTag().getCompound("Ingredient");
			ItemStack ingredient = ItemStack.of(ingredientTag);

			return FOOD_ITEMS.test(ingredient);
		}

		return false;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.isChickenJockey = tag.getBoolean("IsChickenJockey");

		if (tag.contains("EggLayTime")) {
			this.eggTime = tag.getInt("EggLayTime");
		}

	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("IsChickenJockey", isChickenJockey);
		tag.putInt("EggLayTime", eggTime);
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return isChickenJockey();
	}

	@Override
	public void positionRider(Entity passenger) {
		super.positionRider(passenger);

		float f = Mth.sin(yBodyRot * ((float)Math.PI / 180F));
		float f1 = Mth.cos(yBodyRot * ((float)Math.PI / 180F));

		passenger.setPos(getX() + 0.1F * f, getY(0.5D) + passenger.getMyRidingOffset() + 0.0D, getZ() - 0.1F * f1);

		if (passenger instanceof LivingEntity entity) {
			entity.yBodyRot = yBodyRot;
		}

	}

	public boolean isChickenJockey() {
		return isChickenJockey;
	}

	public void setChickenJockey(boolean jockey) {
		this.isChickenJockey = jockey;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
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
		this.persistentAngerTarget = target;
	}

	@Override
	public void startPersistentAngerTimer() {
		setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(random));
	}

	@Override
	public EntityType<?> getNormalVariant() {
		return EntityType.CHICKEN;
	}

	@Override
	public void readFrom(Animal animal) {
		if (animal instanceof Chicken chicken) {
			setChickenJockey(chicken.isChickenJockey());
		}
	}
}
