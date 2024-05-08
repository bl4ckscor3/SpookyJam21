package suszombification.mixin.datafix;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;

import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;

/**
 * Makes sure SusZ's items convert to components properly without losing any data
 */
@Mixin(ItemStackComponentizationFix.class)
public class ItemStackComponentizationFixMixin {
	@Unique
	private static final Set<String> TROPHIES = Set.of("suszombification:carrot_trophy", "suszombification:potato_trophy", "suszombification:iron_ingot_trophy");

	@Inject(method = "fixItemStack", at = @At("TAIL"))
	private static void suszombification$fixItemStacks(ItemStackComponentizationFix.ItemStackData itemStackData, Dynamic<?> dynamic, CallbackInfo ci) {
		if (itemStackData.is(TROPHIES)) {
			OptionalDynamic<?> curseGiven = itemStackData.removeTag("CurseGiven");

			if (curseGiven.get().isSuccess())
				itemStackData.setComponent("suszombification:curse_given", dynamic.emptyMap());
		}
	}
}
