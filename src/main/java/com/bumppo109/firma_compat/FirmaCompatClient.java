package com.bumppo109.firma_compat;

import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.client.model.entity.HorseChestLayer;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.bumppo109.firma_compat.block.CompatWood.BlockType.*;
import static com.bumppo109.firma_compat.block.CompatWood.BlockType.CLUTCH;
import static com.bumppo109.firma_compat.block.CompatWood.BlockType.ENCASED_AXLE;
import static com.bumppo109.firma_compat.block.CompatWood.BlockType.GEAR_BOX;
import static com.bumppo109.firma_compat.block.CompatWood.BlockType.SEWING_TABLE;
import static com.bumppo109.firma_compat.block.CompatWood.BlockType.SHELF;
import static com.bumppo109.firma_compat.block.ModBlocks.GRADED_ORES;
import static com.bumppo109.firma_compat.block.ModBlocks.ORES;

@Mod(value = FirmaCompat.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = FirmaCompat.MODID, value = Dist.CLIENT)
public class FirmaCompatClient {

    public FirmaCompatClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
// Render Types
        final RenderType solid = RenderType.solid();
        final RenderType cutout = RenderType.cutout();
        final RenderType cutoutMipped = RenderType.cutoutMipped();
        final RenderType translucent = RenderType.translucent();
        final Predicate<RenderType> ghostBlock = rt -> rt == cutoutMipped || rt == Sheets.translucentCullBlockSheet();

        //final Predicate<RenderType> leafPredicate = layer -> Minecraft.useFancyGraphics() ? layer == cutoutMipped : layer == solid;
        ModBlocks.WOODS.values().forEach(map -> {
            Stream.of(TWIG, BARREL, SCRIBING_TABLE, SEWING_TABLE, SHELF, ENCASED_AXLE, CLUTCH, GEAR_BOX).forEach(type -> ItemBlockRenderTypes.setRenderLayer(map.get(type).get(), cutout));
        });

        event.enqueueWork(() -> {
            ModBlocks.WOODS.forEach((wood, map) -> {
                HorseChestLayer.registerChest(map.get(BARREL).get().asItem(), FirmaCompatHelpers.modIdentifier("textures/entity/chest/horse/" + wood.getSerializedName() + "_barrel.png"));
            });
        });

        ModBlocks.WOODS.values().forEach(map -> registerSealedProperty(map.get(BARREL), TFCComponents.BARREL));

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get(), cutout);

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CLAY_GRASS_BLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CLAY_PODZOL.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.KAOLIN_CLAY_PODZOL.get(), cutout);

        ORES.forEach((rock, oreMap) ->
                oreMap.forEach((ore, blockId) ->
                        ItemBlockRenderTypes.setRenderLayer(blockId.get(), RenderType.cutout())
                )
        );

        GRADED_ORES.forEach((rock, oreMap) ->
                oreMap.forEach((ore, gradeMap) ->
                        gradeMap.forEach((grade, blockId) ->
                                ItemBlockRenderTypes.setRenderLayer(blockId.get(), RenderType.cutout())
                        )
                )
        );
    }

    private static final ResourceLocation SEALED = Helpers.identifier("sealed");

    private static void registerSealedProperty(ItemLike item, Supplier<? extends DataComponentType<?>> type)
    {
        ItemProperties.register(item.asItem(), SEALED, (stack, level, entity, unused) -> stack.has(type) ? 1.0f : 0f);
    }
}
