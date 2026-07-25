package com.bumppo109.firma_compat.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class IgniteUnlitBlockEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        Level level = event.getLevel();

        if (level.isClientSide()) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        // Block must have a LIT property
        if (!state.hasProperty(BlockStateProperties.LIT)) {
            return;
        }

        // Already lit
        if (state.getValue(BlockStateProperties.LIT)) {
            return;
        }

        var player = event.getEntity();

        InteractionHand eventHand = event.getHand();

        InteractionHand otherHand =
                eventHand == InteractionHand.MAIN_HAND
                        ? InteractionHand.OFF_HAND
                        : InteractionHand.MAIN_HAND;

        ItemStack eventHandStack = player.getItemInHand(eventHand);
        ItemStack otherHandStack = player.getItemInHand(otherHand);

        boolean eventHandIgniter = eventHandStack.is(Tags.Items.TOOLS_IGNITER);
        boolean otherHandIgniter = otherHandStack.is(Tags.Items.TOOLS_IGNITER);

        if (!eventHandIgniter && !otherHandIgniter) {
            return;
        }

        // Light the block
        level.setBlock(
                pos,
                state.setValue(BlockStateProperties.LIT, true),
                3
        );

        level.playSound(
                null,
                pos,
                SoundEvents.FLINTANDSTEEL_USE,
                player.getSoundSource(),
                1.0F,
                1.0F
        );

        if (player instanceof ServerPlayer serverPlayer) {

            ItemStack igniter =
                    eventHandIgniter ? eventHandStack : otherHandStack;

            if (igniter.isDamageableItem()) {
                igniter.hurtAndBreak(
                        1,
                        serverPlayer,
                        player.getEquipmentSlotForItem(igniter)
                );
            }
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}