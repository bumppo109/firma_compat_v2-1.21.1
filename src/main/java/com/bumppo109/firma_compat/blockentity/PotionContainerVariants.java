package com.bumppo109.firma_compat.blockentity;

import com.bumppo109.firma_compat.block.Glass;
import com.bumppo109.firma_compat.item.ModItems; // adjust if your item class name differs
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class PotionContainerVariants {

    private static final Map<Item, Item> TO_SPLASH = new HashMap<>();
    private static final Map<Item, Item> TO_LINGERING = new HashMap<>();

    private PotionContainerVariants() {}

    /**
     * Call once during common setup, after items exist.
     */
    public static void bootstrap() {
        for (Glass glass : Glass.values()) {
            Item regular = glass.getGlassBottle().get();
            if (regular == null) {
                continue;
            }

            Item splash = ModItems.SPLASH_POTIONS.get(glass).get();
            Item lingering = ModItems.LINGERING_POTIONS.get(glass).get();

            register(regular, splash, lingering);
        }
    }

    public static void register(Item regular, Item splash, Item lingering) {
        if (regular != null && splash != null) {
            TO_SPLASH.put(regular, splash);
        }
        if (regular != null && lingering != null) {
            TO_LINGERING.put(regular, lingering);
        }
        // vanilla-style: splash → lingering
        if (splash != null && lingering != null) {
            TO_LINGERING.put(splash, lingering);
        }
    }

    public static boolean canMakeSplash(ItemStack stack) {
        return !stack.isEmpty() && TO_SPLASH.containsKey(stack.getItem());
    }

    public static boolean canMakeLingering(ItemStack stack) {
        return !stack.isEmpty() && TO_LINGERING.containsKey(stack.getItem());
    }

    public static Optional<Item> getSplashItem(ItemStack stack) {
        return Optional.ofNullable(TO_SPLASH.get(stack.getItem()));
    }

    public static Optional<Item> getLingeringItem(ItemStack stack) {
        return Optional.ofNullable(TO_LINGERING.get(stack.getItem()));
    }
}