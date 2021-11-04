package suszombification.entity.ai;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.AnimalEntity;
import suszombification.entity.ZombifiedAnimal;

public class NearestNormalVariantTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
	protected final EntityType<? extends AnimalEntity> targetType;

	public NearestNormalVariantTargetGoal(ZombifiedAnimal zombifiedAnimal, boolean mustSee, boolean mustReach) {
		super((AnimalEntity)zombifiedAnimal, LivingEntity.class, mustSee, mustReach);
		targetType = zombifiedAnimal.getNormalVariant();
	}

	@Override
	public boolean canUse() {
		return !((ZombifiedAnimal)mob).isConverting() && super.canUse();
	}

	@Override
	protected void findTarget() {
		target = mob.level.getNearestEntity(mob.level.getEntities(targetType, getTargetSearchArea(getFollowDistance()), e -> true), targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ());
	}
}
