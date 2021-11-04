package suszombification;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class SZTags {
	public static class Blocks {
		public static final IOptionalNamedTag<Block> ROTTEN_WOOL = tag("rotten_wool");
		public static final IOptionalNamedTag<Block> TROPHIES = tag("trophies");

		private static IOptionalNamedTag<Block> tag(String name) {
			return BlockTags.createOptional(new ResourceLocation(SuspiciousZombification.MODID, name));
		}
	}

	public static class Items {
		public static final IOptionalNamedTag<Item> ROTTEN_WOOL = tag("rotten_wool");

		private static IOptionalNamedTag<Item> tag(String name) {
			return ItemTags.createOptional(new ResourceLocation(SuspiciousZombification.MODID, name));
		}
	}

	public static class EntityTypes {
		public static final IOptionalNamedTag<EntityType<?>> AFFECTED_BY_ZOMBIES_GRACE = tag("affected_by_zombies_grace");

		private static IOptionalNamedTag<EntityType<?>> tag(String name) {
			return EntityTypeTags.createOptional(new ResourceLocation(SuspiciousZombification.MODID, name));
		}
	}
}
