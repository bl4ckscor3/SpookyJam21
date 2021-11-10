package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import suszombification.SZTags;
import suszombification.SuspiciousZombification;
import suszombification.registration.SZEntityTypes;

public class EntityTypeTagGenerator extends EntityTypeTagsProvider {
	public EntityTypeTagGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
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
	}

	@Override
	public String getName() {
		return "Entity Type Tags: " + SuspiciousZombification.MODID;
	}
}
