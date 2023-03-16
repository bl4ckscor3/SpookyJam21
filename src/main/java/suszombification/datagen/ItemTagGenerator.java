package suszombification.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import suszombification.SZTags;
import suszombification.SuspiciousZombification;

public class ItemTagGenerator extends ItemTagsProvider {
	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagsProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTagsProvider, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		copy(SZTags.Blocks.ROTTEN_WOOL, SZTags.Items.ROTTEN_WOOL);
	}

	@Override
	public String getName() {
		return "Item Tags: " + SuspiciousZombification.MODID;
	}
}
