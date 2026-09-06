package com.bumppo109.firma_compat;

import com.bumppo109.firma_compat.addon.ModCompatHandler;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.dynamicpack.ModClientDynamicResources;
import com.bumppo109.firma_compat.event.ModClientEvents;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModCreativeModeTab;
import com.bumppo109.firma_compat.item.ModItems;
import com.mojang.logging.LogUtils;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(FirmaCompat.MODID)
public class FirmaCompat {
    public static final String MODID = "firma_compat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FirmaCompat(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(ModClientEvents::addToBlockEntities);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModFluids.FLUID.register(modEventBus);
        ModCreativeModeTab.CREATIVE_TABS.register(modEventBus);

        ModCompatHandler.registerEveryCompatModules();

        NeoForge.EVENT_BUS.register(this);

        if (FMLEnvironment.dist.isClient()) {
            RegHelper.registerDynamicResourceProvider(new ModClientDynamicResources());
            modEventBus.addListener(FirmaCompatClient::registerExtensions);
        }
    }

    public void commonSetup(FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
