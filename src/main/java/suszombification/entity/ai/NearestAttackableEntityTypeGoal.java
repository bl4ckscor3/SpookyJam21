package suszombification.entity.ai;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class NearestAttackableEntityTypeGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<LivingEntity> {
	protected final EntityType<T> targetType;

	public NearestAttackableEntityTypeGoal(Mob mob, EntityType<T> targetType, boolean mustSee, boolean mustReach) {
		super(mob, LivingEntity.class, mustSee, mustReach);
		this.targetType = targetType;
	}

	@Override
	protected void findTarget() {
		if (targetType != EntityType.PLAYER) {
			target = mob.level.getNearestEntity(mob.level.getEntities(targetType, getTargetSearchArea(getFollowDistance()), (e) -> true), targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ());
		} else {
			target = mob.level.getNearestPlayer(targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ());
		}

	}
}
