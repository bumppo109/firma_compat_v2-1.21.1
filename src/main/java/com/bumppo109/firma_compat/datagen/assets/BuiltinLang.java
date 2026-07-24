package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.BlockAssets;
import com.bumppo109.firma_compat.block.BlockTextureSlot;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Locale;

public class BuiltinLang extends LanguageProvider {
    public BuiltinLang(PackOutput output) {
        super(output, FirmaCompat.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        add("firma_compat.creative_tab.firma_compat", "Firma Compat");

        ModBlocks.WOODS.forEach((compatWood, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                if (blockType.equals(CompatWood.BlockType.VERTICAL_SUPPORT)) {
                    add(blockId.get(), processIdString(compatWood.getSerializedName()) + " Support");
                } else if (blockType.equals(CompatWood.BlockType.HORIZONTAL_SUPPORT)) {
                    add(blockId.get(), processIdString(compatWood.getSerializedName()) + " Support");
                } else {
                    add(blockId.get(), getBlockDisplayName(blockId.get()));
                }
            });
        });
        ModItems.LUMBER.forEach((compatWood, itemId) -> {
            add(itemId.asItem(), getItemDisplayName(itemId.get()));
        });

        ModBlocks.ROCK_BLOCKS.forEach((rock, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                add(blockId.get(), getBlockDisplayName(blockId.get()));
            });
        });
        ModBlocks.ROCK_DECORATIONS.forEach((rock, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, decoration) -> {
                add(decoration.slab().get(), getBlockDisplayName(decoration.slab().get()));
                add(decoration.stair().get(), getBlockDisplayName(decoration.stair().get()));
                add(decoration.wall().get(), getBlockDisplayName(decoration.wall().get()));
            });
        });

        ModBlocks.TFC_ROCK_BLOCKS.forEach((rock, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                add(blockId.get(), getBlockDisplayName(blockId.get()));
            });
        });

        ModBlocks.AQUEDUCTS.forEach((compatRockSets, blockId) -> {
            add(blockId.get(), getBlockDisplayName(blockId.get()));
        });
        Block brickAqueduct = ModBlocks.BRICK_AQUEDUCT.get();
        Block prismarineBrickAqueduct = ModBlocks.PRISMARINE_BRICK_AQUEDUCT.get();
        Block quartzBrickAqueduct = ModBlocks.QUARTZ_BRICK_AQUEDUCT.get();
        Block redNetherBrickAqueduct = ModBlocks.RED_NETHER_BRICK_AQUEDUCT.get();
        add(brickAqueduct, getBlockDisplayName(brickAqueduct));
        add(prismarineBrickAqueduct, getBlockDisplayName(prismarineBrickAqueduct));
        add(quartzBrickAqueduct, getBlockDisplayName(quartzBrickAqueduct));
        add(redNetherBrickAqueduct, getBlockDisplayName(redNetherBrickAqueduct));
    }

    private String getBlockDisplayName(Block block) {
        if (block == null) {
            return "Unknown Block";
        }

        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (id == null || id.equals(BuiltInRegistries.BLOCK.getDefaultKey())) {
            return "Air";
        }

        String path = id.getPath();

        return processIdString(path);
    }

    private String getItemDisplayName(Item item) {
        if (item == null) {
            return "Unknown Item";
        }

        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null || id.equals(BuiltInRegistries.ITEM.getDefaultKey())) {
            return "Air";
        }

        String path = id.getPath();

        return processIdString(path);
    }

    private String processIdString(String string) {
        // Split on both "_" and "/" to handle folder-like paths nicely
        String[] parts = string.split("[_/]");

        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            // Capitalize first letter, lowercase the rest
            String word = Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase();
            sb.append(word).append(" ");
        }

        // Trim trailing space
        return sb.toString().trim();
    }
}
