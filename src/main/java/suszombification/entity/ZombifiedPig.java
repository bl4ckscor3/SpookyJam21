package suszombification.entity;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.SZEntityTypes;
import suszombification.SZItems;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.item.SuspiciousPumpkinPieItem;

public class ZombifiedPig extends Pig implements NeutralMob, ZombifiedAnimal {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.PORKCHOP);
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombifiedPig.class, EntityDataSerializers.BOOLEAN);
	private int conversionTime;
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedPig(EntityType<? extends ZombifiedPig> type, Level level) {
		super(type, level);
		entityData.define(DATA_CONVERTING_ID, false);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0F, false));
		goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(Items.PORKCHOP), false)); //TODO: Porkchop on a Stick
		goalSelector.addGoal(4, new SPPTemptGoal(this, 1.2D, FOOD_ITEMS, false));
		goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}


	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Override
	public void tick() {
		if (!level.isClientSide && isAlive() && isConverting()) {
			int i = getConversionProgress();

			conversionTime -= i;
			if (conversionTime <= 0 && ForgeEventFactory.canLivingConvert(this, EntityType.PIG, this::setConversionTime)) {
				finishConversion((ServerLevel)level);
			}
		}

		super.tick();
	}

	@Override
	public boolean canBeControlledByRider() {
		Entity entity = getControllingPassenger();
		if (!(entity instanceof Player player)) {
			return false;
		} else {
			return player.isHolding(Items.PORKCHOP); //TODO: Porkchop on a Stick
		}
	}

	@Override
	public float getVoicePitch() {
		return isBaby() ? (random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F : (random.nextFloat() - random.nextFloat()) * 0.2F;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && SuspiciousPumpkinPieItem.hasIngredient(stack, Items.GOLDEN_APPLE)) {
			if (hasEffect(MobEffects.WEAKNESS)) {
				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}

				if (!level.isClientSide) {
					startConverting(random.nextInt(2401) + 3600);
				}

				gameEvent(GameEvent.MOB_INTERACT, eyeBlockPosition());
				return InteractionResult.SUCCESS;
			}

			return InteractionResult.CONSUME;
		}

		return super.mobInteract(player, hand);
	}

	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == 16) {
			if (!isSilent()) {
				level.playLocalSound(getX(), getEyeY(), getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, getSoundSource(), 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
			}
		} else {
			super.handleEntityEvent(pId);
		}
	}

	@Override
	public Pig getBreedOffspring(ServerLevel level, AgeableMob mob) {
		return SZEntityTypes.ZOMBIFIED_PIG.get().create(level);
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
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		if (tag.contains("ConversionTime", 99) && tag.getInt("ConversionTime") > -1) {
			startConverting(tag.getInt("ConversionTime"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ConversionTime", isConverting() ? conversionTime : -1);
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
	public EntityType<? extends Animal> getNormalVariant() {
		return EntityType.PIG;
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
}
