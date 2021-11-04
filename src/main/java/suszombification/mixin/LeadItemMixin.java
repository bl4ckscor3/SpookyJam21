package suszombification.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.LeadItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import suszombification.misc.SuspiciousRitual;

@Mixin(LeadItem.class)
public class LeadItemMixin {
	@Redirect(method="bindPlayerMobs", at=@At(value="INVOKE", target="Lnet/minecraft/entity/MobEntity;setLeashedTo(Lnet/minecraft/entity/Entity;Z)V"))
	private static void bindPlayerMobs(MobEntity mob, Entity entity, boolean sendAttachNotification, PlayerEntity player, World level, BlockPos pos) {
		SuspiciousRitual.maybeSendInfoMessages(mob, level, pos, player);
		mob.setLeashedTo(entity, sendAttachNotification);
	}
}
