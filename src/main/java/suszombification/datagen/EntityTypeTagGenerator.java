package suszombification.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import suszombification.SZTags;
import suszombification.SuspiciousZombification;
import suszombification.registration.SZEntityTypes;

public class EntityTypeTagGenerator extends EntityTypeTagsProvider {
	public EntityTypeTagGenerator(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		//@formatter:off
		tag(SZTags.EntityTypes.AFFECTED_BY_ZOMBIES_GRACE).add(
				EntityType.DROWNED,
				EntityType.HUSK,
				EntityType.ZOGLIN,
				EntityType.ZOMBIE,
				EntityType.ZOMBIE_HORSE,
				EntityType.ZOMBIE_VILLAGER,
				EntityType.ZOMBIFIED_PIGLIN,
				SZEntityTypes.ZOMBIFIED_CAT.get(),
				SZEntityTypes.ZOMBIFIED_CHICKEN.get(),
				SZEntityTypes.ZOMBIFIED_COW.get(),
				SZEntityTypes.ZOMBIFIED_PIG.get(),
				SZEntityTypes.ZOMBIFIED_SHEEP.get());
		//@formatter:on
	}

	@Override
	public String getName() {
		return "Entity Type Tags: " + SuspiciousZombification.MODID;
	}
}
