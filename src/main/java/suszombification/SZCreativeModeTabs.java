package suszombification;

import java.util.List;
import java.util.Set;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import suszombification.block.TrophyBlock;
import suszombification.item.SuspiciousPumpkinPieItem;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZItems;

//@formatter:off
public class SZCreativeModeTabs {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SuspiciousZombification.MODID);
	public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()))
			.title(Component.translatable("itemGroup.suszombification"))
			.displayItems((displayParameters, output) -> {
				for (RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
					Block block = ro.get();

					if (!(block instanceof TrophyBlock))
						output.accept(block);
				}

				List<Item> ingredients = SuspiciousPumpkinPieItem.getAllDifferentIngredients();
				Set<ItemStack> differentPumpkinPies = ItemStackLinkedSet.createTypeAndTagSet();

				for (Item ingredient : ingredients) {
					ItemStack pumpkinPie = new ItemStack(SZItems.SUSPICIOUS_PUMPKIN_PIE.get());

					SuspiciousPumpkinPieItem.saveIngredient(pumpkinPie, new ItemStack(ingredient));
					differentPumpkinPies.add(pumpkinPie);
				}

				output.acceptAll(differentPumpkinPies);

				for (RegistryObject<Item> ro : SZItems.ITEMS.getEntries()) {
					Item item = ro.get();

					if (item != SZItems.SUSPICIOUS_PUMPKIN_PIE.get())
						output.accept(item);
				}
			}).build());
}
