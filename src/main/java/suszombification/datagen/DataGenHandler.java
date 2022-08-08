package suszombification.datagen;

import java.util.Collections;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import suszombification.SuspiciousZombification;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
public class DataGenHandler {
	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = new ExistingFileHelper(Collections.EMPTY_LIST, Collections.EMPTY_SET, false, null, null);
		BlockTagGenerator blockTagGenerator = new BlockTagGenerator(generator, existingFileHelper);

		generator.addProvider(event.includeServer(), new BiomeTagGenerator(generator, existingFileHelper));
		generator.addProvider(event.includeClient(), new BlockModelAndStateGenerator(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), blockTagGenerator);
		generator.addProvider(event.includeServer(), new EntityTypeTagGenerator(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new GlobalLootModifierGenerator(generator));
		generator.addProvider(event.includeClient(), new ItemModelGenerator(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new ItemTagGenerator(generator, blockTagGenerator, existingFileHelper));
		generator.addProvider(event.includeServer(), new LootTableGenerator(generator));
		generator.addProvider(event.includeServer(), new RecipeGenerator(generator));
	}
}
