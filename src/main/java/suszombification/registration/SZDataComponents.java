package suszombification.registration;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import suszombification.SuspiciousZombification;

public class SZDataComponents {
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(SuspiciousZombification.MODID);
	//@formatter:off
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> CURSE_GIVEN = DATA_COMPONENTS.registerComponentType("curse_given", builder -> builder
			.persistent(Codec.unit(Unit.INSTANCE))
			.cacheEncoding());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStack>> INGREDIENT = DATA_COMPONENTS.registerComponentType("ingredient", builder -> builder
			.persistent(ItemStack.SINGLE_ITEM_CODEC)
			.networkSynchronized(ItemStack.STREAM_CODEC)
			.cacheEncoding());
	//@formatter:on

	private SZDataComponents() {}
}
