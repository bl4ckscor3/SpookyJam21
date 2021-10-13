package suszombification.entity.ai;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import suszombification.entity.ZombifiedAnimal;

public class ZombifiedAnimalAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
	protected final EntityType<? extends Animal> targetType;

	public ZombifiedAnimalAttackGoal(ZombifiedAnimal zombifiedAnimal, boolean mustSee, boolean mustReach) {
		super((Animal)zombifiedAnimal, LivingEntity.class, mustSee, mustReach);
		this.targetType = zombifiedAnimal.getNormalVariant();
	}

	@Override
	public boolean canUse() {
		if (mob instanceof ZombifiedAnimal zombifiedAnimal && zombifiedAnimal.isConverting()) {
			return false;
		}

		return super.canUse();
	}

	@Override
	protected void findTarget() {
		target = mob.level.getNearestEntity(mob.level.getEntities(targetType, getTargetSearchArea(getFollowDistance()), e -> true), targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ());
	}
}
