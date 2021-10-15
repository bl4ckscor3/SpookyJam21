package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
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

		flatItem(SZItems.HONEY_CANDY.get())
		.transforms() //same transforms as item/handheld, but with a lower thirdperson scaling
		.transform(Perspective.THIRDPERSON_RIGHT).rotation(0.0F, -90.0F, 55.0F).translation(0.0F, 4.0F, 0.5F).scale(0.66F).end()
		.transform(Perspective.THIRDPERSON_LEFT).rotation(0.0F, 90.0F, -55.0F).translation(0.0F, 4.0F, 0.5F).scale(0.66F).end()
		.transform(Perspective.FIRSTPERSON_RIGHT).rotation(0.0F, -90.0F, 25.0F).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end()
		.transform(Perspective.FIRSTPERSON_LEFT).rotation(0.0F, 90.0F, -25.0F).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end()
		.end();
	}

	private ItemModelBuilder flatItem(Item item) {
		String name = item.getRegistryName().getPath();

		return getBuilder(name).parent(new UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation(SuspiciousZombification.MODID, "item/" + name));
	}

	private void handheldItem(Item item) {
		String name = item.getRegistryName().getPath();

		getBuilder(name).parent(new UncheckedModelFile("item/handheld")).texture("layer0", new ResourceLocation(SuspiciousZombification.MODID, "item/" + name));
	}

	private void spawnEgg(Item item) {
		getBuilder(item.getRegistryName().getPath()).parent(new UncheckedModelFile("item/template_spawn_egg"));
	}

	public void simpleParent(Block block) {
		String name = block.getRegistryName().getPath();

		getBuilder(name).parent(new UncheckedModelFile(modLoc(BLOCK_FOLDER + "/" + name)));
	}
}
