package suszombification.registration;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import suszombification.SuspiciousZombification;
import suszombification.block.entity.TrophyBlockEntity;

public class SZBlockEntityTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SuspiciousZombification.MODID);
	//@formatter:off
	public static final RegistryObject<BlockEntityType<TrophyBlockEntity>> TROPHY = BLOCK_ENTITY_TYPES.register("trophy", () -> BlockEntityType.Builder.of(TrophyBlockEntity::new,
			SZBlocks.CARROT_TROPHY.get(),
			SZBlocks.POTATO_TROPHY.get(),
			SZBlocks.IRON_INGOT_TROPHY.get()).build(null));
	//@formatter:on
}
