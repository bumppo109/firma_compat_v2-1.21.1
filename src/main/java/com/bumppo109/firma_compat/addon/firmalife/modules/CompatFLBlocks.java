package com.bumppo109.firma_compat.addon.firmalife.modules;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.eerussianguy.firmalife.common.blockentities.BarrelPressBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.FLBlockEntities;
import com.eerussianguy.firmalife.common.blockentities.KegBlockEntity;
import com.eerussianguy.firmalife.common.blocks.*;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompatFLBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, FirmaCompat.MODID);

    //Wood
    public static final Map<CompatWood, Id<Block>> FOOD_SHELVES = Helpers.mapOf(CompatWood.class, wood -> register(wood.getSerializedName() + "_food_shelf", () -> new FoodShelfBlock(shelfProperties().mapColor(wood.woodColor()))));
    public static final Map<CompatWood, Id<Block>> HANGERS = Helpers.mapOf(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_hanger", () -> new HangerBlock(hangerProperties().mapColor(wood.woodColor()))));
    public static final Map<CompatWood, Id<Block>> JARBNETS = Helpers.mapOf(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_jarbnet", () -> new JarbnetBlock(jarbnetProperties().mapColor(wood.woodColor()))));
    public static final Map<CompatWood, Id<Block>> KEG_SUBS = Helpers.mapOf(CompatWood.class, (wood) -> registerNoItem(wood.getSerializedName() + "_keg_sub", () -> new KegSubBlock(ExtendedProperties.of().noLootTable().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion().strength(10.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.KEG_SUB))));
    public static final Map<CompatWood, Id<Block>> KEGS = Helpers.mapOf(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_keg", () -> new KegCoreBlock(ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion().strength(10.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.KEG).serverTicks(KegBlockEntity::serverTick), (Supplier)KEG_SUBS.get(wood))));
    public static final Map<CompatWood, Id<Block>> WINE_SHELVES = Helpers.mapOf(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_wine_shelf", () -> new WineShelfBlock(ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion().strength(4.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.WINE_SHELF))));
    public static final Map<CompatWood, Id<Block>> STOMPING_BARRELS = Helpers.mapOf(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_stomping_barrel", () -> new StompingBarrelBlock(ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion().strength(4.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.STOMPING_BARREL))));
    public static final Map<CompatWood, Id<Block>> BARREL_PRESSES = Helpers.mapOf(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_barrel_press", () -> new BarrelPressBlock(ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion().strength(4.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.BARREL_PRESS).ticks(BarrelPressBlockEntity::tick))));

    //Chromite
    public static final Map<CompatRock, Map<Ore.Grade, Id<Block>>> CHROMITE_ORES = Helpers.mapOf(CompatRock.class, rock ->
            Helpers.mapOf(Ore.Grade.class, grade ->
                    register((grade.name() + "_" + rock.name() + "_chromite_ore"), () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()))
            )
    );

    public static ExtendedProperties shelfProperties() {
        return ExtendedProperties.of().strength(0.3F).sound(SoundType.WOOD).noOcclusion().blockEntity(FLBlockEntities.FOOD_SHELF);
    }

    public static ExtendedProperties hangerProperties() {
        return ExtendedProperties.of().strength(0.3F).sound(SoundType.WOOD).noOcclusion().blockEntity(FLBlockEntities.HANGER);
    }

    public static ExtendedProperties jarbnetProperties() {
        return ExtendedProperties.of().strength(0.3F).sound(SoundType.WOOD).noOcclusion().randomTicks().lightLevel((s) -> (Boolean)s.getValue(JarbnetBlock.LIT) ? 11 : 0).blockEntity(FLBlockEntities.JARBNET);
    }


    private static <T extends Block> Id<T> registerNoItem(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function)null);
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }

    protected static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return new Id<>(RegistrationHelpers.registerBlock(CompatFLBlocks.BLOCKS, CompatFLItems.ITEMS, name, blockSupplier, blockItemFactory));
    }

    public record Id<T extends Block>(DeferredHolder<Block, T> holder) implements RegistryHolder<Block, T>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get().asItem();
        }
    }

}
