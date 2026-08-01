package com.bumppo109.firma_compat.world.processor.engine;

import com.bumppo109.firma_compat.world.processor.RockResolver;


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

        ReplacementResolverRegistry.register(
                new RockResolver()
        );

        initialized = true;
    }
}