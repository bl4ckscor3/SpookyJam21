package suszombification.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ItemStackComponent(ItemStack stack) {
	//@formatter:off
	public static final Codec<ItemStackComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(ItemStack.SINGLE_ITEM_CODEC.fieldOf("stack").forGetter(ItemStackComponent::stack))
			.apply(instance, ItemStackComponent::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackComponent> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC, ItemStackComponent::stack,
			ItemStackComponent::new);
	//@formatter:on

	public boolean is(Item item) {
		return stack.is(item);
	}

	@Override
	public final boolean equals(Object other) {
		return other instanceof ItemStackComponent isc && ItemStack.isSameItemSameComponents(stack, isc.stack);
	}

	@Override
	public final int hashCode() {
		return ItemStack.hashItemAndComponents(stack);
	}
}
