package suszombification.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import suszombification.SuspiciousZombification;
import suszombification.block.entity.TrophyBlockEntity;

public class SZBlockEntityTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SuspiciousZombification.MODID);
	//@formatter:off
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TrophyBlockEntity>> TROPHY = BLOCK_ENTITY_TYPES.register("trophy", () -> BlockEntityType.Builder.of(TrophyBlockEntity::new,
			SZBlocks.CARROT_TROPHY.get(),
			SZBlocks.POTATO_TROPHY.get(),
			SZBlocks.IRON_INGOT_TROPHY.get()).build(null));
	//@formatter:on

	private SZBlockEntityTypes() {}
}
