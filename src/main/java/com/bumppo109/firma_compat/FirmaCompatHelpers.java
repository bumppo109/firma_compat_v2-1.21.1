package com.bumppo109.firma_compat;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.data.DataManager;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static net.dries007.tfc.util.Helpers.resourceLocation;

public class FirmaCompatHelpers {
    public static ResourceLocation modIdentifier(String name) {
        return resourceLocation(FirmaCompat.MODID, name);
    }

    public static ModelLayerLocation layerId(String name)
    {
        return layerId(name, "main");
    }

    /**
     * Creates {@link ModelLayerLocation} in the default manner
     */
    public static ModelLayerLocation layerId(String name, String part)
    {
        return new ModelLayerLocation(modIdentifier(name), part);
    }

    public static <T> void fakeDataManager(DataManager<T> manager, Map<String, T> values)
    {
        final Map<ResourceLocation, T> map = new HashMap<>();
        for (T value : manager.getValues())
        {
            map.put(manager.getId(value), value);
        }
        for (Map.Entry<String, T> entry : values.entrySet())
        {
            map.put(Helpers.identifier(entry.getKey()), entry.getValue());
        }
        manager.bindValues(map);
    }
}
