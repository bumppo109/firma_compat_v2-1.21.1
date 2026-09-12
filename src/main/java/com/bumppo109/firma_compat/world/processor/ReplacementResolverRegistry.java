package com.bumppo109.firma_compat.world.processor;

import java.util.EnumMap;
import java.util.Map;


/**
 * Holds all replacement resolvers.
 *
 * The registry is the bridge between ReplacementCategory
 * and the strongly typed ReplacementResolver implementation.
 */
public final class ReplacementResolverRegistry
{

    private static final Map<
            ReplacementCategory,
            ReplacementResolver<? extends ReplacementTarget>
            > RESOLVERS =
            new EnumMap<>(ReplacementCategory.class);



    private ReplacementResolverRegistry()
    {
    }



    /**
     * Registers a resolver for its category.
     */
    public static void register(
            ReplacementResolver<? extends ReplacementTarget> resolver
    )
    {
        ReplacementResolver<? extends ReplacementTarget> previous =
                RESOLVERS.put(
                        resolver.category(),
                        resolver
                );


        if (previous != null)
        {
            throw new IllegalStateException(
                    "Replacement resolver already registered for "
                            + resolver.category()
            );
        }
    }

    public static boolean contains(
            ReplacementCategory category
    )
    {
        return RESOLVERS.containsKey(category);
    }

    /**
     * Retrieves a resolver for a target type.
     *
     * The unchecked cast is required because Java cannot associate
     * enum values with their generic parameter types.
     */
    @SuppressWarnings("unchecked")
    public static <T extends ReplacementTarget>
    ReplacementResolver<T> get(
            ReplacementCategory category
    )
    {
        ReplacementResolver<? extends ReplacementTarget> resolver =
                RESOLVERS.get(category);


        if (resolver == null)
        {
            throw new IllegalStateException(
                    "No replacement resolver registered for "
                            + category
            );
        }


        return (ReplacementResolver<T>) resolver;
    }
}