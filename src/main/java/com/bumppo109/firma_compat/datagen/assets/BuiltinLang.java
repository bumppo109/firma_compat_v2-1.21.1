package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addon.firmalife.modules.CompatFLBlocks;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnRBlocks;
import com.bumppo109.firma_compat.addon.rnr.modules.CompatRnRItems;
import com.bumppo109.firma_compat.block.*;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Locale;

import static com.bumppo109.firma_compat.block.ModBlocks.GRADED_ORES;
import static com.bumppo109.firma_compat.block.ModBlocks.ORES;

public class BuiltinLang extends LanguageProvider {
    public BuiltinLang(PackOutput output) {
        super(output, FirmaCompat.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        add("firma_compat.creative_tab.firma_compat", "Firma Compat");

        add("fluid.firma_compat.metal.netherite", "Molten Netherite");
        add("fluid.firma_compat.metal.poor_netherite", "Molten Poor Netherite");

        //Gems Realm Compat
        add("item_type.firma_compat.rod", "%s Rod");
        add("item_type.firma_compat.double_ingot", "%s Double Ingot");
        add("item_type.firma_compat.sheet", "%s Sheet");
        add("item_type.firma_compat.double_sheet", "%s Double Sheet");
        add("item_type.firma_compat.pickaxe_head", "%s Pickaxe Head");
        add("item_type.firma_compat.axe_head", "%s Axe Head");
        add("item_type.firma_compat.shovel_head", "%s Shovel Head");
        add("item_type.firma_compat.hoe_head", "%s Hoe Head");
        add("item_type.firma_compat.sword_blade", "%s Sword Blade");
        add("item_type.firma_compat.unfinished_helmet", "%s Unfinished Helmet");
        add("item_type.firma_compat.unfinished_chestplate", "%s Unfinished Chestplate");
        add("item_type.firma_compat.unfinished_leggings", "%s Unfinished Leggings");
        add("item_type.firma_compat.unfinished_boots", "%s Unfinished Boots");

        //Wood Good Compat
        add("item_type.firma_compat.lumber", "%s Lumber");
        add("block_type.firma_compat.twig", "%s Twig");
        add("block_type.firma_compat.support", "%s Support");
        add("block_type.firma_compat.vertical_support", "%s Support");
        add("block_type.firma_compat.horizontal_support", "%s Support");
        add("block_type.firma_compat.log_fence", "%s Log Fence");
        add("block_type.firma_compat.tool_rack", "%s Tool Rack");
        add("block_type.firma_compat.loom", "%s Loom");
        add("block_type.firma_compat.sluice", "%s Sluice");
        add("block_type.firma_compat.barrel", "%s Barrel");
        add("block_type.firma_compat.scribing_table", "%s Scribing Table");
        add("block_type.firma_compat.sewing_table", "%s Sewing Table");
        add("block_type.firma_compat.shelf", "%s Shelf");
        add("block_type.firma_compat.axle", "%s Axle");
        add("block_type.firma_compat.bladed_axle", "%s Bladed Axle");
        add("block_type.firma_compat.encased_axle", "%s Encased Axle");
        add("block_type.firma_compat.clutch", "%s Clutch");
        add("block_type.firma_compat.gear_box", "%s Gear Box");
        add("block_type.firma_compat.windmill", "%s Windmill");
        add("block_type.firma_compat.water_wheel", "%s Water Wheel");
        add("block_type.firma_compat.crate", "%s Crate");

        add("block_type.firma_compat.keg", "%s Keg");
        add("block_type.firma_compat.food_shelf", "%s Food Shelf");
        add("block_type.firma_compat.wine_shelf", "%s Wine Shelf");
        add("block_type.firma_compat.hanger", "%s Hanger");
        add("block_type.firma_compat.jarbnet", "%s Jarbnet");
        add("block_type.firma_compat.stomping_barrel", "%s Stomping Barrel");
        add("block_type.firma_compat.barrel_press", "%s Barrel Press");

        add("block_type.firma_compat.shingles", "%s Shingles");
        add("block_type.firma_compat.shingles_stairs", "%s Shingle Stairs");
        add("block_type.firma_compat.shingles_slab", "%s Shingle Slab");

    //Wood
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

    //Rock
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
        ModItems.BRICK.forEach((rock, itemId) -> add(itemId.get(), getItemDisplayName(itemId.get())));

        add(ModItems.QUARTZ_BRICK.get(), getItemDisplayName(ModItems.QUARTZ_BRICK.get()));
        add(ModItems.PRISMARINE_BRICK.get(), getItemDisplayName(ModItems.PRISMARINE_BRICK.get()));

        ModBlocks.TFC_ROCK_BLOCKS.forEach((rock, blockTypeIdMap) -> {
            blockTypeIdMap.forEach((blockType, blockId) -> {
                add(blockId.get(), getTFCname(blockId.get()));
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

        ORES.forEach((rock, oreMap) ->
                oreMap.forEach((ore, blockId) ->
                        add(blockId.get(), getBlockDisplayName(blockId.get()))
                )
        );

        GRADED_ORES.forEach((rock, oreMap) ->
                oreMap.forEach((ore, gradeMap) ->
                        gradeMap.forEach((grade, blockId) ->
                                add(blockId.get(), getBlockDisplayName(blockId.get()))
                        )
                )
        );

        ModBlocks.ROCK_ANVILS.forEach((rock, blockId) -> {
            add(blockId.get(), getBlockDisplayName(blockId.get()));
        });

    //Metal
        add(ModItems.SCRAP_NETHERITE_INGOT.get(), getItemDisplayName(ModItems.SCRAP_NETHERITE_INGOT.get()));
        ModItems.METAL_ITEMS.forEach((compatMetal, itemTypeItemIdMap) -> {
            if (compatMetal.isDummy()) return;
            itemTypeItemIdMap.forEach((itemType, itemId) -> {
                add(itemId.get(), getItemDisplayName(itemId.get()));
            });
        });

        ModBlocks.COMPAT_LANTERNS.forEach((metal, lampBlockId) -> {
            add(lampBlockId.get(), getBlockDisplayName(lampBlockId.get()));
        });
        add(ModBlocks.LANTERN.get(), getBlockDisplayName(ModBlocks.LANTERN.get()));

    //Earthen
        add(ModBlocks.CLAY_DIRT.get(), getBlockDisplayName(ModBlocks.CLAY_DIRT.get()));
        add(ModBlocks.CLAY_GRASS_BLOCK.get(), getBlockDisplayName(ModBlocks.CLAY_GRASS_BLOCK.get()));
        add(ModBlocks.CLAY_PODZOL.get(), getBlockDisplayName(ModBlocks.CLAY_PODZOL.get()));
        add(ModBlocks.KAOLIN_CLAY_DIRT.get(), getBlockDisplayName(ModBlocks.KAOLIN_CLAY_DIRT.get()));
        add(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get(), getBlockDisplayName(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get()));
        add(ModBlocks.KAOLIN_CLAY_PODZOL.get(), getBlockDisplayName(ModBlocks.KAOLIN_CLAY_PODZOL.get()));
        add(ModBlocks.COMPAT_FARMLAND.get(), getBlockDisplayName(ModBlocks.COMPAT_FARMLAND.get()));

        add(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get(), getBlockDisplayName(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get()));
        add(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get(), getBlockDisplayName(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get()));
        add(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get(), getBlockDisplayName(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get()));
        add(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get(), getBlockDisplayName(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get()));

        add(ModBlocks.DRYING_MUD_BRICK.get(), getBlockDisplayName(ModBlocks.DRYING_MUD_BRICK.get()));
        add(ModItems.MUD_BRICK.get(), getItemDisplayName(ModItems.MUD_BRICK.get()));

        add(ModItems.UNFIRED_POT.get(), getItemDisplayName(ModItems.UNFIRED_POT.get()));

        ModBlocks.TFC_SUSPICIOUS_GRAVEL.forEach((rock, brushableBlockId) -> {
            add(brushableBlockId.get(), getTFCname(brushableBlockId.get()));
        });
        ModBlocks.TFC_SUSPICIOUS_SAND.forEach((sand, brushableBlockId) -> {
            add(brushableBlockId.get(), getTFCname(brushableBlockId.get()));
        });
        add(ModBlocks.SUSPICIOUS_RED_SAND.get(), getBlockDisplayName(ModBlocks.SUSPICIOUS_RED_SAND.get()));

        //Misc
        add(ModBlocks.COMPAT_CHEST.get(), getBlockDisplayName(ModBlocks.COMPAT_CHEST.get()));
        add(ModBlocks.COMPAT_TRAPPED_CHEST.get(), getBlockDisplayName(ModBlocks.COMPAT_TRAPPED_CHEST.get()));
        add(ModItems.COMPAT_CHEST_MINECART.get(), getItemDisplayName(ModItems.COMPAT_CHEST_MINECART.get()));


    // =============== Firmalife ================
        for (CompatWood wood : CompatWood.VALUES) {
            Block foodShelfBlock = CompatFLBlocks.FOOD_SHELVES.get(wood).get();
            Block hangerBlock = CompatFLBlocks.HANGERS.get(wood).get();
            Block jarbnetBlock = CompatFLBlocks.JARBNETS.get(wood).get();
            Block kegBlock = CompatFLBlocks.KEGS.get(wood).get();
            Block kegSubBlock = CompatFLBlocks.KEG_SUBS.get(wood).get();
            Block stompBarrelBlock = CompatFLBlocks.STOMPING_BARRELS.get(wood).get();
            Block barrelPressBlock = CompatFLBlocks.BARREL_PRESSES.get(wood).get();
            Block wineShelfBlock = CompatFLBlocks.WINE_SHELVES.get(wood).get();

            add(foodShelfBlock, getBlockDisplayName(foodShelfBlock));
            add(hangerBlock, getBlockDisplayName(hangerBlock));
            add(jarbnetBlock, getBlockDisplayName(jarbnetBlock));
            add(kegBlock, getBlockDisplayName(kegBlock));
            add(kegSubBlock, getBlockDisplayName(kegBlock));
            add(stompBarrelBlock, getBlockDisplayName(stompBarrelBlock));
            add(barrelPressBlock, getBlockDisplayName(barrelPressBlock));
            add(wineShelfBlock, getBlockDisplayName(wineShelfBlock));
        }

        CompatFLBlocks.CHROMITE_ORES.forEach((rock, gradeIdMap) -> {
            gradeIdMap.forEach((grade, blockId) -> {
                add(blockId.get(), getBlockDisplayName(blockId.get()));
            });
        });

    // =============== Firmalife ================
        CompatRnRBlocks.ROCK_BLOCKS.forEach((rock, compatRnRIdMap) -> {
            compatRnRIdMap.forEach((compatRnR, blockId) -> add(blockId.get(), getBlockDisplayName(blockId.get())));
        });
        CompatRnRBlocks.ROCK_STAIRS.forEach((rock, compatRnRIdMap) -> {
            compatRnRIdMap.forEach((compatRnR, blockId) -> add(blockId.get(), getBlockDisplayName(blockId.get())));
        });
        CompatRnRBlocks.ROCK_SLABS.forEach((rock, compatRnRIdMap) -> {
            compatRnRIdMap.forEach((compatRnR, blockId) -> add(blockId.get(), getBlockDisplayName(blockId.get())));
        });
        CompatRnRItems.FLAGSTONE.forEach((compatWood, itemId) -> add(itemId.get(), getItemDisplayName(itemId.get())));
        CompatRnRBlocks.WOOD_SHINGLE_ROOFS.forEach((compatWood, blockId) -> add(blockId.get(), getBlockDisplayName(blockId.get())));
        CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.forEach((compatWood, blockId) -> add(blockId.get(), getBlockDisplayName(blockId.get())));
        CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.forEach((compatWood, blockId) -> add(blockId.get(), getBlockDisplayName(blockId.get())));
        CompatRnRItems.SHINGLE.forEach((compatWood, itemId) -> add(itemId.get(), getItemDisplayName(itemId.get())));

        add(CompatRnRItems.GRAVEL_FILL.get(), getItemDisplayName(CompatRnRItems.GRAVEL_FILL.get()));
        add(CompatRnRBlocks.TAMPED_DIRT.get(), getBlockDisplayName(CompatRnRBlocks.TAMPED_DIRT.get()));
        add(CompatRnRBlocks.TAMPED_MUD.get(), getBlockDisplayName(CompatRnRBlocks.TAMPED_MUD.get()));
        add(CompatRnRBlocks.GRAVEL_ROAD.get(), getBlockDisplayName(CompatRnRBlocks.GRAVEL_ROAD.get()));
        add(CompatRnRBlocks.GRAVEL_ROAD_STAIRS.get(), getBlockDisplayName(CompatRnRBlocks.GRAVEL_ROAD_STAIRS.get()));
        add(CompatRnRBlocks.GRAVEL_ROAD_SLAB.get(), getBlockDisplayName(CompatRnRBlocks.GRAVEL_ROAD_SLAB.get()));
        add(CompatRnRBlocks.OVER_HEIGHT_GRAVEL.get(), getBlockDisplayName(CompatRnRBlocks.OVER_HEIGHT_GRAVEL.get()));
        add(CompatRnRBlocks.MACADAM_ROAD.get(), getBlockDisplayName(CompatRnRBlocks.MACADAM_ROAD.get()));
        add(CompatRnRBlocks.MACADAM_ROAD_STAIRS.get(), getBlockDisplayName(CompatRnRBlocks.MACADAM_ROAD_STAIRS.get()));
        add(CompatRnRBlocks.MACADAM_ROAD_SLAB.get(), getBlockDisplayName(CompatRnRBlocks.MACADAM_ROAD_SLAB.get()));
    }

    //for "tfc_" compat blocks
    private String getTFCname(Block block) {
        if (block == null) {
            return "Unknown Block";
        }

        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (id == null || id.equals(BuiltInRegistries.BLOCK.getDefaultKey())) {
            return "Air";
        }

        String path = id.getPath();

        if (path.startsWith("tfc_")) {
            path = path.substring(4);
        }

        return processIdString(path);
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
