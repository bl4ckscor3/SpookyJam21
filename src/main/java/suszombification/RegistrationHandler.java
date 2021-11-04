package suszombification;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.functions.LootFunctionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import suszombification.block.TrophyBlock;
import suszombification.entity.ZombifiedAnimal;
import suszombification.misc.CatMorningGiftModifier;
import suszombification.misc.CurseGivenFunction;
import suszombification.misc.NoDecomposingDropsModifier;
import suszombification.misc.SuspiciousPumpkinPieRecipe;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
public class RegistrationHandler {
	public static final LootFunctionType CURSE_GIVEN_LOOT_FUNCTION = LootFunctionManager.register(SuspiciousZombification.MODID + ":curse_given", new CurseGivenFunction.Serializer());

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SZStructures.setup();
			SZConfiguredStructures.setup();
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.CHICKEN, SZEntityTypes.ZOMBIFIED_CHICKEN.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.COW, SZEntityTypes.ZOMBIFIED_COW.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.PIG, SZEntityTypes.ZOMBIFIED_PIG.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.SHEEP, SZEntityTypes.ZOMBIFIED_SHEEP.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.HORSE, EntityType.ZOMBIE_HORSE);
		});
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		//register block items from blocks
		for(RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
			Block block = ro.get();

			if(!(block instanceof TrophyBlock))
				event.getRegistry().register(new BlockItem(block, new Item.Properties().tab(SuspiciousZombification.TAB)).setRegistryName(block.getRegistryName()));
		}
	}

	@SubscribeEvent
	public static void registerRecipeSerializer(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		event.getRegistry().register(new SpecialRecipeSerializer<>(SuspiciousPumpkinPieRecipe::new).setRegistryName(new ResourceLocation(SuspiciousZombification.MODID, "suspicious_pumpkin_pie")));
	}

	@SubscribeEvent
	public static void registerGlobalLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
		event.getRegistry().register(new CatMorningGiftModifier.Serializer().setRegistryName(new ResourceLocation(SuspiciousZombification.MODID, "cat_morning_gift")));
		event.getRegistry().register(new NoDecomposingDropsModifier.Serializer().setRegistryName(new ResourceLocation(SuspiciousZombification.MODID, "no_decomposing_drops")));
	}
}
