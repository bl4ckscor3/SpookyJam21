package suszombification.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import suszombification.SZEntityTypes;
import suszombification.SZItems;

public class ThrownRottenEgg extends ProjectileItemEntity {
	public ThrownRottenEgg(EntityType<? extends ThrownRottenEgg> type, World level) {
		super(type, level);
	}

	public ThrownRottenEgg(World level, LivingEntity owner) {
		super(SZEntityTypes.ROTTEN_EGG.get(), owner, level);
	}

	public ThrownRottenEgg(World level, double x, double y, double z) {
		super(SZEntityTypes.ROTTEN_EGG.get(), x, y, z, level);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if(id == 3) {
			for(int i = 0; i < 8; ++i) {
				level.addParticle(new ItemParticleData(ParticleTypes.ITEM, getItem()), getX(), getY(), getZ(), (random.nextFloat() - 0.5D) * 0.08D, (random.nextFloat() - 0.5D) * 0.08D, (random.nextFloat() - 0.5D) * 0.08D);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult result) {
		super.onHitEntity(result);

		Entity entity = result.getEntity();

		result.getEntity().hurt(DamageSource.thrown(this, getOwner()), 0.0F);

		if(entity instanceof LivingEntity && random.nextInt(8) == 0)
			((LivingEntity)entity).addEffect(new EffectInstance(Effects.CONFUSION, 200));
	}

	@Override
	protected void onHit(RayTraceResult result) {
		super.onHit(result);

		if(!level.isClientSide) {
			level.broadcastEntityEvent(this, (byte)3);
			remove();
		}
	}

	@Override
	protected Item getDefaultItem() {
		return SZItems.ROTTEN_EGG.get();
	}
}
