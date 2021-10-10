package suszombification.entity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
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
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.SZEntityTypes;
import suszombification.datagen.LootTableGenerator;
import suszombification.entity.ai.NearestAttackableEntityTypeGoal;

public class ZombifiedSheep extends Sheep implements NeutralMob {
	private static final Map<DyeColor, ItemLike> ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), map -> {
		map.put(DyeColor.WHITE, Blocks.WHITE_WOOL); //TODO: Rotten Wools
		map.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
		map.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
		map.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
		map.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
		map.put(DyeColor.LIME, Blocks.LIME_WOOL);
		map.put(DyeColor.PINK, Blocks.PINK_WOOL);
		map.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
		map.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
		map.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
		map.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
		map.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
		map.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
		map.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
		map.put(DyeColor.RED, Blocks.RED_WOOL);
		map.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
	});
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.MUTTON, Items.WHITE_WOOL); //TODO: SPP with these ingredients (all wool colours)
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedSheep(EntityType<? extends Sheep> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		this.eatBlockGoal = new EatBlockGoal(this);
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0F, false));
		goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(4, new TemptGoal(this, 1.2D, FOOD_ITEMS, false));
		goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(5, eatBlockGoal);
		goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableEntityTypeGoal<>(this, EntityType.SHEEP, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.ATTACK_DAMAGE, 2.0F);
	}
	
	@Override
	public ResourceLocation getDefaultLootTable() {
		if (isSheared()) {
			return getType().getDefaultLootTable();
		} else {
			return switch (getColor()) {
				case WHITE -> LootTableGenerator.ZOMBIFIED_SHEEP_WHITE;
				case ORANGE -> LootTableGenerator.ZOMBIFIED_SHEEP_ORANGE;
				case MAGENTA -> LootTableGenerator.ZOMBIFIED_SHEEP_MAGENTA;
				case LIGHT_BLUE -> LootTableGenerator.ZOMBIFIED_SHEEP_LIGHT_BLUE;
				case YELLOW -> LootTableGenerator.ZOMBIFIED_SHEEP_YELLOW;
				case LIME -> LootTableGenerator.ZOMBIFIED_SHEEP_LIME;
				case PINK -> LootTableGenerator.ZOMBIFIED_SHEEP_PINK;
				case GRAY -> LootTableGenerator.ZOMBIFIED_SHEEP_GRAY;
				case LIGHT_GRAY -> LootTableGenerator.ZOMBIFIED_SHEEP_LIGHT_GRAY;
				case CYAN -> LootTableGenerator.ZOMBIFIED_SHEEP_CYAN;
				case PURPLE -> LootTableGenerator.ZOMBIFIED_SHEEP_PURPLE;
				case BLUE -> LootTableGenerator.ZOMBIFIED_SHEEP_BLUE;
				case BROWN -> LootTableGenerator.ZOMBIFIED_SHEEP_BROWN;
				case GREEN -> LootTableGenerator.ZOMBIFIED_SHEEP_GREEN;
				case RED -> LootTableGenerator.ZOMBIFIED_SHEEP_RED;
				case BLACK -> LootTableGenerator.ZOMBIFIED_SHEEP_BLACK;
			};
		}
	}

	@Override
	public void killed(ServerLevel level, LivingEntity killedEntity) {
		super.killed(level, killedEntity);

		if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && killedEntity instanceof Sheep sheep && ForgeEventFactory.canLivingConvert(killedEntity, SZEntityTypes.ZOMBIFIED_SHEEP.get(), timer -> {})) {
			if (level.getDifficulty() != Difficulty.HARD && random.nextBoolean()) {
				return;
			}

			ZombifiedSheep zombifiedSheep = sheep.convertTo(SZEntityTypes.ZOMBIFIED_SHEEP.get(), false);

			zombifiedSheep.finalizeSpawn(level, level.getCurrentDifficultyAt(zombifiedSheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
			zombifiedSheep.setColor(sheep.getColor());
			zombifiedSheep.setSheared(sheep.isSheared());
			ForgeEventFactory.onLivingConvert(killedEntity, zombifiedSheep);

			if (!isSilent()) {
				level.levelEvent(null, 1026, blockPosition(), 0);
			}
		}
	}

	@Override
	public float getVoicePitch() {
		return isBaby() ? (random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F : (random.nextFloat() - random.nextFloat()) * 0.2F;
	}

	@Override
	public Sheep getBreedOffspring(ServerLevel level, AgeableMob mob) {
		Sheep sheep = (Sheep)mob;
		Sheep newSheep = EntityType.SHEEP.create(level);

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
	public boolean isFood(ItemStack stack) {
		return FOOD_ITEMS.test(stack);
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
}
