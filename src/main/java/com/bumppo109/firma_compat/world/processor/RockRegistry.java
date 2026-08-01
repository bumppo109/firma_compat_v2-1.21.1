package com.bumppo109.firma_compat.world.processor;

import net.dries007.tfc.common.blocks.rock.Rock;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public final class RockRegistry
{
    private static final Map<Block, Rock> RAW_LOOKUP = new HashMap<>();

    private RockRegistry()
    {
    }

    public static void bootstrap()
    {
        RAW_LOOKUP.clear();

        for (Rock rock : Rock.VALUES)
        {
            RAW_LOOKUP.put(
                    rock.getBlock(Rock.BlockType.RAW).get(),
                    rock
            );
        }
    }

    public static Rock fromRaw(Block raw)
    {
        Rock rock = RAW_LOOKUP.get(raw);

        if (rock == null)
        {
            throw new IllegalStateException(
                    "Unknown TFC raw rock block: " + raw
            );
        }

        return rock;
    }
}