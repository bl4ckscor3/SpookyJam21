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
				SZBlocks.WHITE_ROTTEN_WOOl.get(),
				SZBlocks.ORANGE_ROTTEN_WOOL.get(),
				SZBlocks.MAGENTA_ROTTEN_WOOL.get(),
				SZBlocks.LIGHT_BLUE_ROTTEN_WOOL.get(),
				SZBlocks.YELLOW_ROTTEN_WOOL.get(),
				SZBlocks.LIME_ROTTEN_WOOL.get(),
				SZBlocks.PINK_ROTTEN_WOOL.get(),
				SZBlocks.GRAY_ROTTEN_WOOL.get(),
				SZBlocks.LIGHT_GRAY_ROTTEN_WOOL.get(),
				SZBlocks.CYAN_ROTTEN_WOOL.get(),
				SZBlocks.PURPLE_ROTTEN_WOOL.get(),
				SZBlocks.BLUE_ROTTEN_WOOL.get(),
				SZBlocks.BROWN_ROTTEN_WOOL.get(),
				SZBlocks.GREEN_ROTTEN_WOOL.get(),
				SZBlocks.RED_ROTTEN_WOOL.get(),
				SZBlocks.BLACK_ROTTEN_WOOL.get());
	}

	@Override
	public String getName() {
		return "Suspicious Zombification Block Tags";
	}
}
