package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
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
        add("block_type.firma_compat.loose", "%s Loose");
        add("block_type.firma_compat.mossy_loose", "Mossy %s Loose");
        add("block_type.firma_compat.hardened", "%s Hardened");
        add("block_type.firma_compat.spike", "%s Spike");
        add("block_type.firma_compat.brick_aqueduct", "%s Brick Aqueduct");
        add("item_type.firma_compat.brick", "%s Brick");

        add("block_type.firma_compat.cobble", "%s Cobble");
        add("block_type.firma_compat.hardened_cobble", "Hardened %s Cobble");
        add("block_type.firma_compat.mossy_cobble", "Mossy %s Cobble");
        add("block_type.firma_compat.mossy_hardened_cobble", "Mossy Hardened %s Cobble");

        //Firmalife
        add("block_type.firma_compat.poor_chromite_ore", "Poor %s Chromite Ore");
        add("block_type.firma_compat.normal_chromite_ore", "Normal %s Chromite Ore");
        add("block_type.firma_compat.rich_chromite_ore", "Rich %s Chromite Ore");

        //RnR
        add("block_type.firma_compat.flagstones", "%s Flagstones");
        add("block_type.firma_compat.flagstones_stairs", "%s Flagstone Stairs");
        add("block_type.firma_compat.flagstones_slab", "%s Flagstone Slab");
        add("block_type.firma_compat.cobbled_road", "%s Cobbled Road");
        add("block_type.firma_compat.cobbled_road_stairs", "%s Cobbled Road Stairs");
        add("block_type.firma_compat.cobbled_road_slab", "%s Cobbled Road Slab");
        add("block_type.firma_compat.sett_road", "%s Sett Road");
        add("block_type.firma_compat.sett_road_stairs", "%s Sett Road Stairs");
        add("block_type.firma_compat.sett_road_slab", "%s Sett Road Slab");
        add("item_type.firma_compat.flagstone", "%s Flagstone");

        add("item_type.firma_compat.shingle", "%s Shingle");
        add("item_type.firma_compat.shingles", "%s Shingle");
        add("item_type.firma_compat.shingle_stair", "%s Shingle Stairs");
        add("item_type.firma_compat.shingle_slab", "%s Shingle Slab");

        for(Ore ore : Ore.values()) {
            if (ModBlocks.skippedOre(ore)) continue;
            if (ore.isGraded()) {
                for (Ore.Grade grade : Ore.Grade.values()) {
                    String gradeStr = switch (grade) {
                        case POOR -> "Poor";
                        case NORMAL -> "Normal";
                        case RICH -> "Rich";
                    };
                    add("block_type.firma_compat." + grade.name().toLowerCase(Locale.ROOT) + "_" + ore.name().toLowerCase(Locale.ROOT) + "_ore",
                            gradeStr + " %s " + processIdString(ore.name().toLowerCase(Locale.ROOT)) + " Ore");
                }
            } else {
                add("block_type.firma_compat." + ore.name().toLowerCase(Locale.ROOT) + "_ore",
                        "%s " + processIdString(ore.name().toLowerCase(Locale.ROOT)) + " Ore");
            }
        }

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
