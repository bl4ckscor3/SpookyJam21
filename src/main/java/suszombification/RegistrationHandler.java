package suszombification;

import java.util.List;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import suszombification.block.TrophyBlock;
import suszombification.entity.ZombifiedAnimal;
import suszombification.glm.CatMorningGiftModifier;
import suszombification.glm.NoDecomposingDropsModifier;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZItems;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
public class RegistrationHandler {
	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.CAT, SZEntityTypes.ZOMBIFIED_CAT.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.CHICKEN, SZEntityTypes.ZOMBIFIED_CHICKEN.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.COW, SZEntityTypes.ZOMBIFIED_COW.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.PIG, SZEntityTypes.ZOMBIFIED_PIG.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.SHEEP, SZEntityTypes.ZOMBIFIED_SHEEP.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.HORSE, EntityType.ZOMBIE_HORSE);
		});
	}

	@SubscribeEvent
	public static void registerItems(RegisterEvent event) {
		event.register(Keys.ITEMS, helper -> {
			//register block items from blocks
			for (RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
				Block block = ro.get();

				if (!(block instanceof TrophyBlock))
					helper.register(ro.getId().getPath(), new BlockItem(block, new Item.Properties()));
			}
		});
		event.register(Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, helper -> {
			helper.register("cat_morning_gift", CatMorningGiftModifier.CODEC.get());
			helper.register("no_decomposing_drops", NoDecomposingDropsModifier.CODEC.get());
		});
	}

	@SubscribeEvent
	public static void onCreativeModeTabBuildContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {
			event.acceptAll(List.of( //@formatter:off
					new ItemStack(SZBlocks.WHITE_ROTTEN_WOOl.get()),
					new ItemStack(SZBlocks.LIGHT_GRAY_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.GRAY_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.BLACK_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.BROWN_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.RED_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.ORANGE_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.YELLOW_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.LIME_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.GREEN_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.CYAN_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.LIGHT_BLUE_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.BLUE_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.PURPLE_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.MAGENTA_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.PINK_ROTTEN_WOOL.get())));
			//@formatter:on
		}
		else if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			for (RegistryObject<Item> ro : SZItems.ITEMS.getEntries()) {
				Item item = ro.get();

				if (item instanceof SpawnEggItem)
					event.accept(item);
			}
		}
	}
}
