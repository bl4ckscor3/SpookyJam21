package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
import suszombification.SZBlocks;
import suszombification.SZItems;
import suszombification.SuspiciousZombification;

public class ItemModelGenerator extends ItemModelProvider {
	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for(RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
			Block block = ro.get();
			Item item = block.asItem();

			if (item instanceof BlockItem) {
				simpleParent(block);
			}
		}

		for (RegistryObject<Item> ro : SZItems.ITEMS.getEntries()) {
			Item item = ro.get();

			if (item instanceof SpawnEggItem) {
				spawnEgg(item);
			}
			else {
				flatItem(item);
			}
		}
	}

	private void flatItem(Item item) {
		getBuilder(item.getRegistryName().getPath()).parent(new UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation(SuspiciousZombification.MODID, "item/" + item.getRegistryName().getPath()));
	}

	private void spawnEgg(Item item) {
		getBuilder(item.getRegistryName().getPath()).parent(new UncheckedModelFile("item/template_spawn_egg"));
	}

	public void simpleParent(Block block) {
		String name = block.getRegistryName().getPath();

		getBuilder(name).parent(new UncheckedModelFile(modLoc(BLOCK_FOLDER + "/" + name)));
	}

	@Override
	public String getName() {
		return "Suspicious Zombification Item Models";
	}
}
