package suszombification;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;

public class SZDamageSources {
	public static final ResourceKey<DamageType> DECOMPOSING = ResourceKey.create(Registries.DAMAGE_TYPE, SuspiciousZombification.resLoc("decomposing"));
	public static final ResourceKey<DamageType> RITUAL_SACRIFICE = ResourceKey.create(Registries.DAMAGE_TYPE, SuspiciousZombification.resLoc("ritual_sacrifice"));
	public static final ResourceKey<DamageType> SPP_EXPLOSION = ResourceKey.create(Registries.DAMAGE_TYPE, SuspiciousZombification.resLoc("spp_explosion"));

	private SZDamageSources() {}

	public static DamageSource decomposing(RegistryAccess registryAccess) {
		return new DamageSource(registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DECOMPOSING));
	}

	public static DamageSource ritualSacrifice(Player player, RegistryAccess registryAccess) {
		return new DamageSource(registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(RITUAL_SACRIFICE), player);
	}

	public static DamageSource sppExplosion(RegistryAccess registryAccess) {
		return new DamageSource(registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(SPP_EXPLOSION));
	}
}
