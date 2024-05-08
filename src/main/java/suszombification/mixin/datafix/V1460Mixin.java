package suszombification.mixin.datafix;

import java.util.Map;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V1460;

/**
 * Registers SusZ's entites to datafixers so chunks they are in convert properly
 */
@Mixin(V1460.class)
public class V1460Mixin {
	@Inject(method = "registerEntities", at = @At("TAIL"))
	private void suszombification$registerEntities(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> ci, @Local Map<String, Supplier<TypeTemplate>> map) {
		schema.registerSimple(map, "suszombification:zombified_cat");
		schema.registerSimple(map, "suszombification:zombified_chicken");
		schema.registerSimple(map, "suszombification:zombified_cow");
		schema.registerSimple(map, "suszombification:zombified_pig");
		schema.registerSimple(map, "suszombification:zombified_sheep");
		schema.register(map, "suszombification:rotten_egg", () -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
	}
}
