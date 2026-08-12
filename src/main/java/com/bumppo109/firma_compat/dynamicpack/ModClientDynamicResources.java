package com.bumppo109.firma_compat.dynamicpack;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.misc.IProgressTracker;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicClientResourceProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.PackGenerationStrategy;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.resources.textures.*;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.stone_zone.api.set.RockType;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneType;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
        executor.accept(this::generateChiseledSandstone);
        //executor.accept(this::generateSuspiciousGravel);
        //executor.accept(this::generateSuspiciousSand);
        /*
        executor.accept(this::generateLumber);
        executor.accept(this::generateWaterWheel);
        executor.accept(this::generateTwig);
        executor.accept(this::generateExtrusiveLoose);
        executor.accept(this::generateMetamorphicLoose);
        executor.accept(this::generateIntrusiveLoose);
        executor.accept(this::generateSedimentaryLoose);
        executor.accept(this::generateBrick);
        executor.accept(this::generateCrate);

         */
    }

    @Override
    protected void addDynamicTranslations(AfterLanguageLoadEvent event) {
    }

    @Override
    public void reload(ResourceManager manager, IProgressTracker reporter) {
        super.reload(manager, reporter);
    }

    private void generateChiseledSandstone(ResourceManager manager, ResourceSink sink) {

        try (TextureImage template = TextureImage.open(manager,
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/block/chiseled_sandstone"))) {

            var respriter = net.mehvahdjukaar.moonlight.api.resources.textures.Respriter.of(template);

            for (SandBlockType sand : SandBlockType.values()) {

                ResourceLocation output = FirmaCompatHelpers.modIdentifier("block/chiseled_sandstone/" + sand.name().toLowerCase(Locale.ROOT));

                sink.addTextureUnlessPresent(manager, output, () -> {

                    ResourceLocation sandstoneTexture = ResourceLocation.fromNamespaceAndPath(
                            "tfc",
                            "block/sandstone/top/" + sand.name().toLowerCase(Locale.ROOT)
                    );

                    try (TextureImage sandstone = TextureImage.open(manager, sandstoneTexture)) {

                        Palette palette = SpriteUtils.extrapolateWoodItemPalette(sandstone);

                        return respriter.recolor(palette);
                    }
                });
            }

        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed generating chiseled sandstone textures", e);
        }
    }

    private void generateSuspiciousGravel(ResourceManager manager, ResourceSink sink) {

        for (Rock rock : Rock.VALUES) {

            ResourceLocation gravelTexture = ResourceLocation.fromNamespaceAndPath(
                    "tfc",
                    "block/rock/gravel/" + rock.getSerializedName()
            );

            try (TextureImage gravel = TextureImage.open(manager, gravelTexture)) {

                Palette palette = SpriteUtils.extrapolateWoodItemPalette(gravel);

                for (int stage = 0; stage < 4; stage++) {

                    int currentStage = stage;

                    ResourceLocation output = FirmaCompatHelpers.modIdentifier(
                            "block/suspicious_gravel/"
                                    + rock.getSerializedName()
                                    + "_"
                                    + currentStage
                    );

                    sink.addTextureUnlessPresent(manager, output, () -> {

                        ResourceLocation overlayTexture =
                                FirmaCompatHelpers.modIdentifier(
                                        "template/block/suspicious_"
                                                + currentStage
                                                + "_overlay"
                                );

                        try (
                                TextureImage overlay = TextureImage.open(
                                        manager,
                                        overlayTexture
                                )
                        ) {

                            TextureImage recoloredOverlay =
                                    Respriter.of(overlay)
                                            .recolor(palette);

                            TextureImage result = gravel.makeCopy();

                            TextureOps.applyOverlayOnExisting(
                                    result,
                                    recoloredOverlay
                            );

                            recoloredOverlay.close();

                            return result;

                        } catch (Exception e) {
                            throw new RuntimeException(
                                    "Failed generating suspicious gravel texture for "
                                            + rock.getSerializedName()
                                            + " stage "
                                            + currentStage,
                                    e
                            );
                        }
                    });
                }

            } catch (Exception e) {
                FirmaCompat.LOGGER.error(
                        "Failed generating suspicious gravel textures for "
                                + rock.getSerializedName(),
                        e
                );
            }
        }
    }

    private void generateSuspiciousSand(ResourceManager manager, ResourceSink sink) {

        for (SandBlockType sand : SandBlockType.values()) {

            ResourceLocation sandTexture = ResourceLocation.fromNamespaceAndPath(
                    "tfc",
                    "block/sand/" + sand.name().toLowerCase(Locale.ROOT)
            );

            try (TextureImage sandImage = TextureImage.open(manager, sandTexture)) {

                Palette palette = SpriteUtils.extrapolateWoodItemPalette(sandImage);

                for (int stage = 0; stage < 4; stage++) {

                    int currentStage = stage;

                    ResourceLocation output = FirmaCompatHelpers.modIdentifier(
                            "block/suspicious_sand/"
                                    + sand.name().toLowerCase(Locale.ROOT)
                                    + "_"
                                    + currentStage
                    );

                    sink.addTextureUnlessPresent(manager, output, () -> {

                        ResourceLocation overlayTexture =
                                FirmaCompatHelpers.modIdentifier(
                                        "template/block/suspicious_"
                                                + currentStage
                                                + "_overlay"
                                );

                        try (
                                TextureImage overlay = TextureImage.open(
                                        manager,
                                        overlayTexture
                                )
                        ) {

                            TextureImage recoloredOverlay =
                                    Respriter.of(overlay)
                                            .recolor(palette);

                            TextureImage result = sandImage.makeCopy();

                            TextureOps.applyOverlayOnExisting(
                                    result,
                                    recoloredOverlay
                            );

                            recoloredOverlay.close();

                            return result;

                        } catch (Exception e) {
                            throw new RuntimeException(
                                    "Failed generating suspicious gravel texture for "
                                            + sand.name()
                                            + " stage "
                                            + currentStage,
                                    e
                            );
                        }
                    });
                }

            } catch (Exception e) {
                FirmaCompat.LOGGER.error(
                        "Failed generating suspicious gravel textures for "
                                + sand.name(),
                        e
                );
            }
        }
    }

    private void generateLumber(ResourceManager manager, ResourceSink sink) {

        try (TextureImage template = TextureImage.open(manager,
                ResourceLocation.fromNamespaceAndPath("tfc","item/wood/lumber"))) {

            var respriter = net.mehvahdjukaar.moonlight.api.resources.textures.Respriter.of(template);

            for (WoodType wood : WoodTypeRegistry.INSTANCE.getValues()) {

                if (wood.planks == null) {
                    continue;
                }

                if (wood.getNamespace().equals("tfc")) {
                    continue;
                }

                ResourceLocation output = FirmaCompatHelpers.modIdentifier("item/" + wood.getTypeName() + "_lumber");

                sink.addTextureUnlessPresent(manager, output, () -> {

                    ResourceLocation plankTexture = RPUtils.findFirstBlockTextureLocation(manager, wood.planks);

                    try (TextureImage plank = TextureImage.open(manager, plankTexture)) {

                        Palette palette = SpriteUtils.extrapolateWoodItemPalette(plank);

                        return respriter.recolor(palette);
                    }
                });
            }

        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed generating lumber textures", e);
        }
    }
    private void generateWaterWheel(ResourceManager manager, ResourceSink sink) {

        try (TextureImage template = TextureImage.open(manager,
                ResourceLocation.fromNamespaceAndPath("tfc","item/wood/water_wheel"))) {

            var respriter = net.mehvahdjukaar.moonlight.api.resources.textures.Respriter.of(template);

            for (WoodType wood : WoodTypeRegistry.INSTANCE.getValues()) {

                if (wood.planks == null) {
                    continue;
                }

                if (wood.getNamespace().equals("tfc")) {
                    continue;
                }

                ResourceLocation output = FirmaCompatHelpers.modIdentifier("item/" + wood.getTypeName() + "_water_wheel");

                sink.addTextureUnlessPresent(manager, output, () -> {

                    ResourceLocation plankTexture = RPUtils.findFirstBlockTextureLocation(manager, wood.planks);

                    try (TextureImage plank = TextureImage.open(manager, plankTexture)) {

                        Palette palette = SpriteUtils.extrapolateWoodItemPalette(plank);

                        return respriter.recolor(palette);
                    }
                });
            }

        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed generating lumber textures", e);
        }
    }
    private void generateTwig(ResourceManager manager, ResourceSink sink) {

        try (TextureImage template = TextureImage.open(manager,
                ResourceLocation.fromNamespaceAndPath("tfc","item/wood/twig"))) {

            var respriter = net.mehvahdjukaar.moonlight.api.resources.textures.Respriter.of(template);

            for (WoodType wood : WoodTypeRegistry.INSTANCE.getValues()) {

                if (wood.planks == null) {
                    continue;
                }

                if (wood.getNamespace().equals("tfc")) {
                    continue;
                }

                ResourceLocation output = FirmaCompatHelpers.modIdentifier("item/" + wood.getTypeName() + "_twig");

                sink.addTextureUnlessPresent(manager, output, () -> {

                    ResourceLocation plankTexture = RPUtils.findFirstBlockTextureLocation(manager, wood.planks);

                    try (TextureImage plank = TextureImage.open(manager, plankTexture)) {

                        Palette palette = SpriteUtils.extrapolateWoodItemPalette(plank);

                        return respriter.recolor(palette);
                    }
                });
            }

        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed generating lumber textures", e);
        }
    }
    private void generateCrate(ResourceManager manager, ResourceSink sink) {

        try (TextureImage template = TextureImage.open(manager,
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/block/crate"))) {

            var respriter = net.mehvahdjukaar.moonlight.api.resources.textures.Respriter.of(template);

            for (WoodType wood : WoodTypeRegistry.INSTANCE.getValues()) {

                if (wood.planks == null) {
                    continue;
                }

                if (wood.getNamespace().equals("tfc")) {
                    continue;
                }

                ResourceLocation output = FirmaCompatHelpers.modIdentifier("block/crate/" + wood.getTypeName());

                sink.addTextureUnlessPresent(manager, output, () -> {

                    ResourceLocation plankTexture = RPUtils.findFirstBlockTextureLocation(manager, wood.planks);

                    try (TextureImage plank = TextureImage.open(manager, plankTexture)) {

                        Palette palette = SpriteUtils.extrapolateWoodItemPalette(plank);

                        return respriter.recolor(palette);
                    }
                });
            }

        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed generating crate textures", e);
        }
    }

    private void generateBrick(ResourceManager manager, ResourceSink sink) {

        try (TextureImage template = TextureImage.open(manager,
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/item/brick"))) {

            var respriter = net.mehvahdjukaar.moonlight.api.resources.textures.Respriter.of(template);

            for (StoneType rock : StoneTypeRegistry.INSTANCE) {

                if (rock.stone == null) {
                    continue;
                }

                if (rock.getNamespace().equals("tfc")) {
                    continue;
                }

                ResourceLocation output = FirmaCompatHelpers.modIdentifier("item/brick/" + rock.getTypeName());

                sink.addTextureUnlessPresent(manager, output, () -> {

                    ResourceLocation plankTexture = RPUtils.findFirstBlockTextureLocation(manager, rock.stone);

                    try (TextureImage plank = TextureImage.open(manager, plankTexture)) {

                        Palette palette = SpriteUtils.extrapolateWoodItemPalette(plank);

                        return respriter.recolor(palette);
                    }
                });
            }

        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed generating brick textures", e);
        }
    }
    private void generateIntrusiveLoose(ResourceManager manager, ResourceSink sink) {

        try (TextureImage template = TextureImage.open(manager,
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/item/loose_igneous_intrusive"))) {

            var respriter = net.mehvahdjukaar.moonlight.api.resources.textures.Respriter.of(template);

            for (StoneType rock : StoneTypeRegistry.INSTANCE) {

                if (rock.stone == null) {
                    continue;
                }

                if (rock.getNamespace().equals("tfc")) {
                    continue;
                }

                ResourceLocation output = FirmaCompatHelpers.modIdentifier("item/intrusive/loose_" + rock.getTypeName());

                sink.addTextureUnlessPresent(manager, output, () -> {

                    ResourceLocation plankTexture = RPUtils.findFirstBlockTextureLocation(manager, rock.stone);

                    try (TextureImage plank = TextureImage.open(manager, plankTexture)) {

                        Palette palette = SpriteUtils.extrapolateWoodItemPalette(plank);

                        return respriter.recolor(palette);
                    }
                });
            }

        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed generating lumber textures", e);
        }
    }

    private void generateExtrusiveLoose(ResourceManager manager, ResourceSink sink) {

        try (TextureImage template = TextureImage.open(manager,
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/item/loose_igneous_extrusive"))) {

            var respriter = net.mehvahdjukaar.moonlight.api.resources.textures.Respriter.of(template);

            for (StoneType rock : StoneTypeRegistry.INSTANCE) {

                if (rock.stone == null) {
                    continue;
                }

                if (rock.getNamespace().equals("tfc")) {
                    continue;
                }

                ResourceLocation output = FirmaCompatHelpers.modIdentifier("item/extrusive/loose_" + rock.getTypeName());

                sink.addTextureUnlessPresent(manager, output, () -> {

                    ResourceLocation plankTexture = RPUtils.findFirstBlockTextureLocation(manager, rock.stone);

                    try (TextureImage plank = TextureImage.open(manager, plankTexture)) {

                        Palette palette = SpriteUtils.extrapolateWoodItemPalette(plank);

                        return respriter.recolor(palette);
                    }
                });
            }

        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed generating lumber textures", e);
        }
    }

    private void generateMetamorphicLoose(ResourceManager manager, ResourceSink sink) {

        try (TextureImage template = TextureImage.open(manager,
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/item/loose_metamorphic"))) {

            var respriter = net.mehvahdjukaar.moonlight.api.resources.textures.Respriter.of(template);

            for (StoneType rock : StoneTypeRegistry.INSTANCE) {

                if (rock.stone == null) {
                    continue;
                }

                if (rock.getNamespace().equals("tfc")) {
                    continue;
                }

                ResourceLocation output = FirmaCompatHelpers.modIdentifier("item/metamorphic/loose_" + rock.getTypeName());

                sink.addTextureUnlessPresent(manager, output, () -> {

                    ResourceLocation plankTexture = RPUtils.findFirstBlockTextureLocation(manager, rock.stone);

                    try (TextureImage plank = TextureImage.open(manager, plankTexture)) {

                        Palette palette = SpriteUtils.extrapolateWoodItemPalette(plank);

                        return respriter.recolor(palette);
                    }
                });
            }

        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed generating lumber textures", e);
        }
    }

    private void generateSedimentaryLoose(ResourceManager manager, ResourceSink sink) {

        try (TextureImage template = TextureImage.open(manager,
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/item/loose_sedimentary"))) {

            var respriter = net.mehvahdjukaar.moonlight.api.resources.textures.Respriter.of(template);

            for (StoneType rock : StoneTypeRegistry.INSTANCE) {

                if (rock.stone == null) {
                    continue;
                }

                if (rock.getNamespace().equals("tfc")) {
                    continue;
                }

                ResourceLocation output = FirmaCompatHelpers.modIdentifier("item/sedimentary/loose_" + rock.getTypeName());

                sink.addTextureUnlessPresent(manager, output, () -> {

                    ResourceLocation plankTexture = RPUtils.findFirstBlockTextureLocation(manager, rock.stone);

                    try (TextureImage plank = TextureImage.open(manager, plankTexture)) {

                        Palette palette = SpriteUtils.extrapolateWoodItemPalette(plank);

                        return respriter.recolor(palette);
                    }
                });
            }

        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed generating lumber textures", e);
        }
    }

    private void regenerateDynamicAssets(ResourceManager manager, ResourceSink sink) {
        StaticResource itemModel = StaticResource.getOrLog(manager,
                ResType.ITEM_MODELS.getPath(ResourceLocation.fromNamespaceAndPath("tfc", "wood/lumber/oak")));

        for (WoodType wood : WoodTypeRegistry.INSTANCE.getValues()) {
            try {
                sink.addSimilarJsonResource(manager, itemModel, "lumber_oak", wood.getVariantId("lumber"));
            } catch (Exception ex) {
                FirmaCompat.LOGGER.error("Failed to generate Lumber item model", ex);
            }
        }

        //item textures
        try (TextureImage template = TextureImage.open(manager, ResourceLocation.fromNamespaceAndPath("tfc", "item/wood/lumber"))) {

            Respriter respriter = Respriter.of(template);

            for (WoodType wood : WoodTypeRegistry.INSTANCE.getValues()) {

                ResourceLocation textureRes = FirmaCompatHelpers.modIdentifier("item/" + wood.getTexturePath() + "_lumber");
                if (sink.alreadyHasTextureAtLocation(manager, textureRes)) return;

                try (TextureImage plankTexture = TextureImage.open(manager,
                        RPUtils.findFirstBlockTextureLocation(manager, wood.planks))) {
                    //Palette targetPalette = SpriteUtils.extrapolateWoodItemPalette(plankTexture);
                    var targetPalette = Palette.fromImage(plankTexture);
                    TextureImage newImage = respriter.recolor(targetPalette);
                    //TextureImage newImage = respriter.recolorWithAnimationOf(plankTexture);
                    sink.addTexture(textureRes, newImage);

                } catch (Exception ex) {
                    FirmaCompat.LOGGER.error("Failed to generate lumber texture", ex);
                }
            }
        } catch (Exception ex) {
            FirmaCompat.LOGGER.error("Could not generate any sled entity texture : ", ex);
        }
    }
}