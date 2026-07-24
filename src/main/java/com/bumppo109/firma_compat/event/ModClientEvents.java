package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

import java.util.stream.Stream;

public class ModClientEvents {
    // This is where we add new wood blocks to existing TFC block entity types
    @SubscribeEvent
    public static void addToBlockEntities(BlockEntityTypeAddBlocksEvent event)
    {
        //Wood Good Module
        modifyBlockEntityType(
                TFCBlockEntities.CRATE.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_crate");
                        }),
                event
        );
        modifyBlockEntityType(
                TFCBlockEntities.TOOL_RACK.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_tool_rack");
                        }),
                event
        );
        modifyBlockEntityType(
                TFCBlockEntities.LOOM.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_loom");
                        }),
                event
        );
        modifyBlockEntityType(
                TFCBlockEntities.SLUICE.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_sluice");
                        }),
                event
        );
        modifyBlockEntityType(
                TFCBlockEntities.BARREL.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                                if (id == null) return false;
                                String path = id.getPath();
                                return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                        && path.endsWith("_barrel")
                                        && !path.endsWith("_stomping_barrel")
                                        && !path.equals("compat_barrel");
                        }),
                event
        );

        modifyBlockEntityType(
                TFCBlockEntities.AXLE.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            if (id == null) return false;
                            String path = id.getPath();
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && path.endsWith("_axle")
                                    && !path.endsWith("_bladed_axle")      // exclude bladed
                                    && !path.endsWith("_encased_axle");    // exclude encased
                        }),
                event
        );

        modifyBlockEntityType(
                TFCBlockEntities.BLADED_AXLE.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            if (id == null) return false;
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_bladed_axle");
                        }),
                event
        );

        modifyBlockEntityType(
                TFCBlockEntities.WATER_WHEEL.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            String path = id.getPath();
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && path.endsWith("_water_wheel");
                        }),
                event
        );

        modifyBlockEntityType(
                TFCBlockEntities.WINDMILL.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_windmill");
                        }),
                event
        );
        modifyBlockEntityType(
                TFCBlockEntities.CLUTCH.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_clutch");
                        }),
                event
        );
        modifyBlockEntityType(
                TFCBlockEntities.GEAR_BOX.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_gear_box");
                        }),
                event
        );
        modifyBlockEntityType(
                TFCBlockEntities.SHELF.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                                if (id == null) return false;
                                String path = id.getPath();
                                return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                        && path.endsWith("_shelf")
                                        && !path.endsWith("_wine_shelf")
                                        && !path.endsWith("_food_shelf");
                        }),
                event
        );
        modifyBlockEntityType(
                TFCBlockEntities.ENCASED_AXLE.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_encased_axle");
                        }),
                event
        );
    }

    private static void modifyWood(BlockEntityType<?> type, CompatWood.BlockType blockType, BlockEntityTypeAddBlocksEvent event)
    {
        modifyBlockEntityType(type, ModBlocks.WOODS.values().stream().map(map -> map.get(blockType).get()), event);
    }

    private static void modifyBlockEntityType(BlockEntityType<?> type, Stream<Block> extraBlocks, BlockEntityTypeAddBlocksEvent event)
    {
        extraBlocks.forEach(
                (block -> event.modify(type, block))
        );
    }

    private static void modifyBlockEntityType(BlockEntityType<?> type, Block extraBlock, BlockEntityTypeAddBlocksEvent event)
    {
        event.modify(type, extraBlock);
    }

    @SubscribeEvent
    public static void addResourcePacks(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) {
            return;
        }

        System.out.println(
                ModClientEvents.class.getResource(
                        "/builtin_resource_packs/excalibur_addon/pack.mcmeta"
                )
        );

        System.out.println(
                ModClientEvents.class.getResource(
                        "/builtin_resource_packs/vexxed_visuals_addon/pack.mcmeta"
                )
        );

        ResourceLocation excaliburAddon = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"builtin_resource_packs/excalibur_addon");
        ResourceLocation vexxedAddon = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"builtin_resource_packs/vexxed_visuals_addon");

        event.addPackFinders(excaliburAddon, PackType.CLIENT_RESOURCES,
                Component.literal("Excalibur Support"),
                PackSource.DEFAULT,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(vexxedAddon, PackType.CLIENT_RESOURCES,
                Component.literal("Vexxed Visuals Support"),
                PackSource.DEFAULT,
                false,
                Pack.Position.TOP
        );
    }
}
