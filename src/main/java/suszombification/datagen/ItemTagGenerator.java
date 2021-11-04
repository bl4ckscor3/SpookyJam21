package suszombification.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import suszombification.SZTags;
import suszombification.SuspiciousZombification;

public class ItemTagGenerator extends ItemTagsProvider {
	public ItemTagGenerator(DataGenerator generator, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
		super(generator, blockTagsProvider, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		copy(SZTags.Blocks.ROTTEN_WOOL, SZTags.Items.ROTTEN_WOOL);
	}

	@Override
	public String getName() {
		return "Item Tags: " + SuspiciousZombification.MODID;
	}
}
