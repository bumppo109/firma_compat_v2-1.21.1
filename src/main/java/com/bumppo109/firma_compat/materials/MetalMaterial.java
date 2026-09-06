package com.bumppo109.firma_compat.materials;

import com.bumppo109.firma_compat.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public enum MetalMaterial {

    NETHERITE(
            () -> Items.NETHERITE_INGOT,
            null,
            () -> Blocks.NETHERITE_BLOCK,
            () -> Items.NETHERITE_SWORD,
            () -> Items.NETHERITE_PICKAXE,
            () -> Items.NETHERITE_SHOVEL,
            () -> Items.NETHERITE_AXE,
            () -> Items.NETHERITE_HOE,
            () -> Items.NETHERITE_HELMET,
            () -> Items.NETHERITE_CHESTPLATE,
            () -> Items.NETHERITE_LEGGINGS,
            () -> Items.NETHERITE_BOOTS,
            null,
            null
    ),

    SCRAP_NETHERITE(
            ModItems.SCRAP_NETHERITE_INGOT,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null

    ),
    IRON(
            () -> Items.IRON_INGOT,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null

    );
    private final EnumMap<MetalPart, Supplier<?>> parts = new EnumMap<>(MetalPart.class);

    private final Supplier<Item> ingot;
    private final @Nullable Supplier<Item> nugget;
    private final @Nullable Supplier<Block> storageBlock;
    private final @Nullable Supplier<Item> sword;
    private final @Nullable Supplier<Item> pickaxe;
    private final @Nullable Supplier<Item> shovel;
    private final @Nullable Supplier<Item> axe;
    private final @Nullable Supplier<Item> hoe;
    private final @Nullable Supplier<Item> helmet;
    private final @Nullable Supplier<Item> chestplate;
    private final @Nullable Supplier<Item> leggings;
    private final @Nullable Supplier<Item> boots;
    private final @Nullable Supplier<Item> shield;
    private final @Nullable Supplier<Item> horseArmor;

    MetalMaterial(
            Supplier<Item> ingot,
            @Nullable Supplier<Item> nugget,
            @Nullable Supplier<Block> storageBlock,
            @Nullable Supplier<Item> sword,
            @Nullable Supplier<Item> pickaxe,
            @Nullable Supplier<Item> shovel,
            @Nullable Supplier<Item> axe,
            @Nullable Supplier<Item> hoe,
            @Nullable Supplier<Item> helmet,
            @Nullable Supplier<Item> chestplate,
            @Nullable Supplier<Item> leggings,
            @Nullable Supplier<Item> boots,
            @Nullable Supplier<Item> shield,
            @Nullable Supplier<Item> horseArmor
    ) {
        this.ingot = ingot;
        this.nugget = nugget;
        this.storageBlock = storageBlock;
        this.sword = sword;
        this.pickaxe = pickaxe;
        this.shovel = shovel;
        this.axe = axe;
        this.hoe = hoe;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.shield = shield;
        this.horseArmor = horseArmor;

        parts.put(MetalPart.INGOT, ingot);
        parts.put(MetalPart.NUGGET, nugget);
        parts.put(MetalPart.STORAGE_BLOCK, storageBlock);
        parts.put(MetalPart.SWORD, sword);
        parts.put(MetalPart.PICKAXE, pickaxe);
        parts.put(MetalPart.SHOVEL, shovel);
        parts.put(MetalPart.AXE, axe);
        parts.put(MetalPart.HOE, hoe);
        parts.put(MetalPart.HELMET, helmet);
        parts.put(MetalPart.CHESTPLATE, chestplate);
        parts.put(MetalPart.LEGGINGS, leggings);
        parts.put(MetalPart.BOOTS, boots);
        parts.put(MetalPart.SHIELD, shield);
        parts.put(MetalPart.HORSE_ARMOR, horseArmor);
    }

    public EnumMap<MetalPart, Supplier<?>> parts() {
        return parts;
    }

    public Supplier<Item> ingot() {
        return ingot;
    }

    public Supplier<Item> nugget() {
        return nugget;
    }

    public Supplier<Block> storageBlock() {
        return storageBlock;
    }

    public Supplier<Item> sword() {
        return sword;
    }

    public Supplier<Item> pickaxe() {
        return pickaxe;
    }

    public Supplier<Item> shovel() {
        return shovel;
    }

    public Supplier<Item> axe() {
        return axe;
    }

    public Supplier<Item> hoe() {
        return hoe;
    }

    public Supplier<Item> helmet() {
        return helmet;
    }

    public Supplier<Item> chestplate() {
        return chestplate;
    }

    public Supplier<Item> leggings() {
        return leggings;
    }

    public Supplier<Item> boots() {
        return boots;
    }

    public Supplier<Item> shield() {
        return shield;
    }

    public Supplier<Item> horseArmor() {
        return horseArmor;
    }

    public enum MetalPart {
        INGOT,
        NUGGET,
        STORAGE_BLOCK,
        SWORD,
        PICKAXE,
        SHOVEL,
        AXE,
        HOE,
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        SHIELD,
        HORSE_ARMOR
    }
}