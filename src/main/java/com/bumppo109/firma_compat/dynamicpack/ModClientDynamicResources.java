package com.bumppo109.firma_compat.dynamicpack;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.materials.BlockAssets;
import com.bumppo109.firma_compat.materials.BlockTextureSlot;
import com.bumppo109.firma_compat.materials.SoilMaterial;
import com.bumppo109.firma_compat.materials.food.FoodIngredients;
import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class ModClientDynamicResources extends DynamicClientResourceProvider {

    public static final ModClientDynamicResources INSTANCE = new ModClientDynamicResources();

    public ModClientDynamicResources() {
        super(
                FirmaCompatHelpers.modIdentifier("generated_pack"),
                PackGenerationStrategy.CACHED
        );
        BlockAssets.bootstrap();
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
        executor.accept(this::generateMetalTextures);
        executor.accept(this::generateRockTextures);
        executor.accept(this::generateSoilTextures);
        executor.accept(this::generateTFCSandStoneTextures);
        executor.accept(this::generateTFCMetalTextures);
        executor.accept(this::generateFoodTextures);
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
            if (wood.woodMaterial().leaves() != null) {
                ResourceLocation leavesRes = BuiltInRegistries.BLOCK.getKey(wood.woodMaterial().leaves().get());
                ResourceLocation leavesColor = ResourceLocation.fromNamespaceAndPath(leavesRes.getNamespace(),"block/" + leavesRes.getPath());

                ResourceLocation fallenLeavesTexture = ResourceLocation.fromNamespaceAndPath("tfc","item/groundcover/fallen_leaves");

                recolorTexture(manager, sink, fallenLeavesTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + wood.getSerializedName() + "_fallen_leaves"), leavesColor);
            }

            if (wood.woodMaterial().planks() != null) {
                ResourceLocation planksRes = BuiltInRegistries.BLOCK.getKey(wood.woodMaterial().planks().get());
                ResourceLocation woodColor = ResourceLocation.fromNamespaceAndPath(planksRes.getNamespace(),"block/" + planksRes.getPath());

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

                recolorTexture(manager, sink, lumberTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + wood.getSerializedName() + "_lumber"), woodColor);
                recolorTexture(manager, sink, twigTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + wood.getSerializedName() + "_twig"), woodColor);
                recolorTexture(manager, sink, waterwheelTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"entity/water_wheel/" + wood.getSerializedName()), woodColor);
                recolorTexture(manager, sink, waterwheelItemTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + wood.getSerializedName() + "_water_wheel"), woodColor);
                recolorTexture(manager, sink, crateTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/" + wood.getSerializedName() + "_crate"), woodColor);

                //simpleOverlayRecolor(manager, sink, bookshelfTexture, bookshelfEmptyOverlay, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"textures/block/wood/bookshelf/" + wood.getSerializedName() + "_empty"), woodColor);
                //simpleOverlayRecolor(manager, sink, bookshelfFullTexture, bookshelfOverlay, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"textures/block/wood/bookshelf/" + wood.getSerializedName() + "_occupied"), woodColor);
            }
        }
    }

    private void generateFoodTextures(ResourceManager manager, ResourceSink sink) {
        FoodIngredients.preserveIngredients().forEach(foodIngredient -> {
            ResourceLocation foodRes = BuiltInRegistries.ITEM.getKey(foodIngredient.foodItem().get());
            ResourceLocation foodTexture = ResourceLocation.withDefaultNamespace("item/" + foodRes.getPath());

            ResourceLocation jam = ResourceLocation.fromNamespaceAndPath("tfc","item/food/green_apple_jam");
            ResourceLocation jar = ResourceLocation.fromNamespaceAndPath("tfc","item/jar/green_apple");
            ResourceLocation jarOpen = ResourceLocation.fromNamespaceAndPath("tfc","item/jar/green_apple_unsealed");
            ResourceLocation jarOverlay = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/item/overlay/jar");
            ResourceLocation jarOpenOverlay = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/item/overlay/jar_unsealed");

            ResourceLocation jarBlock = ResourceLocation.fromNamespaceAndPath("tfc","block/jar/green_apple");

            recolorTexture(manager, sink, jarBlock, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/jar/" + foodRes.getPath()), foodTexture);
            recolorTexture(manager, sink, jam, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + foodRes.getPath() + "_jam"), foodTexture);
            simpleOverlayRecolor(manager, sink, jar, jarOverlay, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/jar/" + foodRes.getPath()), foodTexture);
            simpleOverlayRecolor(manager, sink, jarOpen, jarOpenOverlay, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/jar/" + foodRes.getPath() + "_unsealed"), foodTexture);
        });
    }

    private void generateTFCSandStoneTextures(ResourceManager manager, ResourceSink sink) {
        for (SandBlockType sand : SandBlockType.values()) {
            ResourceLocation sandColor = ResourceLocation.fromNamespaceAndPath("tfc","block/sandstone/top/" + sand.name().toLowerCase(Locale.ROOT));

            recolorTexture(manager, sink, ResourceLocation.withDefaultNamespace("block/chiseled_sandstone"),
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/tfc_chiseled_" + sand.name().toLowerCase(Locale.ROOT) + "_sandstone"),
                    sandColor);
        }
    }

    private void generateSoilTextures(ResourceManager manager, ResourceSink sink) {
        for (SoilMaterial material : SoilMaterial.values()) {
            ResourceLocation dirtTexture = BlockAssets.get(material.getDirt().get()).textures().get(BlockTextureSlot.SIDE);

            simpleOverlay(manager, sink, dirtTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/block/overlay/clay_dirt_mask"),
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block/clay_" + material.getSerializedName()));
        }
    }

    private void generateRockTextures(ResourceManager manager, ResourceSink sink) {
        for (CompatRock rock : CompatRock.values()) {
            ResourceLocation rawRes = BuiltInRegistries.BLOCK.getKey(rock.rockMaterial().raw().base().get());
            ResourceLocation rockColor = rock.blockAsset().textures().get(BlockTextureSlot.SIDE);

            ResourceLocation looseTexture = switch (rock.category()) {
                case METAMORPHIC -> ResourceLocation.fromNamespaceAndPath("tfc","item/loose_rock/gneiss");
                case SEDIMENTARY -> ResourceLocation.fromNamespaceAndPath("tfc","item/loose_rock/chalk");
                case FELSIC_IGNEOUS_EXTRUSIVE, INTERMEDIATE_IGNEOUS_EXTRUSIVE, MAFIC_IGNEOUS_EXTRUSIVE -> ResourceLocation.fromNamespaceAndPath("tfc","item/loose_rock/andesite");
                case FELSIC_IGNEOUS_INTRUSIVE, INTERMEDIATE_IGNEOUS_INTRUSIVE, MAFIC_IGNEOUS_INTRUSIVE -> ResourceLocation.fromNamespaceAndPath("tfc","item/loose_rock/granite");
            };
            ResourceLocation brickTexture = ResourceLocation.fromNamespaceAndPath("tfc","item/brick/andesite");
            ResourceLocation cobbleTexture = ResourceLocation.withDefaultNamespace("block/cobblestone");
            ResourceLocation mossyCobbleOverlay = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/block/overlay/mossy_cobble_mask");

            if (rock.equals(CompatRock.BLACKSTONE)) {
                recolorTexture(manager, sink, brickTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "item/polished_" + rock.getSerializedName() + "_brick"), rockColor);
            } else {
                recolorTexture(manager, sink, brickTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "item/" + rock.getSerializedName() + "_brick"), rockColor);
            }

            recolorTexture(manager, sink, looseTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "item/loose_" + rock.getSerializedName()), rockColor);
            recolorTexture(manager, sink, cobbleTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/" + rock.getSerializedName() + "_cobble"), rockColor);
            simpleOverlayRecolor(manager, sink, cobbleTexture, mossyCobbleOverlay, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/mossy_" + rock.getSerializedName() + "_cobble"), rockColor);
        }
    }

    private void generateMetalTextures(ResourceManager manager, ResourceSink sink) {
        for (CompatMetal metal: CompatMetal.values()) {
            ResourceLocation ingotRes = BuiltInRegistries.ITEM.getKey(metal.getMetalMaterial().ingot().get());
            ResourceLocation metalColor = ResourceLocation.fromNamespaceAndPath(ingotRes.getNamespace(),"item/" + ingotRes.getPath());

            for (CompatMetal.ItemType itemType : CompatMetal.ItemType.values()) {

                boolean requiresOverlay = switch (itemType) {
                    case CHISEL, PROPICK, HAMMER, SCYTHE, MACE, KNIFE, JAVELIN, SAW, TUYERE -> true;
                    default -> false;
                };

                ResourceLocation baseTexture;
                if (itemType.equals(CompatMetal.ItemType.NUGGET)) {
                    baseTexture = ResourceLocation.withDefaultNamespace("item/iron_nugget");
                } else {
                    baseTexture = ResourceLocation.fromNamespaceAndPath("tfc","item/metal/" + itemType.getSerializedName() + "/wrought_iron");
                }

                String outputPath = "item/" + metal.getSerializedName() + "_" + itemType.getSerializedName();

                if (requiresOverlay) {
                    ResourceLocation overlayTexture = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"template/item/overlay/" + itemType.getSerializedName() + "_mask");
                    simpleOverlayRecolor(manager, sink, baseTexture, overlayTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, outputPath), metalColor);
                } else {
                    recolorTexture(manager, sink, baseTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, outputPath), metalColor);
                }
            }
        }
    }

    private void generateTFCMetalTextures(ResourceManager manager, ResourceSink sink) {
        for (Metal metal: Metal.values()) {
            ResourceLocation ingotRes = BuiltInRegistries.ITEM.getKey(TFCItems.METAL_ITEMS.get(metal).get(Metal.ItemType.INGOT).get());
            ResourceLocation metalColor = ResourceLocation.fromNamespaceAndPath(ingotRes.getNamespace(),"item/" + ingotRes.getPath());

            ResourceLocation baseTexture = ResourceLocation.withDefaultNamespace("item/iron_nugget");
            String outputPath = "item/tfc_" + metal.getSerializedName() + "_nugget";

            recolorTexture(manager, sink, baseTexture, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, outputPath), metalColor);
        }
    }

    private void simpleOverlay(
            ResourceManager manager,
            ResourceSink sink,
            ResourceLocation texture,
            ResourceLocation overlay,
            ResourceLocation outputLoc
    ) {
        try (TextureImage base = TextureImage.open(manager, texture)) {

            try (TextureImage overlayTexture = TextureImage.open(manager, overlay)) {

                TextureOps.applyOverlayOnExisting(
                        base,
                        overlayTexture
                );

                sink.addTexture(outputLoc, base);

            } catch (Exception e) {
                throw new RuntimeException(
                        "[Firma Compat] Failed to apply overlay " + overlay,
                        e
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "[Firma Compat] Failed to recolor " + texture,
                    e
            );
        }
    }

    private void simpleOverlayRecolor(
            ResourceManager manager,
            ResourceSink sink,
            ResourceLocation texture,
            ResourceLocation overlay,
            ResourceLocation outputLoc,
            ResourceLocation baseTexture
    ) {
        try (TextureImage base = TextureImage.open(manager, baseTexture)) {

            Palette palette = Palette.fromImage(base);

            try (TextureImage recolor = TextureImage.open(manager, texture)) {

                Respriter respriter = Respriter.of(recolor);

                try (TextureImage overlayTexture = TextureImage.open(manager, overlay)) {

                    TextureImage tempTexture = respriter.recolor(palette);

                    TextureOps.applyOverlayOnExisting(
                            tempTexture,
                            overlayTexture
                    );

                    sink.addTexture(outputLoc, tempTexture);

                } catch (Exception e) {
                    throw new RuntimeException(
                            "[Firma Compat] Failed to apply overlay " + overlay,
                            e
                    );
                }

            } catch (Exception e) {
                throw new RuntimeException(
                        "[Firma Compat] Failed to recolor " + texture,
                        e
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "[Firma Compat] Failed to get base texture " + baseTexture,
                    e
            );
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