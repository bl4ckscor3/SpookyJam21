package suszombification.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.RegistryObject;
import suszombification.SuspiciousZombification;
import suszombification.block.TrophyBlock;
import suszombification.registration.SZBlocks;

public class BlockModelAndStateGenerator extends BlockStateProvider {
	public BlockModelAndStateGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		for (RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
			Block block = ro.get();

			if (block instanceof TrophyBlock)
				horizontalBlock(block, state -> new UncheckedModelFile(modLoc("block/trophy")));
			else
				simpleBlock(block);
		}
	}
}
