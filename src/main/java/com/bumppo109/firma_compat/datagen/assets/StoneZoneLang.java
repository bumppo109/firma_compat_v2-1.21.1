package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.block.ModBlocks;
import net.mehvahdjukaar.stone_zone.StoneZone;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Locale;

public class StoneZoneLang extends LanguageProvider {
    public StoneZoneLang(PackOutput output) {
        super(output, StoneZone.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("block_type.tfc.loose", "%s Loose");
        add("block_type.tfc.mossy_loose", "Mossy %s Loose");
        add("item_type.tfc.brick", "%s Brick");
        add("block_type.tfc.hardened", "%s Hardened");
        add("block_type.tfc.spike", "%s Spike");
        add("block_type.tfc.rock_anchor", "Rope Anchor");
        add("block_type.tfc.cobble", "%s Cobble");
        add("block_type.tfc.mossy_cobble", "Mossy %s Cobble");
        add("block_type.tfc.hardened_cobble", "Hardened %s Cobble");
        add("block_type.tfc.mossy_hardened_cobble", "Mossy Hardened %s Cobble");

        add("block_type.tfc.flagstones", "%s Flagstones");
        add("block_type.tfc.flagstone_stairs", "%s Flagstone Stairs");
        add("block_type.tfc.flagstone_slab", "%s Flagstone Slab");
        add("block_type.tfc.cobbled_road", "%s Cobbled Road");
        add("block_type.tfc.cobbled_road_stairs", "%s Cobbled Road Stairs");
        add("block_type.tfc.cobbled_road_slab", "%s Cobbled Road Slab");
        add("block_type.tfc.sett_road", "%s Sett Road");
        add("block_type.tfc.sett_road_stairs", "%s Sett Road Stairs");
        add("block_type.tfc.sett_road_slab", "%s Sett Road Slab");
        add("item_type.tfc.flagstone", "%s Flagstone");

        //TODO - ore names not going through correctly
        ModBlocks.GRADED_ORES.forEach((rock, oreMapMap) -> {
            oreMapMap.forEach((ore, gradeIdMap) -> {
                gradeIdMap.forEach((grade, blockId) -> {
                    add(blockId.get(), getBlockDisplayName(blockId.get()));
                });
            });
        });

        ModBlocks.ORES.forEach((rock, oreIdMap) -> {
            oreIdMap.forEach((ore, blockId) -> {
                add(blockId.get(), getBlockDisplayName(blockId.get()));
            });
        });


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
