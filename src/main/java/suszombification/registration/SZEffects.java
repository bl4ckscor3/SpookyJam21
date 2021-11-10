package suszombification.registration;

import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.SuspiciousZombification;
import suszombification.effect.AmplifyingEffect;
import suszombification.effect.DecomposingEffect;
import suszombification.effect.VicinityAffectingEffect;
import suszombification.effect.ZombiesCurseEffect;

public class SZEffects {
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, SuspiciousZombification.MODID);

	public static final RegistryObject<Effect> AMPLIFYING = EFFECTS.register("amplifying", () -> new AmplifyingEffect(EffectType.BENEFICIAL, 0xEBE294));
	public static final RegistryObject<Effect> CUSHION = EFFECTS.register("cushion", () -> new Effect(EffectType.BENEFICIAL, 0xEDEDED));
	public static final RegistryObject<Effect> DECOMPOSING = EFFECTS.register("decomposing", () -> new DecomposingEffect(EffectType.HARMFUL, 0x799C65).addAttributeModifier(Attributes.MOVEMENT_SPEED, "71c0aa55-a4ea-410d-8a42-99e9daa37ef5", -0.2D, Operation.MULTIPLY_TOTAL));
	public static final RegistryObject<Effect> STENCH = EFFECTS.register("stench", () -> new VicinityAffectingEffect(EffectType.BENEFICIAL, 0xCACC52,
			amplifier -> Math.max((amplifier + 1) * 3, 10),
			e -> true,
			() -> new EffectInstance(Effects.CONFUSION, 200, 1),
			() -> new EffectInstance(Effects.WEAKNESS, 200, 1),
			() -> new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200, 0)));
	public static final RegistryObject<Effect> ZOMBIES_GRACE = EFFECTS.register("zombies_grace", () -> new Effect(EffectType.BENEFICIAL, 0x009E9E));
	public static final RegistryObject<Effect> ZOMBIES_CURSE = EFFECTS.register("zombies_curse", () -> new ZombiesCurseEffect(EffectType.HARMFUL, 0xAE1A1A));
}
