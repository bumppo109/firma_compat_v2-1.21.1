package com.bumppo109.firma_compat.event;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public class PlaceUnlitBlockEvent {

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Level level = (Level) event.getLevel();

        if (level.isClientSide()) {
            return;
        }

        BlockState state = event.getPlacedBlock();

        if (state.hasProperty(BlockStateProperties.LIT)
                && state.getValue(BlockStateProperties.LIT)) {

            BlockState unlit =
                    state.setValue(BlockStateProperties.LIT, false);

            level.setBlock(
                    event.getPos(),
                    unlit,
                    3
            );
        }
    }
}