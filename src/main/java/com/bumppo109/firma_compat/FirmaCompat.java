package com.bumppo109.firma_compat;

import com.bumppo109.firma_compat.block.BlockAssets;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.data.ModDataComponents;
import com.bumppo109.firma_compat.event.ModClientEvents;
import com.bumppo109.firma_compat.addon.ModCompatHandler;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModCreativeModeTab;
import com.bumppo109.firma_compat.item.ModItemCapabilities;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.loot.ModLootFunctions;
import com.bumppo109.firma_compat.loot.loot_modifiers.ModLootModifiers;
import com.bumppo109.firma_compat.world.climate.ModClimateModels;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(FirmaCompat.MODID)
public class FirmaCompat {
    public static final String MODID = "firma_compat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean isFirmalifeLoaded = false;
    public static boolean isRnRLoaded = false;

    public static boolean isTreePhysicsLoaded = false;
    public static boolean isEclipticLoaded = false;
    public static boolean isSereneLoaded = false;
    public static boolean isLSOLoaded = false;

    public static boolean isWoodGoodLoaded = false;
    public static boolean isStoneZoneLoaded = false;
    public static boolean isGemsRealmLoaded = false;

    public FirmaCompat(IEventBus modEventBus, ModContainer modContainer) {
        this.modIntegration();

        modEventBus.addListener(ModClientEvents::addToBlockEntities);
        modEventBus.addListener(ModClientEvents::addResourcePacks);

        ModDataComponents.COMPONENTS.register(modEventBus);
        ModLootFunctions.FUNCTIONS.register(modEventBus);
        ModFluids.FLUID_TYPES.register(modEventBus);
        ModFluids.FLUID.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeModeTab.CREATIVE_TABS.register(modEventBus);
        ModLootModifiers.register(modEventBus);

        ModClimateModels.TYPES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        ModCompatHandler.registerEveryCompatModules();
        ModCompatHandler.registerAddon(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        FirmaCompatConfig.register(modContainer);

        if (FMLEnvironment.dist.isClient()) {
            //RegHelper.registerDynamicResourceProvider(ModClientDynamicResources.INSTANCE);
            modEventBus.addListener(ModItemCapabilities::register);
            modEventBus.addListener(FirmaCompatClient::registerExtensions);
        }
    }

    private void modIntegration() {
        isTreePhysicsLoaded = ModList.get().isLoaded("treephysics");
        isEclipticLoaded = ModList.get().isLoaded("eclipticseasons");
        isSereneLoaded = ModList.get().isLoaded("sereneseasons");
        isLSOLoaded = ModList.get().isLoaded("legendarysurvivaloverhaul");
        isFirmalifeLoaded = ModList.get().isLoaded("firmalife");
        isRnRLoaded = ModList.get().isLoaded("rnr");

        isGemsRealmLoaded = ModList.get().isLoaded("gemsrealm");
        isStoneZoneLoaded = ModList.get().isLoaded("stonezone");
        isWoodGoodLoaded = ModList.get().isLoaded("everycomp");
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(BlockAssets::bootstrap);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
