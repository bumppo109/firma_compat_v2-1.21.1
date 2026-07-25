package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public class FarmlandHoeingEventHandler {

    @SubscribeEvent
    public static void onToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility() != ItemAbilities.HOE_TILL || event.isSimulated()) {
            return;
        }

        if (!event.getState().is(Blocks.DIRT) &&
                !event.getState().is(Blocks.GRASS_BLOCK) &&
                    !event.getState().is(Blocks.DIRT_PATH)) {
            return;
        }
        BlockState replacement = ModBlocks.COMPAT_FARMLAND.get().defaultBlockState();
        event.setFinalState(replacement);
    }
}
