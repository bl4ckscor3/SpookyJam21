package suszombification.entity;

import java.util.UUID;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.world.World;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import suszombification.SZEntityTypes;
import suszombification.SZItems;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.misc.AnimalUtil;

public class ZombifiedCow extends CowEntity implements IAngerable, ZombifiedAnimal {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.BEEF, Items.LEATHER);
	private static final DataParameter<Boolean> DATA_CONVERTING_ID = EntityDataManager.defineId(ZombifiedCow.class, DataSerializers.BOOLEAN);
	private int conversionTime;
	private static final RangedInteger PERSISTENT_ANGER_TIME = TickRangeConverter.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedCow(EntityType<? extends ZombifiedCow> type, World level) {
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
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, PlayerEntity.class, 6.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.18D).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Override
	public void tick() {
		AnimalUtil.tick(this);
		super.tick();
	}

	@Override
	public float getVoicePitch() {
		return isBaby() ? (random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F : (random.nextFloat() - random.nextFloat()) * 0.2F;
	}

	@Override
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if(stack.getItem() == Items.BUCKET && !isBaby()) {
			ItemStack filledBucket = DrinkHelper.createFilledResult(stack, player, SZItems.SPOILED_MILK_BUCKET.get().getDefaultInstance());

			player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
			player.setItemInHand(hand, filledBucket);
			return ActionResultType.sidedSuccess(level.isClientSide);
		}

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
	public ZombifiedCow getBreedOffspring(ServerWorld level, AgeableEntity parent) {
		return SZEntityTypes.ZOMBIFIED_COW.get().create(level);
	}

	@Override
	protected int getExperienceReward(Player player) {
		return super.getExperienceReward(player) + 5;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return AnimalUtil.isFood(stack, FOOD_ITEMS);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT tag) {
		super.readAdditionalSaveData(tag);

		if(tag.contains("ConversionTime", Constants.NBT.TAG_ANY_NUMERIC) && tag.getInt("ConversionTime") > -1)
			startConverting(tag.getInt("ConversionTime"));
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ConversionTime", isConverting() ? conversionTime : -1);
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
		return EntityType.COW;
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
