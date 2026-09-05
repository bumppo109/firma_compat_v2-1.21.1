package com.bumppo109.firma_compat;

import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.client.extensions.ItemRendererExtension;
import net.dries007.tfc.client.model.entity.HorseChestLayer;
import net.dries007.tfc.client.render.blockentity.ChestItemRenderer;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.bumppo109.firma_compat.block.CompatWood.BlockType.*;
import static com.bumppo109.firma_compat.block.CompatWood.BlockType.CLUTCH;
import static com.bumppo109.firma_compat.block.CompatWood.BlockType.ENCASED_AXLE;
import static com.bumppo109.firma_compat.block.CompatWood.BlockType.GEAR_BOX;
import static com.bumppo109.firma_compat.block.CompatWood.BlockType.SEWING_TABLE;
import static com.bumppo109.firma_compat.block.CompatWood.BlockType.SHELF;

@Mod(value = FirmaCompat.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = FirmaCompat.MODID, value = Dist.CLIENT)
public class FirmaCompatClient {

    private static final ResourceLocation SEALED = Helpers.identifier("sealed");

    public FirmaCompatClient(ModContainer container) {
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

        ModBlocks.WOODS.values().forEach(map -> {
            Stream.of(TWIG, BARREL, SCRIBING_TABLE, SEWING_TABLE, SHELF, ENCASED_AXLE, CLUTCH, GEAR_BOX).forEach(type -> ItemBlockRenderTypes.setRenderLayer(map.get(type).get(), cutout));
        });

        event.enqueueWork(() -> {

            //EveryComp barrels
            BuiltInRegistries.BLOCK.stream()
                    .filter(block -> {
                        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                        if (id == null) return false;
                        String path = id.getPath();
                        return (id.getNamespace().equals("everycomp"))
                                && path.endsWith("_barrel")
                                && !path.endsWith("_stomping_barrel")
                                && !path.equals("compat_barrel");
                    })
                    .forEach(block -> {
                        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                        String path = id.getPath(); //regions_unexplored/pine_barrel
                        String[] parts = path.split("/", 3);

                        String woodNamespace = parts[1];
                        String barrelId = parts[2];

                        HorseChestLayer.registerChest(block.asItem(),
                                ResourceLocation.fromNamespaceAndPath("everycomp", "textures/entity/tfc/" + woodNamespace + "/chest/horse/" + barrelId + ".png")
                        );
                    });

            ModBlocks.WOODS.forEach((wood, map) -> {
                HorseChestLayer.registerChest(map.get(BARREL).get().asItem(), FirmaCompatHelpers.modIdentifier("textures/entity/chest/horse/" + wood.getSerializedName() + "_barrel.png"));
                //HorseChestLayer.registerChest(map.get(CHEST).get().asItem(), FirmaCompatHelpers.modIdentifier("textures/entity/chest/horse/" + wood.getSerializedName() + "_chest.png"));
                //HorseChestLayer.registerChest(map.get(TRAPPED_CHEST).get().asItem(), FirmaCompatHelpers.modIdentifier("textures/entity/chest/horse/" + wood.getSerializedName() + "_chest.png"));
            });
        });

        ModBlocks.WOODS.values().forEach(map -> registerSealedProperty(map.get(BARREL), TFCComponents.BARREL));

        /*
        registerLampLitProperty(ModBlocks.LANTERN.get());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LANTERN.get(), cutout);

        for(Metal metal : Metal.values()){
            if(metal.allParts()){
                registerLampLitProperty(ModBlocks.COMPAT_LANTERNS.get(metal).get());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.COMPAT_LANTERNS.get(metal).get(), cutout);
            }
        }

         */
    }

    public static void registerExtensions(RegisterClientExtensionsEvent event) {
        /*
        ModFluids.METALS.forEach((metal, holder) -> event.registerFluidType(
                new FluidRendererExtension(TFCFluids.ALPHA_MASK | metal.getColor(), ClientEventHandler.MOLTEN_STILL, ClientEventHandler.MOLTEN_FLOW, null, null),
                holder.getType()
        ));

         */
        // Chest item renderers
        ModBlocks.WOODS.forEach((compatWood, blockTypeIdMap) -> {
            //registerCustomItemRenderer(event, blockTypeIdMap.get(CHEST), ChestItemRenderer::new);
            //registerCustomItemRenderer(event, blockTypeIdMap.get(TRAPPED_CHEST), ChestItemRenderer::new);
        });
    }

    private static void registerSealedProperty(ItemLike item, Supplier<? extends DataComponentType<?>> type) {
        ItemProperties.register(item.asItem(), SEALED, (stack, level, entity, unused) -> stack.has(type) ? 1.0f : 0f);
    }

    /*
    private static void registerLampLitProperty(ItemLike item) {
        ItemProperties.register(item.asItem(), ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "lit"),
                (stack, level, entity, seed) ->
                        stack.getOrDefault(ModDataComponents.LIT, false) ? 1.0F : 0.0F
        );
    }

     */

    private static <T> void registerCustomItemRenderer(RegisterClientExtensionsEvent event, @Nullable Supplier<? extends ItemLike> item, Function<T, BlockEntityWithoutLevelRenderer> renderer) {
        if (item != null) {
            event.registerItem(ItemRendererExtension.cached(() -> (BlockEntityWithoutLevelRenderer)renderer.apply((T) item.get().asItem())), new Item[]{((ItemLike)item.get()).asItem()});
        }

    }
}