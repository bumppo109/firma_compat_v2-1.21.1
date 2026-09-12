package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.blockentity.FluidBrewingStandBlockEntity;
import com.bumppo109.firma_compat.blockentity.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FluidBrewingStandBlock extends BaseEntityBlock {

    public static final MapCodec<FluidBrewingStandBlock> CODEC =
            simpleCodec(FluidBrewingStandBlock::new);

    public static final BooleanProperty HAS_BOTTLE_0 =
            BooleanProperty.create("has_bottle_0");

    public static final BooleanProperty HAS_BOTTLE_1 =
            BooleanProperty.create("has_bottle_1");

    public static final BooleanProperty HAS_BOTTLE_2 =
            BooleanProperty.create("has_bottle_2");

    protected static final VoxelShape SHAPE = Shapes.or(Block.box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0), Block.box(7.0, 0.0, 7.0, 9.0, 14.0, 9.0));

    public FluidBrewingStandBlock(Properties properties) {
        super(properties);

        registerDefaultState(
                stateDefinition.any()
                        .setValue(HAS_BOTTLE_0, false)
                        .setValue(HAS_BOTTLE_1, false)
                        .setValue(HAS_BOTTLE_2, false)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(
                HAS_BOTTLE_0,
                HAS_BOTTLE_1,
                HAS_BOTTLE_2
        );
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        return new FluidBrewingStandBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (level.isClientSide) {
            return null;
        }

        return createTickerHelper(
                type,
                ModBlockEntities.FLUID_BREWING_STAND.get(),
                FluidBrewingStandBlockEntity::serverTick
        );
    }

    @Override
    protected void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean isMoving
    ) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof FluidBrewingStandBlockEntity brewingStand) {
                Containers.dropContents(
                        level,
                        pos,
                        brewingStand
                );
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }


    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        if (!level.isClientSide) {
            MenuProvider provider =
                    getMenuProvider(state, level, pos);

            if (provider != null) {
                player.openMenu(provider);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected MenuProvider getMenuProvider(
            BlockState state,
            Level level,
            BlockPos pos
    ) {
        BlockEntity blockEntity =
                level.getBlockEntity(pos);

        if (blockEntity instanceof MenuProvider provider) {
            return provider;
        }

        return null;
    }
}
