package suszombification.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.client.audio.SoundSource;
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
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.World;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import suszombification.SZBlocks;
import suszombification.SZEntityTypes;
import suszombification.SZLootTables;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.misc.AnimalUtil;

public class ZombifiedSheep extends SheepEntity implements IAngerable, ZombifiedAnimal {
	private static final Map<DyeColor, IItemProvider> ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), map -> {
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
	private static final DataParameter<Boolean> DATA_CONVERTING_ID = EntityDataManager.defineId(ZombifiedSheep.class, DataSerializers.BOOLEAN);
	private int conversionTime;
	private static final RangedInteger PERSISTENT_ANGER_TIME = TickRangeConverter.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedSheep(EntityType<? extends SheepEntity> type, World level) {
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
		goalSelector.addGoal(2, new SPPTemptGoal(this, 1.0D, FOOD_ITEMS, false, stack -> stack.is(ItemTags.WOOL)));
		goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(5, eatBlockGoal = new EatBlockGoal(this));
		goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, PlayerEntity.class, 6.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.ATTACK_DAMAGE, 2.0F);
	}

	@Override
	public void tick() {
		AnimalUtil.tick(this);
		super.tick();
	}

	@Override
	public ResourceLocation getDefaultLootTable() {
		if(isSheared())
			return getType().getDefaultLootTable();
		else {
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
	public void shear(SoundCategory category) {
		level.playSound(null, this, SoundEvents.SHEEP_SHEAR, category, 1.0F, 1.0F);
		setSheared(true);

		int amount = 1 + random.nextInt(3);

		for(int i = 0; i < amount; ++i) {
			ItemEntity item = spawnAtLocation(ITEM_BY_DYE.get(getColor()), 1);

			if(item != null)
				item.setDeltaMovement(item.getDeltaMovement().add((random.nextFloat() - random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (random.nextFloat() - random.nextFloat()) * 0.1F));
		}
	}

	@Override
	public SheepEntity getBreedOffspring(ServerWorld level, AgeableEntity mob) {
		SheepEntity sheep = (SheepEntity)mob;
		SheepEntity newSheep = SZEntityTypes.ZOMBIFIED_SHEEP.get().create(level);

		newSheep.setColor(getOffspringColor(this, sheep));
		return newSheep;
	}

	@Override
	public List<ItemStack> onSheared(PlayerEntity player, ItemStack item, World level, BlockPos pos, int fortune) {
		level.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundCategory.BLOCKS : SoundCategory.PLAYERS, 1.0F, 1.0F);

		if(!level.isClientSide) {
			setSheared(true);

			int amount = 1 + random.nextInt(3);
			List<ItemStack> items = new ArrayList<>();

			for(int i = 0; i < amount; ++i) {
				items.add(new ItemStack(ITEM_BY_DYE.get(getColor())));
			}

			return items;
		}

		return Collections.emptyList();
	}

	@Override
	protected int getExperienceReward(PlayerEntity player) {
		return super.getExperienceReward(player) + 5;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return AnimalUtil.isFood(stack, FOOD_ITEMS, ingredient -> ingredient.is(ItemTags.WOOL));
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
		setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(random));
	}

	@Override
	public EntityType<? extends AnimalEntity> getNormalVariant() {
		return EntityType.SHEEP;
	}

	@Override
	public void readFromVanilla(AnimalEntity animal) {
		if(animal instanceof SheepEntity sheep) {
			setColor(sheep.getColor());
			setSheared(sheep.isSheared());
		}
	}

	@Override
	public void writeToVanilla(AnimalEntity animal) {
		if(animal instanceof SheepEntity sheep) {
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

	@Override
	public int getConversionTime() {
		return conversionTime;
	}
}
