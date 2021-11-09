package suszombification.entity.ai;

import java.util.function.Predicate;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.AnimalEntity;
import suszombification.entity.ZombifiedAnimal;

public class NearestNormalVariantTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
	protected final EntityType<? extends AnimalEntity> targetType;
	protected final Predicate<AnimalEntity> mobPredicate;

	public NearestNormalVariantTargetGoal(ZombifiedAnimal zombifiedAnimal, boolean mustSee, boolean mustReach) {
		this(zombifiedAnimal, mustSee, mustReach, animal -> true);
	}

	public NearestNormalVariantTargetGoal(ZombifiedAnimal zombifiedAnimal, boolean mustSee, boolean mustReach, Predicate<AnimalEntity> predicate) {
		super((AnimalEntity)zombifiedAnimal, LivingEntity.class, mustSee, mustReach);
		targetType = zombifiedAnimal.getNormalVariant();
		mobPredicate = predicate;
	}

	@Override
	public boolean canUse() {
		return !((ZombifiedAnimal)mob).isConverting() && mobPredicate.test((AnimalEntity)mob) && super.canUse();
	}

	@Override
	protected void findTarget() {
		target = mob.level.getNearestEntity(mob.level.getEntities(targetType, getTargetSearchArea(getFollowDistance()), e -> true), targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ());
	}
}
