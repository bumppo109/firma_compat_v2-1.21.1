package com.bumppo109.firma_compat.event;

import net.dries007.tfc.common.blockentities.LampBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class FirmaLampProtectLightEvent {
    //ensures lamp only lights if there is fuel
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();

        if (!(level.getBlockEntity(event.getPos()) instanceof LampBlockEntity lamp)) {
            return;
        }

        ItemStack held = event.getItemStack();

        if (!held.is(Tags.Items.TOOLS_IGNITER)) {
            return;
        }

        if (lamp.getFuel() == null) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }
    }
}