package suszombification;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.block.entity.TrophyBlockEntity;

public class SZBlockEntityTypes {
	public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SuspiciousZombification.MODID);

	public static final RegistryObject<TileEntityType<TrophyBlockEntity>> TROPHY = BLOCK_ENTITY_TYPES.register("trophy", () -> TileEntityType.Builder.of(TrophyBlockEntity::new,
			SZBlocks.CARROT_TROPHY.get(),
			SZBlocks.POTATO_TROPHY.get(),
			SZBlocks.IRON_INGOT_TROPHY.get()).build(null));
}
