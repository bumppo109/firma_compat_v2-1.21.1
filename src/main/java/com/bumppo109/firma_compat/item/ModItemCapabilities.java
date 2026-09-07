package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.common.component.block.Barrel;
import net.dries007.tfc.common.component.fluid.FluidContainerHandler;
import net.dries007.tfc.common.items.LampBlockItem;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static net.dries007.tfc.common.capabilities.ItemCapabilities.FLUID;

public class ModItemCapabilities {
    public static void register(RegisterCapabilitiesEvent event)
    {

        //Barrels
        final ItemLike[] compatBarrels = BuiltInRegistries.BLOCK.stream()
                .filter(block -> {
                    ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                    return id.getNamespace().equals(FirmaCompat.MODID)
                            && id.getPath().endsWith("_barrel");
                })
                .toArray(ItemLike[]::new);

        event.registerItem(FLUID, (stack, context) -> new Barrel(stack), compatBarrels);

        //Lanterns
        for(Metal metal : Metal.values()) {
            if(metal.allParts()) {
                event.registerItem(
                        Capabilities.FluidHandler.ITEM,
                        (stack, ctx) -> new FluidContainerHandler(
                                stack,
                                ((LampBlockItem) stack.getItem()).containerInfo()
                        ),
                        ModBlocks.COMPAT_LANTERNS.get(metal).get().asItem()
                );
            }
        }
        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new FluidContainerHandler(
                        stack,
                        ((LampBlockItem) stack.getItem()).containerInfo()
                ),
                ModBlocks.LANTERN.get().asItem()
        );
    }
}