package suszombification;

import net.minecraft.world.damagesource.DamageSource;

public class SZDamageSources {
	public static final DamageSource DECOMPOSING = new DamageSource("suszombification.decomposing").bypassArmor();
	public static final DamageSource RITUAL_SACRIFICE = new DamageSource("suszombification.ritual_sacrifice").bypassArmor();
	public static final DamageSource SPP_EXPLOSION = new DamageSource("suszombification.spp_explosion").setScalesWithDifficulty().setExplosion();
}
