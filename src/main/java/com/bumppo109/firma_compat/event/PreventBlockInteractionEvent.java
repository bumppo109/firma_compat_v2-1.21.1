package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FirmaCompat.MODID)
public class PreventBlockInteractionEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        InteractionHand hand = event.getHand();
        BlockState state = level.getBlockState(pos);

        if (state.isAir()) {
            return;
        }

        // Prevent interaction with tagged blocks.
        if (state.is(ModTags.Blocks.PREVENT_INTERACTION)) {

            // Allow exactly one interaction:
            // Lighting an unlit campfire with an igniter.
            if (state.getBlock() instanceof CampfireBlock
                    && player.getItemInHand(hand).is(Tags.Items.TOOLS_IGNITER)
                    && !state.getValue(CampfireBlock.LIT)) {

                if (!level.isClientSide) {
                    level.setBlock(
                            pos,
                            state.setValue(CampfireBlock.LIT, true),
                            3
                    );

                    level.playSound(
                            null,
                            pos,
                            SoundEvents.FLINTANDSTEEL_USE,
                            SoundSource.BLOCKS,
                            1.0F,
                            level.random.nextFloat() * 0.4F + 0.8F
                    );
                }

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                return;
            }

            // Block every other interaction (including placing food on campfires).
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);

            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.literal("For villager use only"),
                        true
                );

                level.playSound(
                        null,
                        pos,
                        SoundEvents.VILLAGER_NO,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
            }

            return;
        }

        // Prevent opening vanilla barrels.
        if (state.is(Blocks.BARREL)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);

            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.literal("Try breaking it"),
                        true
                );
            }
        }
    }
}