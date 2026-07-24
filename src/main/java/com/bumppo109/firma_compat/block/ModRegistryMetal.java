package com.bumppo109.firma_compat.block;

import net.dries007.tfc.common.LevelTier;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

public interface ModRegistryMetal extends StringRepresentable {
    LevelTier toolTier();

    Holder<ArmorMaterial> armorMaterial();

    int armorDurability(ArmorItem.Type var1);

    MapColor mapColor();

    Rarity rarity();
}
