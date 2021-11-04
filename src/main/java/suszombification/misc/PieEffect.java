package suszombification.misc;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.TextFormatting;

public class PieEffect {
	public final Function<ItemStack, Boolean> check;
	public final Supplier<EffectInstance> mainEffect;
	public final Supplier<EffectInstance> extraEffect;
	public final TextFormatting displayColor;
	public final String messageSuffix;

	public PieEffect(Function<ItemStack, Boolean> check, Supplier<EffectInstance> mainEffect, Supplier<EffectInstance> extraEffect, TextFormatting displayColor, String messageSuffix) {
		this.check = check;
		this.mainEffect = mainEffect;
		this.extraEffect = extraEffect;
		this.displayColor = displayColor;
		this.messageSuffix = messageSuffix;
	}
}
