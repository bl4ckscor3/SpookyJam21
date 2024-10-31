package suszombification.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.Level;
import suszombification.misc.SuspiciousRitual;

@Mixin(LeadItem.class)
public class LeadItemMixin {
	@Inject(method = "bindPlayerMobs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Leashable;setLeashedTo(Lnet/minecraft/world/entity/Entity;Z)V"))
	private static void suszombification$bindPlayerMobs(Player player, Level level, BlockPos pos, CallbackInfoReturnable<InteractionResult> cir, @Local Leashable mob) {
		SuspiciousRitual.maybeSendInfoMessages(mob, level, pos, player);
	}
}
