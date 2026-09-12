package com.bumppo109.firma_compat.blockentity;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(
                    Registries.MENU,
                    FirmaCompat.MODID
            );

    public static final DeferredHolder<
            MenuType<?>,
            MenuType<FluidBrewingStandMenu>
            > FLUID_BREWING_STAND =
            MENUS.register(
                    "fluid_brewing_stand",
                    () -> new MenuType<>(
                            FluidBrewingStandMenu::new,
                            net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS
                    )
            );

    private ModMenus() {
    }
}
