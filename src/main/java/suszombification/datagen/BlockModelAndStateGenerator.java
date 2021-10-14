package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
import suszombification.SZBlocks;
import suszombification.SuspiciousZombification;

public class BlockModelAndStateGenerator extends BlockStateProvider {
	public BlockModelAndStateGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		for (RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
			Block block = ro.get();

			//as long as we don't have any fancy models, this works well
			simpleBlock(block);
		}
	}

	@Override
	public String getName() {
		return "Suspicious Zombification Block States/Models";
	}
}
