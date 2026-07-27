package com.bumppo109.firma_compat.addon.firmalife;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.eerussianguy.firmalife.common.blockentities.FLBlockEntities;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

import java.util.stream.Stream;

public class FLCompatEvents {
    @SubscribeEvent
    public static void addToBlockEntities(BlockEntityTypeAddBlocksEvent event)
{
        modifyBlockEntityType(
                FLBlockEntities.FOOD_SHELF.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_food_shelf")
                                    && !id.getPath().endsWith("_wine_shelf");
                        }),
                event
        );
        modifyBlockEntityType(
                FLBlockEntities.HANGER.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_hanger");
                        }),
                event
        );
        modifyBlockEntityType(
                FLBlockEntities.JARBNET.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_jarbnet");
                        }),
                event
        );
        modifyBlockEntityType(
                FLBlockEntities.KEG_SUB.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_keg_sub");
                        }),
                event
        );
        modifyBlockEntityType(
                FLBlockEntities.KEG.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_keg");
                        }),
                event
        );
        modifyBlockEntityType(
                FLBlockEntities.WINE_SHELF.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_wine_shelf")
                                    && !id.getPath().endsWith("_food_shelf");
                        }),
                event
        );
        modifyBlockEntityType(
                FLBlockEntities.STOMPING_BARREL.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_stomping_barrel");
                        }),
                event
        );
        modifyBlockEntityType(
                FLBlockEntities.BARREL_PRESS.get(),
                BuiltInRegistries.BLOCK.stream()
                        .filter(block -> {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                            return (id.getNamespace().equals(FirmaCompat.MODID) || id.getNamespace().equals("everycomp"))
                                    && id.getPath().endsWith("_barrel_press");
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
                FLCompatEvents.class.getResource(
                        "/builtin_resource_packs/excalibur_addon/pack.mcmeta"
                )
        );

        System.out.println(
                FLCompatEvents.class.getResource(
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
