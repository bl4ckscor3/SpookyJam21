package suszombification.datagen;

import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import suszombification.SuspiciousZombification;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZItems;

public class ItemModelGenerator extends ItemModelProvider {
	public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
			Block block = ro.get();

			if (block.asItem() instanceof BlockItem)
				simpleParent(block);
		}

		for (RegistryObject<Item> ro : SZItems.ITEMS.getEntries()) {
			Item item = ro.get();

			if (item instanceof SpawnEggItem)
				spawnEgg(item);
			else
				flatItem(item);
		}

		//@formatter:off
		flatItem(SZItems.HONEY_CANDY.get())
		.transforms() //same transforms as item/handheld, but with a lower thirdperson scaling
		.transform(TransformType.THIRD_PERSON_RIGHT_HAND).rotation(0.0F, -90.0F, 55.0F).translation(0.0F, 4.0F, 0.5F).scale(0.66F).end()
		.transform(TransformType.THIRD_PERSON_LEFT_HAND).rotation(0.0F, 90.0F, -55.0F).translation(0.0F, 4.0F, 0.5F).scale(0.66F).end()
		.transform(TransformType.FIRST_PERSON_RIGHT_HAND).rotation(0.0F, -90.0F, 25.0F).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end()
		.transform(TransformType.FIRST_PERSON_LEFT_HAND).rotation(0.0F, 90.0F, -25.0F).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end()
		.end();
		//@formatter:on
		handheldRodItem(SZItems.PORKCHOP_ON_A_STICK.get());
	}

	private ItemModelBuilder flatItem(Item item) {
		String name = ForgeRegistries.ITEMS.getKey(item).getPath();

		return getBuilder(name).parent(new UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation(SuspiciousZombification.MODID, "item/" + name));
	}

	private void handheldRodItem(Item item) {
		String name = ForgeRegistries.ITEMS.getKey(item).getPath();

		getBuilder(name).parent(new UncheckedModelFile("item/handheld_rod")).texture("layer0", new ResourceLocation(SuspiciousZombification.MODID, "item/" + name));
	}

	private void spawnEgg(Item item) {
		getBuilder(ForgeRegistries.ITEMS.getKey(item).getPath()).parent(new UncheckedModelFile("item/template_spawn_egg"));
	}

	public void simpleParent(Block block) {
		String name = ForgeRegistries.BLOCKS.getKey(block).getPath();

		getBuilder(name).parent(new UncheckedModelFile(modLoc(BLOCK_FOLDER + "/" + name)));
	}
}
