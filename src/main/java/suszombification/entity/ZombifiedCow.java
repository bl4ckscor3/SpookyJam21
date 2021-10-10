package suszombification.entity;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
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
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import suszombification.SZEntityTypes;
import suszombification.SZItems;
import suszombification.entity.ai.NearestAttackableEntityTypeGoal;
import suszombification.entity.ai.SPPTemptGoal;

public class ZombifiedCow extends Cow implements NeutralMob, ZombifiedAnimal {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.BEEF, Items.LEATHER);
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedCow(EntityType<? extends ZombifiedCow> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(3, new SPPTemptGoal(this, 1.25D, FOOD_ITEMS, false));
		goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableEntityTypeGoal<>(this, EntityType.COW, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.18D).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Override
	public float getVoicePitch() {
		return isBaby() ? (random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F : (random.nextFloat() - random.nextFloat()) * 0.2F;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.is(Items.BUCKET) && !isBaby()) {
			ItemStack filledBucket = ItemUtils.createFilledResult(stack, player, SZItems.ROTTEN_MILK_BUCKET.get().getDefaultInstance());

			player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
			player.setItemInHand(hand, filledBucket);
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return super.mobInteract(player, hand);
		}
	}

	@Override
	public ZombifiedCow getBreedOffspring(ServerLevel level, AgeableMob parent) {
		return SZEntityTypes.ZOMBIFIED_COW.get().create(level);
	}

	@Override
	protected int getExperienceReward(Player player) {
		return super.getExperienceReward(player) + 5;
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
		persistentAngerTarget = target;
	}

	@Override
	public void startPersistentAngerTimer() {
		setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(random));
	}

	@Override
	public EntityType<? extends Animal> getCastedType() {
		return SZEntityTypes.ZOMBIFIED_COW.get();
	}

	@Override
	public EntityType<?> getNormalVariant() {
		return EntityType.COW;
	}
}
