package com.bumppo109.firma_compat.world.processor;

import com.bumppo109.firma_compat.world.processor.dirt.DirtResolver;
import com.bumppo109.firma_compat.world.processor.rock.RockResolver;
import com.bumppo109.firma_compat.world.processor.sand.SandResolver;
import com.bumppo109.firma_compat.world.processor.sandstone.SandstoneResolver;


public final class ReplacementBootstrap
{
    private static boolean initialized = false;


    private ReplacementBootstrap()
    {
    }


    public static void init()
    {
        if (initialized)
        {
            return;
        }

        ReplacementResolverRegistry.register(new RockResolver());
        ReplacementResolverRegistry.register(new DirtResolver());
        ReplacementResolverRegistry.register(new SandResolver());
        ReplacementResolverRegistry.register(new SandstoneResolver());

        initialized = true;
    }
}