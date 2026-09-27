package com.bumppo109.firma_compat.event;

import net.dries007.tfc.common.TFCTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class SoulFireInteractionEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        // Must be clicking Soul Fire
        if (!event.getLevel().getBlockState(event.getPos()).is(Blocks.SOUL_FIRE)) {
            return;
        }

        Player player = event.getEntity();
        ItemStack heldItem = player.getItemInHand(event.getHand());

        // Example input item
        if (!heldItem.is(TFCTags.Items.LAMPS)) {
            return;
        }

        if (!event.getLevel().isClientSide()) {
            // Consume one input
            if (!player.getAbilities().instabuild) {
                heldItem.shrink(1);
            }

            // Give output
            ItemStack output = new ItemStack(Items.SOUL_LANTERN);

            if (!player.getInventory().add(output)) {
                player.drop(output, false);
            }
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
