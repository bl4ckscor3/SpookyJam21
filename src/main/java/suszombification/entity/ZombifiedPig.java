package suszombification.entity;

import java.util.UUID;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.SZEntityTypes;
import suszombification.entity.ai.NearestAttackableEntityTypeGoal;

public class ZombifiedPig extends Pig implements NeutralMob {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.PORKCHOP); //TODO: SPP with this ingredient
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedPig(EntityType<? extends ZombifiedPig> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0F, false));
		goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(Items.PORKCHOP), false)); //TODO: Porkchop on a Stick
		goalSelector.addGoal(4, new TemptGoal(this, 1.2D, FOOD_ITEMS, false));
		goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableEntityTypeGoal<>(this, EntityType.PIG, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 2.0D);
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
	public void killed(ServerLevel level, LivingEntity killedEntity) {
		super.killed(level, killedEntity);

		if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && killedEntity instanceof Pig pig && ForgeEventFactory.canLivingConvert(killedEntity, SZEntityTypes.ZOMBIFIED_PIG.get(), timer -> {})) {
			if (level.getDifficulty() != Difficulty.HARD && random.nextBoolean()) {
				return;
			}

			ZombifiedPig zombifiedPig = pig.convertTo(SZEntityTypes.ZOMBIFIED_PIG.get(), false);

			zombifiedPig.finalizeSpawn(level, level.getCurrentDifficultyAt(zombifiedPig.blockPosition()), MobSpawnType.CONVERSION, null, null);
			ForgeEventFactory.onLivingConvert(killedEntity, zombifiedPig);

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
	public Pig getBreedOffspring(ServerLevel level, AgeableMob mob) {
		return SZEntityTypes.ZOMBIFIED_PIG.get().create(level);
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
