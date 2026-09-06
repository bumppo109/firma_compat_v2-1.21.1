package com.bumppo109.firma_compat.dynamicpack;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.block.CompatWood;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.misc.IProgressTracker;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicClientResourceProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.PackGenerationStrategy;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ModClientDynamicResources extends DynamicClientResourceProvider {

    public static final ModClientDynamicResources INSTANCE = new ModClientDynamicResources();

    public ModClientDynamicResources() {
        super(
                FirmaCompatHelpers.modIdentifier("generated_pack"),
                PackGenerationStrategy.CACHED
        );
    }

    @Override
    protected Collection<String> gatherSupportedNamespaces() {
        return List.of(
                "minecraft",
                "tfc",
                FirmaCompat.MODID
        );
    }

    @Override
    protected void regenerateDynamicAssets(Consumer<ResourceGenTask> executor) {
        executor.accept(this::generateWoodTextures);
    }

    @Override
    protected void addDynamicTranslations(AfterLanguageLoadEvent event) {
    }

    @Override
    public void reload(ResourceManager manager, IProgressTracker reporter) {
        super.reload(manager, reporter);
    }

    private void generateWoodTextures(ResourceManager manager, ResourceSink sink) {
        for (CompatWood wood: CompatWood.values()) {
            ResourceLocation woodColor = ResourceLocation.withDefaultNamespace("block/" + wood.getSerializedName() + "_planks");
            ResourceLocation leavesColor = ResourceLocation.withDefaultNamespace("block/" + wood.getSerializedName() + "_leaves");

            ResourceLocation fallenLeavesTexture = ResourceLocation.fromNamespaceAndPath("tfc","item/groundcover/fallen_leaves");
            ResourceLocation lumberTexture = ResourceLocation.fromNamespaceAndPath("tfc","item/wood/lumber");
            ResourceLocation twigTexture = ResourceLocation.fromNamespaceAndPath("tfc","item/wood/twig");
            ResourceLocation waterwheelTexture = ResourceLocation.fromNamespaceAndPath("tfc","entity/water_wheel/oak");
            ResourceLocation waterwheelItemTexture = ResourceLocation.fromNamespaceAndPath("tfc","item/wood/water_wheel");
            ResourceLocation crateTexture = ResourceLocation.fromNamespaceAndPath("tfc","block/wood/crate/oak");

            ResourceLocation bookshelfTexture = ResourceLocation.fromNamespaceAndPath("tfc","block/wood/bookshelf/oak_empty");
            ResourceLocation bookshelfFullTexture = ResourceLocation.fromNamespaceAndPath("tfc","block/wood/bookshelf/oak_occupied");
            ResourceLocation bookshelfEmptyOverlay = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"textures/template/block/wood/bookshelf/bookshelf_empty");
            ResourceLocation bookshelfOverlay = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"textures/template/block/wood/bookshelf/bookshelf_occupied");

            ResourceLocation lecternTexture = ResourceLocation.fromNamespaceAndPath("tfc","block/wood/lectern/oak/base");
            ResourceLocation lecternFrontTexture = ResourceLocation.fromNamespaceAndPath("tfc","block/wood/lectern/oak/front");
            ResourceLocation lecternSidesTexture = ResourceLocation.fromNamespaceAndPath("tfc","block/wood/lectern/oak/sides");
            ResourceLocation lecternTopTexture = ResourceLocation.fromNamespaceAndPath("tfc","block/wood/lectern/oak/top");

            ResourceLocation workTexture = ResourceLocation.fromNamespaceAndPath("tfc","block/wood/workbench/oak_front");
            ResourceLocation workSideTexture = ResourceLocation.fromNamespaceAndPath("tfc","block/wood/workbench/oak_side");
            ResourceLocation workTopTexture = ResourceLocation.fromNamespaceAndPath("tfc","block/wood/workbench/oak_top");

            ResourceLocation chestHorseTexture = ResourceLocation.fromNamespaceAndPath("tfc","entity/chest/horse/oak");
            ResourceLocation barrelHorseTexture = ResourceLocation.fromNamespaceAndPath("tfc","entity/chest/horse/oak_barrel");
            ResourceLocation chestTexture = ResourceLocation.fromNamespaceAndPath("tfc","entity/chest/normal/oak");
            ResourceLocation chestLeftTexture = ResourceLocation.fromNamespaceAndPath("tfc","entity/chest/normal_left/oak");
            ResourceLocation chestRightTexture = ResourceLocation.fromNamespaceAndPath("tfc","entity/chest/normal_right/oak");
            ResourceLocation trappedChestTexture = ResourceLocation.fromNamespaceAndPath("tfc","entity/chest/trapped/oak");
            ResourceLocation trappedChestLeftTexture = ResourceLocation.fromNamespaceAndPath("tfc","entity/chest/trapped_left/oak");
            ResourceLocation trappedChestRightTexture = ResourceLocation.fromNamespaceAndPath("tfc","entity/chest/trapped_right/oak");

            if (wood.woodMaterial().leaves() != null) {
                recolorTexture(manager, sink, fallenLeavesTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + wood.getSerializedName() + "_fallen_leaves"), leavesColor);
            }

            recolorTexture(manager, sink, lumberTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + wood.getSerializedName() + "_lumber"), woodColor);
            recolorTexture(manager, sink, twigTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + wood.getSerializedName() + "_twig"), woodColor);
            recolorTexture(manager, sink, waterwheelTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"entity/water_wheel/" + wood.getSerializedName()), woodColor);
            recolorTexture(manager, sink, waterwheelItemTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + wood.getSerializedName() + "_water_wheel"), woodColor);
            recolorTexture(manager, sink, crateTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/" + wood.getSerializedName() + "_crate"), woodColor);

            //simpleOverlayRecolor(manager, sink, bookshelfTexture, bookshelfEmptyOverlay, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"textures/block/wood/bookshelf/" + wood.getSerializedName() + "_empty"), woodColor);
            //simpleOverlayRecolor(manager, sink, bookshelfFullTexture, bookshelfOverlay, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"textures/block/wood/bookshelf/" + wood.getSerializedName() + "_occupied"), woodColor);
        }
    }

    private void simpleOverlayRecolor(ResourceManager manager, ResourceSink sink, ResourceLocation texture, ResourceLocation overlay, ResourceLocation outputLoc, ResourceLocation baseTexture) {
        try (TextureImage base = TextureImage.open(manager, baseTexture)) {
            Palette palette = Palette.fromImage(base);

            try (TextureImage recolor = TextureImage.open(manager, texture)) {
                Respriter respriter = Respriter.of(recolor);

                try (TextureImage overlayTexture = TextureImage.open(manager, overlay)) {

                    TextureImage tempTexture = respriter.recolor(palette);

                    TextureOps.applyOverlayOnExisting(tempTexture, overlayTexture);

                    tempTexture.close();

                    sink.addTexture(outputLoc, tempTexture);
                } catch (Exception e) {
                    throw new RuntimeException("[Firma Compat] Failed to apply overlay " + overlay, e);
                }
            } catch (Exception e) {
                throw new RuntimeException("[Firma Compat] Failed to recolor " + texture, e);
            }

        } catch (Exception e) {
            throw new RuntimeException("[Firma Compat] Failed to get base texture " + baseTexture, e);
        }
    }

    private void recolorTexture(ResourceManager manager, ResourceSink sink, ResourceLocation texture, ResourceLocation outputLoc, ResourceLocation baseTexture) {
        try (TextureImage base = TextureImage.open(manager, baseTexture)) {
            Palette palette = Palette.fromImage(base);

            try (TextureImage recolor = TextureImage.open(manager, texture)) {
                Respriter respriter = Respriter.of(recolor);

                TextureImage finalTexture = respriter.recolor(palette);

                sink.addTexture(outputLoc, finalTexture);
            } catch (Exception e) {
                throw new RuntimeException("[Firma Compat] Failed to recolor " + texture, e);
            }

        } catch (Exception e) {
            throw new RuntimeException("[Firma Compat] Failed to get base texture " + baseTexture, e);
        }
    }
}