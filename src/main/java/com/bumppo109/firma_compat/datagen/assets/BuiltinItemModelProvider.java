package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.FirmaCompatClient;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatRockMaterial;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BuiltinItemModelProvider extends ItemModelProvider {
    public BuiltinItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FirmaCompat.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        for (CompatWood wood : CompatWood.VALUES) {
            basicItem(ModItems.LUMBER.get(wood).get());
            basicItem(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.WATER_WHEEL).get().asItem());
            basicItem(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.TWIG).get().asItem());
            withExistingParent(itemPathName(ModItems.SUPPORTS.get(wood).get()), ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/support/" + wood.getSerializedName() + "_inventory"));
            withExistingParent(wood.getSerializedName() + "_gear_box", ResourceLocation.fromNamespaceAndPath("tfc","block/ore"))
                    .texture("all", ResourceLocation.withDefaultNamespace("block/" + wood.getSerializedName() + "_planks"))
                    .texture("overlay", ResourceLocation.fromNamespaceAndPath("tfc", "block/axle_casing_front"));
            withExistingParent(wood.getSerializedName() + "_axle", modLoc("block/axle/" + wood.getSerializedName() + "_axle"));
            withExistingParent(wood.getSerializedName() + "_bladed_axle", modLoc("block/bladed_axle/" + wood.getSerializedName() + "_bladed_axle"));
        }

        for (CompatRock rock : CompatRock.VALUES) {
            basicItem(rock.brickItem().get());
            basicItem(ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem());
            withExistingParent("mossy_" + rock.getSerializedName() + "_loose", mcLoc("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + rock.getSerializedName() + "_loose"))
                    .texture("layer1", ResourceLocation.fromNamespaceAndPath("tfc", "item/loose_rock/moss"));
        }
    }

    private String itemPathName(Item item) {
        return item.builtInRegistryHolder().key().location().getPath();
    }
}
