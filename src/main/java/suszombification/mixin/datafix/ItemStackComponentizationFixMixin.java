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

		if (itemStackData.is("suszombification:suspicious_pumpkin_pie")) {
			//@formatter:off
			itemStackData.removeTag("Effects").result().ifPresent(effects -> itemStackData.setComponent("minecraft:suspicious_stew_effects",
					dynamic.createList(effects.asList(entry ->
							dynamic.emptyMap()
							.set("duration", dynamic.createInt(entry.get("EffectDuration").asInt(0)))
							.set("id", dynamic.createString(entry.get("forge:effect_id").asString("minecraft:speed"))))
							.stream())));
			//@formatter:on
			itemStackData.moveTagToComponent("effects", "minecraft:suspicious_stew_effects");
			itemStackData.removeTag("Ingredient").result().ifPresent(ingredient -> itemStackData.setComponent("suszombification:ingredient", dynamic.emptyMap().set("stack", ingredient)));
		}
	}
}
