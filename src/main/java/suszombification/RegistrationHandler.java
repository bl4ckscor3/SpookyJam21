package suszombification;

import java.util.List;
import java.util.Set;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.event.CreativeModeTabEvent;
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
import suszombification.item.SuspiciousPumpkinPieItem;
import suszombification.misc.CurseGivenFunction;
import suszombification.misc.SuspiciousPumpkinPieRecipe;
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
		event.register(Registries.LOOT_FUNCTION_TYPE, helper -> helper.register(new ResourceLocation(SuspiciousZombification.MODID, "curse_given"), new LootItemFunctionType(new CurseGivenFunction.Serializer())));
		event.register(Registries.RECIPE_SERIALIZER, helper -> helper.register(new ResourceLocation(SuspiciousZombification.MODID, "suspicious_pumpkin_pie"), new SimpleCraftingRecipeSerializer<>(SuspiciousPumpkinPieRecipe::new)));
	}

	@SubscribeEvent
	public static void onCreativeModeTabRegister(CreativeModeTabEvent.Register event) {
		//@formatter:off
		SuspiciousZombification.tab = event.registerCreativeModeTab(new ResourceLocation(SuspiciousZombification.MODID, "tab"), builder -> builder
				.icon(() -> new ItemStack(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()))
				.title(Component.translatable("itemGroup.suszombification"))
				.displayItems((features, output, hasPermissions) -> {
					for (RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
						Block block = ro.get();

						if (!(block instanceof TrophyBlock))
							output.accept(block);
					}

					List<Item> ingredients = SuspiciousPumpkinPieItem.getAllDifferentIngredients();
					Set<ItemStack> differentPumpkinPies = ItemStackLinkedSet.createTypeAndTagSet();

					for(Item ingredient : ingredients) {
						ItemStack pumpkinPie = new ItemStack(SZItems.SUSPICIOUS_PUMPKIN_PIE.get());
						SuspiciousPumpkinPieItem.saveIngredient(pumpkinPie, new ItemStack(ingredient));
						differentPumpkinPies.add(pumpkinPie);
					}

					output.acceptAll(differentPumpkinPies);

					for (RegistryObject<Item> ro : SZItems.ITEMS.getEntries()) {
						Item item = ro.get();

						if (item != SZItems.SUSPICIOUS_PUMPKIN_PIE.get())
							output.accept(ro.get());
					}
				}));
		//@formatter:on
	}
}
