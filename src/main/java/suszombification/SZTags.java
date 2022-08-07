package suszombification;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class SZTags {
	public static class Blocks {
		public static final TagKey<Block> ROTTEN_WOOL = tag("rotten_wool");
		public static final TagKey<Block> TROPHIES = tag("trophies");

		private static TagKey<Block> tag(String name) {
			return BlockTags.create(new ResourceLocation(SuspiciousZombification.MODID, name));
		}
	}

	public static class Biomes {
		public static final TagKey<Biome> HAS_ZOMBIE_COVE = tag("has_structure/zombie_cove");
		public static final TagKey<Biome> HAS_DESERT_ZOMBIE_COVE = tag("has_structure/desert_zombie_cove");

		private static TagKey<Biome> tag(String name) {
			return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(SuspiciousZombification.MODID, name));
		}
	}

	public static class Items {
		public static final TagKey<Item> ROTTEN_WOOL = tag("rotten_wool");

		private static TagKey<Item> tag(String name) {
			return ItemTags.create(new ResourceLocation(SuspiciousZombification.MODID, name));
		}
	}

	public static class EntityTypes {
		public static final TagKey<EntityType<?>> AFFECTED_BY_ZOMBIES_GRACE = tag("affected_by_zombies_grace");

		private static TagKey<EntityType<?>> tag(String name) {
			return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(SuspiciousZombification.MODID, name));
		}
	}
}
