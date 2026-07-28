package com.bumppo109.firma_compat.mixin.firmalife;

import com.bumppo109.firma_compat.addon.legendarysurvivaloverhaul.ClimateStationAccess;
import com.eerussianguy.firmalife.common.blockentities.ClimateStationBlockEntity;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;


@Mixin(ClimateStationBlockEntity.class)
public interface ClimateStationAccessor extends ClimateStationAccess {

    @Accessor("positions")
    Set<BlockPos> firmaCompat$getPositions();

}