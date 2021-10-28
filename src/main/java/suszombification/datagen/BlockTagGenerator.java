package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
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
		tag(SZTags.Blocks.TROPHIES).add(
				SZBlocks.CARROT_TROPHY.get(),
				SZBlocks.POTATO_TROPHY.get(),
				SZBlocks.IRON_INGOT_TROPHY.get());
		tag(BlockTags.WOOL).addTag(SZTags.Blocks.ROTTEN_WOOL);
		tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(SZTags.Blocks.TROPHIES);
		tag(BlockTags.NEEDS_IRON_TOOL).addTag(SZTags.Blocks.TROPHIES);
	}

	@Override
	public String getName() {
		return "Block Tags: " + SuspiciousZombification.MODID;
	}
}
