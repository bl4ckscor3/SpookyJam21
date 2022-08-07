package suszombification.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.Level;
import suszombification.misc.SuspiciousRitual;

@Mixin(LeadItem.class)
public class LeadItemMixin {
	@Redirect(method = "bindPlayerMobs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;setLeashedTo(Lnet/minecraft/world/entity/Entity;Z)V"))
	private static void bindPlayerMobs(Mob mob, Entity entity, boolean sendAttachNotification, Player player, Level level, BlockPos pos) {
		SuspiciousRitual.maybeSendInfoMessages(mob, level, pos, player);
		mob.setLeashedTo(entity, sendAttachNotification);
	}
}
