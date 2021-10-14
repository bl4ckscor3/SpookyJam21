package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import suszombification.SZBlocks;
import suszombification.SZTags;
import suszombification.SuspiciousZombification;

public class BlockTagGenerator extends BlockTagsProvider {
	public BlockTagGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(SZTags.Blocks.ROTTEN_WOOL).add(
				SZBlocks.ROTTEN_WHITE_WOOL.get(), 
				SZBlocks.ROTTEN_ORANGE_WOOL.get(), 
				SZBlocks.ROTTEN_MAGENTA_WOOL.get(), 
				SZBlocks.ROTTEN_LIGHT_BLUE_WOOL.get(), 
				SZBlocks.ROTTEN_YELLOW_WOOL.get(), 
				SZBlocks.ROTTEN_LIME_WOOL.get(), 
				SZBlocks.ROTTEN_PINK_WOOL.get(), 
				SZBlocks.ROTTEN_GRAY_WOOL.get(),
				SZBlocks.ROTTEN_LIGHT_GRAY_WOOL.get(),
				SZBlocks.ROTTEN_CYAN_WOOL.get(),
				SZBlocks.ROTTEN_PURPLE_WOOL.get(),
				SZBlocks.ROTTEN_BLUE_WOOL.get(), 
				SZBlocks.ROTTEN_BROWN_WOOL.get(), 
				SZBlocks.ROTTEN_GREEN_WOOL.get(), 
				SZBlocks.ROTTEN_RED_WOOL.get(),
				SZBlocks.ROTTEN_BLACK_WOOL.get());
	}

	@Override
	public String getName() {
		return "Suspicious Zombification Block Tags";
	}
}
