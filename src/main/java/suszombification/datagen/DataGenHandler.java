package suszombification.datagen;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import suszombification.SuspiciousZombification;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
public class DataGenHandler {
	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper existingFileHelper = new ExistingFileHelper(Collections.EMPTY_LIST, Collections.EMPTY_SET, false, null, null);
		BlockTagGenerator blockTagGenerator = new BlockTagGenerator(output, lookupProvider, existingFileHelper);

		generator.addProvider(event.includeServer(), new BiomeTagGenerator(output, lookupProvider, existingFileHelper));
		generator.addProvider(event.includeClient(), new BlockModelAndStateGenerator(output, existingFileHelper));
		generator.addProvider(event.includeServer(), blockTagGenerator);
		generator.addProvider(event.includeServer(), new DamageTypeTagGenerator(output, lookupProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new EntityTypeTagGenerator(output, lookupProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new GlobalLootModifierGenerator(output));
		generator.addProvider(event.includeClient(), new ItemModelGenerator(output, existingFileHelper));
		generator.addProvider(event.includeServer(), new ItemTagGenerator(output, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
		generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(), List.of(new SubProviderEntry(BlockLootTableGenerator::new, LootContextParamSets.BLOCK), new SubProviderEntry(ChestLootTableGenerator::new, LootContextParamSets.CHEST), new SubProviderEntry(EntityLootTableGenerator::new, LootContextParamSets.ENTITY), new SubProviderEntry(GiftLootTableGenerator::new, LootContextParamSets.GIFT))));
		generator.addProvider(event.includeServer(), new RecipeGenerator(output, lookupProvider));
	}
}
