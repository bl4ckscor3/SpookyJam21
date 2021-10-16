package suszombification.entity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.SZBlocks;
import suszombification.SZEntityTypes;
import suszombification.SZItems;
import suszombification.SZLootTables;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.item.SuspiciousPumpkinPieItem;

public class ZombifiedSheep extends Sheep implements NeutralMob, ZombifiedAnimal {
	private static final Map<DyeColor, ItemLike> ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), map -> {
		map.put(DyeColor.WHITE, SZBlocks.WHITE_ROTTEN_WOOl.get());
		map.put(DyeColor.ORANGE, SZBlocks.ORANGE_ROTTEN_WOOL.get());
		map.put(DyeColor.MAGENTA, SZBlocks.MAGENTA_ROTTEN_WOOL.get());
		map.put(DyeColor.LIGHT_BLUE, SZBlocks.LIGHT_BLUE_ROTTEN_WOOL.get());
		map.put(DyeColor.YELLOW, SZBlocks.YELLOW_ROTTEN_WOOL.get());
		map.put(DyeColor.LIME, SZBlocks.LIME_ROTTEN_WOOL.get());
		map.put(DyeColor.PINK, SZBlocks.PINK_ROTTEN_WOOL.get());
		map.put(DyeColor.GRAY, SZBlocks.GRAY_ROTTEN_WOOL.get());
		map.put(DyeColor.LIGHT_GRAY, SZBlocks.LIGHT_GRAY_ROTTEN_WOOL.get());
		map.put(DyeColor.CYAN, SZBlocks.CYAN_ROTTEN_WOOL.get());
		map.put(DyeColor.PURPLE, SZBlocks.PURPLE_ROTTEN_WOOL.get());
		map.put(DyeColor.BLUE, SZBlocks.BLUE_ROTTEN_WOOL.get());
		map.put(DyeColor.BROWN, SZBlocks.BROWN_ROTTEN_WOOL.get());
		map.put(DyeColor.GREEN, SZBlocks.GREEN_ROTTEN_WOOL.get());
		map.put(DyeColor.RED, SZBlocks.RED_ROTTEN_WOOL.get());
		map.put(DyeColor.BLACK, SZBlocks.BLACK_ROTTEN_WOOL.get());
	});
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.MUTTON);
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombifiedSheep.class, EntityDataSerializers.BOOLEAN);
	private int conversionTime;
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedSheep(EntityType<? extends Sheep> type, Level level) {
		super(type, level);
		entityData.define(DATA_CONVERTING_ID, false);
	}

	@Override
	protected void registerGoals() {
		this.eatBlockGoal = new EatBlockGoal(this);
		goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(2, new SPPTemptGoal(this, 1.0D, FOOD_ITEMS, false, stack -> stack.is(ItemTags.WOOL)));
		goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(5, eatBlockGoal);
		goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.ATTACK_DAMAGE, 2.0F);
	}

	@Override
	public void tick() {
		if (!level.isClientSide && isAlive() && isConverting()) {
			int i = getConversionProgress();

			conversionTime -= i;
			if (conversionTime <= 0 && ForgeEventFactory.canLivingConvert(this, EntityType.SHEEP, this::setConversionTime)) {
				finishConversion((ServerLevel)level);
			}
		}

		super.tick();
	}

	@Override
	public ResourceLocation getDefaultLootTable() {
		if (isSheared()) {
			return getType().getDefaultLootTable();
		} else {
			return switch (getColor()) {
				case WHITE -> SZLootTables.ZOMBIFIED_SHEEP_WHITE;
				case ORANGE -> SZLootTables.ZOMBIFIED_SHEEP_ORANGE;
				case MAGENTA -> SZLootTables.ZOMBIFIED_SHEEP_MAGENTA;
				case LIGHT_BLUE -> SZLootTables.ZOMBIFIED_SHEEP_LIGHT_BLUE;
				case YELLOW -> SZLootTables.ZOMBIFIED_SHEEP_YELLOW;
				case LIME -> SZLootTables.ZOMBIFIED_SHEEP_LIME;
				case PINK -> SZLootTables.ZOMBIFIED_SHEEP_PINK;
				case GRAY -> SZLootTables.ZOMBIFIED_SHEEP_GRAY;
				case LIGHT_GRAY -> SZLootTables.ZOMBIFIED_SHEEP_LIGHT_GRAY;
				case CYAN -> SZLootTables.ZOMBIFIED_SHEEP_CYAN;
				case PURPLE -> SZLootTables.ZOMBIFIED_SHEEP_PURPLE;
				case BLUE -> SZLootTables.ZOMBIFIED_SHEEP_BLUE;
				case BROWN -> SZLootTables.ZOMBIFIED_SHEEP_BROWN;
				case GREEN -> SZLootTables.ZOMBIFIED_SHEEP_GREEN;
				case RED -> SZLootTables.ZOMBIFIED_SHEEP_RED;
				case BLACK -> SZLootTables.ZOMBIFIED_SHEEP_BLACK;
			};
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
	public void shear(SoundSource category) {
		level.playSound(null, this, SoundEvents.SHEEP_SHEAR, category, 1.0F, 1.0F);
		setSheared(true);

		int i = 1 + random.nextInt(3);

		for(int j = 0; j < i; ++j) {
			ItemEntity item = spawnAtLocation(ITEM_BY_DYE.get(getColor()), 1);

			if (item != null) {
				item.setDeltaMovement(item.getDeltaMovement().add((random.nextFloat() - random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (random.nextFloat() - random.nextFloat()) * 0.1F));
			}
		}
	}

	@Override
	public Sheep getBreedOffspring(ServerLevel level, AgeableMob mob) {
		Sheep sheep = (Sheep)mob;
		Sheep newSheep = SZEntityTypes.ZOMBIFIED_SHEEP.get().create(level);

		newSheep.setColor(getOffspringColor(this, sheep));
		return newSheep;
	}

	@Override
	public List<ItemStack> onSheared(Player player, ItemStack item, Level world, BlockPos pos, int fortune) {
		world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);

		if (!world.isClientSide) {
			setSheared(true);

			int i = 1 + random.nextInt(3);
			List<ItemStack> items = new java.util.ArrayList<>();

			for (int j = 0; j < i; ++j) {
				items.add(new ItemStack(ITEM_BY_DYE.get(getColor())));
			}

			return items;
		}

		return Collections.emptyList();
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

			return FOOD_ITEMS.test(ingredient) || ingredient.is(ItemTags.WOOL);
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
		return EntityType.SHEEP;
	}

	@Override
	public void readFromVanilla(Animal animal) {
		if (animal instanceof Sheep sheep) {
			setColor(sheep.getColor());
			setSheared(sheep.isSheared());
		}
	}

	@Override
	public void writeToVanilla(Animal animal) {
		if (animal instanceof Sheep sheep) {
			sheep.setColor(getColor());
			sheep.setSheared(isSheared());
		}
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
