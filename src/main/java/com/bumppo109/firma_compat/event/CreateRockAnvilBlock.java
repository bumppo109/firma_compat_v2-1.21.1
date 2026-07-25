package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class CreateRockAnvilBlock {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        ItemStack stack = event.getItemStack();
        if (!stack.is(TFCTags.Items.TOOLS_HAMMER)) return;

        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);

        if (!state.is(ModTags.Blocks.MAKES_ROCK_ANVIL)) return;

        if (!level.isClientSide()) {
            level.setBlock(pos, matchAnvil(state.getBlock()).defaultBlockState(), 3);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private static Block matchAnvil(Block block) {
        for (CompatRock rock : CompatRock.VALUES) {
            Block testBlock = rock.rockMaterial().raw().base().get();

            if (testBlock.equals(block)) {
                return ModBlocks.ROCK_ANVILS.get(rock).get();
            }
        }
        return block;
    }
}
