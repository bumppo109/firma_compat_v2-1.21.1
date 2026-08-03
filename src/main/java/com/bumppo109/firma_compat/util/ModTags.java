package com.bumppo109.firma_compat.util;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Fluid;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> PREVENT_INTERACTION = createTag("prevent_interaction");
        public static final TagKey<Block> TWIGS = createTag("twigs");
        public static final TagKey<Block> MAKES_ROCK_ANVIL = createTag("makes_rock_anvil");
        public static final TagKey<Block> CHISELED_BOOKSHELVES = createTag("chiseled_bookshelves");
        public static final TagKey<Block> MUD = createTag("mud");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> PREVENT_INTERACTION = createTag("prevent_interaction");
        //Dye
        public static final TagKey<Item> MAKES_WHITE_DYE = createTag("makes_dye/white");
        public static final TagKey<Item> MAKES_LIGHT_GRAY_DYE = createTag("makes_dye/light_gray_dye");
        public static final TagKey<Item> MAKES_GRAY_DYE = createTag("makes_dye/gray_dye");
        public static final TagKey<Item> MAKES_BLACK_DYE = createTag("makes_dye/black_dye");
        public static final TagKey<Item> MAKES_BROWN_DYE = createTag("makes_dye/brown_dye");
        public static final TagKey<Item> MAKES_RED_DYE = createTag("makes_dye/red_dye");
        public static final TagKey<Item> MAKES_ORANGE_DYE = createTag("makes_dye/orange_dye");
        public static final TagKey<Item> MAKES_YELLOW_DYE = createTag("makes_dye/yellow_dye");
        public static final TagKey<Item> MAKES_LIME_DYE = createTag("makes_dye/lime_dye");
        public static final TagKey<Item> MAKES_GREEN_DYE = createTag("makes_dye/green_dye");
        public static final TagKey<Item> MAKES_CYAN_DYE = createTag("makes_dye/cyan_dye");
        public static final TagKey<Item> MAKES_LIGHT_BLUE_DYE = createTag("makes_dye/light_blue_dye");
        public static final TagKey<Item> MAKES_BLUE_DYE = createTag("makes_dye/blue_dye");
        public static final TagKey<Item> MAKES_PURPLE_DYE = createTag("makes_dye/purple_dye");
        public static final TagKey<Item> MAKES_MAGENTA_DYE = createTag("makes_dye/magenta_dye");
        public static final TagKey<Item> MAKES_PINK_DYE = createTag("makes_dye/pink_dye");

        public static final TagKey<Item> TOOLS_NETHERITE = commonTag("tools/netherite");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }
    }

    public static class PlacedFeatures {

        public static final TagKey<PlacedFeature> TWIG_PATCHES = createTag("twig_patches");
        public static final TagKey<PlacedFeature> COMPAT_VEINS = createTag("compat_veins");

        private static TagKey<PlacedFeature> createTag(String name) {
            return TagKey.create(
                    Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name)
            );
        }
    }

    public static class Biomes {

        public static final TagKey<Biome> FORESTED_BIOMES = createTag("forested_biomes");
        public static final TagKey<Biome> IS_TFC_OVERWORLD = createTag("is_tfc_overworld");
        public static final TagKey<Biome> CAN_BE_DEEP_DARK = createTag("can_be_deep_dark");

        private static TagKey<Biome> createTag(String name) {
            return TagKey.create(Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name)
            );
        }
    }

    public static class Fluids {
        private static TagKey<Fluid> createTag(String name) {
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }

        public static final TagKey<Fluid> WATERLOGGING_WATER = createTag("waterlogging_water");

    }

    public static class Entities {
        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }

        public static final TagKey<EntityType<?>> REMOVE_VANILLA_MOBS = createTag("remove_vanilla_mobs");
        public static final TagKey<EntityType<?>> COMPAT_TFC_ENTITIES = createTag("compat_tfc_entities");
        public static final TagKey<EntityType<?>> CAN_SURVIVE_SALT_WATER = createTag("can_survive_salt_water");

    }

    private static TagKey<Item> commonTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
