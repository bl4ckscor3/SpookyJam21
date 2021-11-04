package suszombification.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;

public class CustomSpawnEggItem extends SpawnEggItem {
	public static final List<CustomSpawnEggItem> SUS_EGGS = new ArrayList<>();
	private final Supplier<? extends EntityType<? extends MobEntity>> typeSupplier;

	public CustomSpawnEggItem(Supplier<? extends EntityType<? extends MobEntity>> type, int backgroundColor, int highlightColor, Properties properties) {
		super(null, backgroundColor, highlightColor, properties);

		typeSupplier = type;
		SUS_EGGS.add(this);
	}

	@Override
	public EntityType<?> getType(CompoundNBT tag) {
		EntityType<?> type = super.getType(tag);

		return type != null ? type : typeSupplier.get();
	}
}
