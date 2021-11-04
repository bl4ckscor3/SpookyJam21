package suszombification;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.effect.AmplifyingEffect;
import suszombification.effect.DecomposingEffect;
import suszombification.effect.VicinityAffectingEffect;
import suszombification.effect.ZombiesCurseEffect;

public class SZEffects {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SuspiciousZombification.MODID);

	public static final RegistryObject<MobEffect> AMPLIFYING = EFFECTS.register("amplifying", () -> new AmplifyingEffect(MobEffectCategory.BENEFICIAL, 0xEBE294));
	public static final RegistryObject<MobEffect> CUSHION = EFFECTS.register("cushion", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0xEDEDED));
	public static final RegistryObject<MobEffect> DECOMPOSING = EFFECTS.register("decomposing", () -> new DecomposingEffect(MobEffectCategory.HARMFUL, 0x799C65).addAttributeModifier(Attributes.MOVEMENT_SPEED, "71c0aa55-a4ea-410d-8a42-99e9daa37ef5", -0.2D, Operation.MULTIPLY_TOTAL));
	public static final RegistryObject<MobEffect> STENCH = EFFECTS.register("stench", () -> new VicinityAffectingEffect(MobEffectCategory.BENEFICIAL, 0xCACC52,
			amplifier -> Math.max((amplifier + 1) * 3, 10),
			e -> true,
			() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 1),
			() -> new MobEffectInstance(MobEffects.WEAKNESS, 200, 1),
			() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0)));
	public static final RegistryObject<MobEffect> ZOMBIES_GRACE = EFFECTS.register("zombies_grace", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0x009E9E));
	public static final RegistryObject<MobEffect> ZOMBIES_CURSE = EFFECTS.register("zombies_curse", () -> new ZombiesCurseEffect(MobEffectCategory.HARMFUL, 0xAE1A1A));
}
