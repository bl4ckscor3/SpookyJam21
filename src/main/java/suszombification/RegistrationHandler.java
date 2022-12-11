package suszombification;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
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
import suszombification.misc.CurseGivenFunction;
import suszombification.misc.SuspiciousPumpkinPieRecipe;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZEntityTypes;

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
}
