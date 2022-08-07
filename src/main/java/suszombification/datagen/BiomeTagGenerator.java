package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import suszombification.SZTags;
import suszombification.SuspiciousZombification;

public class BiomeTagGenerator extends BiomeTagsProvider {
	public BiomeTagGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(SZTags.Biomes.HAS_DESERT_ZOMBIE_COVE).add(Biomes.DESERT);
		//@formatter:off
		tag(SZTags.Biomes.HAS_ZOMBIE_COVE).addTag(BiomeTags.IS_MOUNTAIN).add(
				Biomes.PLAINS,
				Biomes.SAVANNA,
				Biomes.SNOWY_PLAINS,
				Biomes.TAIGA,
				Biomes.GROVE);
		//@formatter:on
	}

	@Override
	public String getName() {
		return "Biome Tags: " + SuspiciousZombification.MODID;
	}
}
