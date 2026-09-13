package com.bumppo109.firma_compat.addon.rnr.modules;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class CompatRnRItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, FirmaCompat.MODID);

    public static final ModItems.ItemId GRAVEL_FILL = register("gravel_fill");
    public static final Map<CompatRock, ModItems.ItemId> FLAGSTONE = Helpers.mapOf(CompatRock.class, rock -> register(rock.getSerializedName() + "_flagstone"));
    public static final Map<CompatWood, ModItems.ItemId> SHINGLE = Helpers.mapOf(CompatWood.class, wood -> register(wood.name() + "_shingle"));

    private static ModItems.ItemId register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> ModItems.ItemId register(String name, Supplier<T> item)
    {
        return new ModItems.ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), item));
    }

    public record ItemId(DeferredHolder<Item, Item> holder) implements RegistryHolder<Item, Item>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get();
        }
    }
}
