package com.bumppo109.firma_compat.block;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.blocks.DecorationBlockHolder;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.RockRopeAnchorBlock;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.RockDisplayCategory;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public enum CompatRock implements ModRegistryRock {
    STONE(Blocks.STONE, RockDisplayCategory.METAMORPHIC),
    DEEPSLATE(Blocks.DEEPSLATE, RockDisplayCategory.METAMORPHIC),
    ANDESITE(Blocks.ANDESITE, RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE),
    DIORITE(Blocks.DIORITE, RockDisplayCategory.INTERMEDIATE_IGNEOUS_INTRUSIVE),
    GRANITE(Blocks.GRANITE, RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE),
    TUFF(Blocks.TUFF, RockDisplayCategory.FELSIC_IGNEOUS_EXTRUSIVE),
    CALCITE(Blocks.CALCITE, RockDisplayCategory.SEDIMENTARY),
    DRIPSTONE(Blocks.DRIPSTONE_BLOCK, RockDisplayCategory.SEDIMENTARY),
    BASALT(Blocks.BASALT, RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE),
    BLACKSTONE(Blocks.BLACKSTONE, RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE),
    NETHERRACK(Blocks.NETHERRACK, RockDisplayCategory.METAMORPHIC),
    END_STONE(Blocks.END_STONE, RockDisplayCategory.METAMORPHIC)
    ;

    public static final CompatRock[] VALUES = values();
    private final String serializedName;
    private final Block base;
    private final RockDisplayCategory category;

    private CompatRock(Block base, RockDisplayCategory category) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.base = base;
        this.category = category;
    }

    public Item.Properties createItemProperties() {
        return new Item.Properties();
    }

    public Block base() {
        return this.base;
    }

    public RockDisplayCategory category() {
        return this.category;
    }

    public BlockAsset blockAsset() {
        return BlockAssets.get(this.base);
    }

    public CompatRockMaterial rockMaterial() {
        return switch (this) {
            case STONE -> CompatRockMaterial.STONE;
            case DEEPSLATE -> CompatRockMaterial.DEEPSLATE;
            case ANDESITE -> CompatRockMaterial.ANDESITE;
            case DIORITE -> CompatRockMaterial.DIORITE;
            case GRANITE -> CompatRockMaterial.GRANITE;
            case TUFF -> CompatRockMaterial.TUFF;
            case CALCITE -> CompatRockMaterial.CALCITE;
            case DRIPSTONE -> CompatRockMaterial.DRIPSTONE;
            case BASALT -> CompatRockMaterial.BASALT;
            case BLACKSTONE -> CompatRockMaterial.BLACKSTONE;
            case END_STONE -> CompatRockMaterial.END_STONE;
            case NETHERRACK -> CompatRockMaterial.NETHERRACK;

        };
    }

    public Supplier<? extends Block> getBlock(BlockType type) {
        return (Supplier)((Map)ModBlocks.ROCK_BLOCKS.get(this)).get(type);
    }

    public Supplier<? extends Block> getAnvil() {
        return (Supplier)ModBlocks.ROCK_ANVILS.get(this);
    }

    public Supplier<? extends SlabBlock> getSlab(BlockType type) {
        return ((DecorationBlockHolder)((Map)ModBlocks.ROCK_DECORATIONS.get(this)).get(type)).slab();
    }

    public Supplier<? extends StairBlock> getStair(BlockType type) {
        return ((DecorationBlockHolder)((Map)ModBlocks.ROCK_DECORATIONS.get(this)).get(type)).stair();
    }

    public Supplier<? extends WallBlock> getWall(BlockType type) {
        return ((DecorationBlockHolder)((Map)ModBlocks.ROCK_DECORATIONS.get(this)).get(type)).wall();
    }


    public Supplier<Item> brickItem() {
        return this.equals(NETHERRACK) ? () -> Items.NETHER_BRICK : ModItems.BRICK.get(this);
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public boolean canMakeAnvil() {
        return switch (this) {
            case CALCITE, DRIPSTONE, BASALT, END_STONE, NETHERRACK, TUFF -> false;
            default -> true;
        };
    }

    public static enum BlockType implements StringRepresentable {
        HARDENED((rock, self) -> new Block(properties(rock)), false),
        COBBLE((rock, self) -> new Block(properties(rock)), false),
        MOSSY_COBBLE((rock, self) -> new Block(properties(rock)), false),
        HARDENED_COBBLE((rock, self) -> new Block(properties(rock)), true),
        MOSSY_HARDENED_COBBLE((rock, self) -> new Block(properties(rock)), true),
        SPIKE((rock, self) -> new RockSpikeBlock(properties(rock).lightLevel(ModBlocks.lavaLoggedBlockEmission()), rock.getBlock(self.anchor())), false),
        LOOSE((rock, self) -> new LooseRockBlock(properties(rock)), false),
        MOSSY_LOOSE((rock, self) -> new LooseRockBlock(properties(rock)), false),
        ROPE_ANCHOR((rock, self) -> new RockRopeAnchorBlock(ExtendedProperties.of(properties(rock)), rock.getBlock(self.spike())), false);

        public static final BlockType[] VALUES = values();
        private final boolean variants;
        private final BiFunction<ModRegistryRock, BlockType, Block> blockFactory;
        private final String serializedName;

        /*
        public static BlockType valueOf(int i) {
            return i >= 0 && i < VALUES.length ? VALUES[i] : RAW;
        }
         */

        private static BlockBehaviour.Properties properties(ModRegistryRock rock) {
            return Properties.ofFullCopy(rock.base());
        }

        private BlockType(BiFunction<ModRegistryRock, BlockType, Block> blockFactory, boolean variants) {
            this.blockFactory = blockFactory;
            this.variants = variants;
            this.serializedName = this.name().toLowerCase(Locale.ROOT);
        }

        public boolean hasVariants() {
            return this.variants;
        }

        public boolean hasTFCVariants() {
            return this.equals(HARDENED_COBBLE) || this.equals(MOSSY_HARDENED_COBBLE);
        }

        public Block create(ModRegistryRock rock) {
            return (Block)this.blockFactory.apply(rock, this);
        }

        public SlabBlock createSlab(ModRegistryRock rock) {
            BlockBehaviour.Properties properties = Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5F, 10.0F).requiresCorrectToolForDrops();
            
            return new SlabBlock(properties);
        }

        public StairBlock createStairs(ModRegistryRock rock) {
            Supplier<BlockState> state = () -> ((Block)rock.getBlock(this).get()).defaultBlockState();
            BlockBehaviour.Properties properties = Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5F, 10.0F).requiresCorrectToolForDrops();
            
            return new StairBlock((BlockState)state.get(), properties);
        }

        public WallBlock createWall(ModRegistryRock rock) {
            BlockBehaviour.Properties properties = Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5F, 10.0F).requiresCorrectToolForDrops();

            return new WallBlock(properties);
        }

        public String getSerializedName() {
            return this.serializedName;
        }

        public boolean needsItem() {
            return this != ROPE_ANCHOR;
        }

        /*
        private @Nullable BlockType mossy() {
            BlockType var10000;
            switch (this.ordinal()) {
                case 3:
                case 9:
                    var10000 = MOSSY_COBBLE;
                    break;
                case 4:
                case 8:
                    var10000 = MOSSY_BRICKS;
                    break;
                case 5:
                case 6:
                case 7:
                default:
                    var10000 = null;
            }

            return var10000;
        }

         */

        private BlockType anchor() {
            return ROPE_ANCHOR;
        }

        private BlockType spike() {
            return SPIKE;
        }
    }
}
