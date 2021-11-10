package suszombification.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZItems;

public class ThrownRottenEgg extends ThrowableItemProjectile {
	public ThrownRottenEgg(EntityType<? extends ThrownRottenEgg> type, Level level) {
		super(type, level);
	}

	public ThrownRottenEgg(Level level, LivingEntity owner) {
		super(SZEntityTypes.ROTTEN_EGG.get(), owner, level);
	}

	public ThrownRottenEgg(Level level, double x, double y, double z) {
		super(SZEntityTypes.ROTTEN_EGG.get(), x, y, z, level);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if(id == 3) {
			for(int i = 0; i < 8; ++i) {
				level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, getItem()), getX(), getY(), getZ(), (random.nextFloat() - 0.5D) * 0.08D, (random.nextFloat() - 0.5D) * 0.08D, (random.nextFloat() - 0.5D) * 0.08D);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);

		Entity entity = result.getEntity();

		result.getEntity().hurt(DamageSource.thrown(this, getOwner()), 0.0F);

		if(entity instanceof LivingEntity livingEntity && random.nextInt(8) == 0)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);

		if(!level.isClientSide) {
			level.broadcastEntityEvent(this, (byte)3);
			discard();
		}
	}

	@Override
	protected Item getDefaultItem() {
		return SZItems.ROTTEN_EGG.get();
	}
}
