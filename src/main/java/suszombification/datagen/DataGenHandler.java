package suszombification.datagen;

import java.util.Collections;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import suszombification.SuspiciousZombification;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
public class DataGenHandler {
	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = new ExistingFileHelper(Collections.EMPTY_LIST, Collections.EMPTY_SET, false, null, null);

		generator.addProvider(new ItemModelGenerator(generator, existingFileHelper));
		generator.addProvider(new LootTableGenerator(generator));
		generator.addProvider(new RecipeGenerator(generator));
	}
}
