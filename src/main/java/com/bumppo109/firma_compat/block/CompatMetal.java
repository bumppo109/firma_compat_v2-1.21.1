package com.bumppo109.firma_compat.block;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

import com.bumppo109.firma_compat.materials.MetalMaterial;
import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.common.TFCTags.Blocks;
import net.dries007.tfc.common.items.ChiselItem;
import net.dries007.tfc.common.items.HammerItem;
import net.dries007.tfc.common.items.JavelinItem;
import net.dries007.tfc.common.items.PropickItem;
import net.dries007.tfc.common.items.ScytheItem;
import net.dries007.tfc.common.items.TFCMaceItem;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public enum CompatMetal implements StringRepresentable, ModRegistryMetal {
    NETHERITE(2167299, MapColor.COLOR_BLACK, Rarity.COMMON, PartType.ALL, TFCTiers.BLACK_STEEL, TFCArmorMaterials.BLACK_STEEL),
    SCRAP_NETHERITE(6897943, MapColor.COLOR_BROWN, Rarity.COMMON, PartType.DEFAULT)
    ;

    private final String serializedName;
    private final PartType partType;
    private final @Nullable LevelTier toolTier;
    private final TFCArmorMaterials.@Nullable Id armorMaterial;
    private final MapColor mapColor;
    private final Rarity rarity;
    private final int color;

    private CompatMetal(int color, MapColor mapColor, Rarity rarity, PartType partType) {
        this(color, mapColor, rarity, partType, (LevelTier)null, (TFCArmorMaterials.Id)null);
    }

    private CompatMetal(int color, MapColor mapColor, Rarity rarity, LevelTier toolTier, TFCArmorMaterials.Id armorTier) {
        this(color, mapColor, rarity, PartType.ALL, toolTier, armorTier);
    }

    private CompatMetal(int color, MapColor mapColor, Rarity rarity) {
        this(color, mapColor, rarity, PartType.ALL, null, null);
    }

    private CompatMetal(int color, MapColor mapColor, Rarity rarity, @Nullable PartType partType, LevelTier toolTier, TFCArmorMaterials.Id armorTier) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.toolTier = toolTier;
        this.armorMaterial = armorTier;
        this.rarity = rarity;
        this.mapColor = mapColor;
        this.color = color;
        this.partType = partType;
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public MetalMaterial getMetalMaterial() {
        return MetalMaterial.valueOf(this.name());
    }

    public int getColor() {
        return this.color;
    }

    public Rarity rarity() {
        return this.rarity;
    }

    public boolean defaultParts() {
        return true;
    }

    public boolean allParts() {
        return this.partType == CompatMetal.PartType.ALL;
    }

    public LevelTier toolTier() {
        return (LevelTier)Objects.requireNonNull(this.toolTier, "Tried to get non-existent tier from " + this.name());
    }

    public Holder<ArmorMaterial> armorMaterial() {
        return ((TFCArmorMaterials.Id)Objects.requireNonNull(this.armorMaterial)).holder();
    }

    public int armorDurability(ArmorItem.Type type) {
        Objects.requireNonNull(this.armorMaterial);
        int var10000;
        switch (type) {
            case HELMET:
                var10000 = this.armorMaterial.headDamage();
                break;
            case BODY:
            case CHESTPLATE:
                var10000 = this.armorMaterial.chestDamage();
                break;
            case LEGGINGS:
                var10000 = this.armorMaterial.legDamage();
                break;
            case BOOTS:
                var10000 = this.armorMaterial.feetDamage();
                break;
            default:
                throw new MatchException((String)null, (Throwable)null);
        }

        return var10000;
    }

    public MapColor mapColor() {
        return this.mapColor;
    }

    public int tier() {
        return this.toolTier != null ? this.toolTier.level() : 0;
    }

    public static enum ItemType {
        NUGGET(CompatMetal.PartType.DEFAULT, false),
        DOUBLE_INGOT(CompatMetal.PartType.DEFAULT, false),
        SHEET(CompatMetal.PartType.DEFAULT, false),
        DOUBLE_SHEET(CompatMetal.PartType.DEFAULT, false),
        ROD(CompatMetal.PartType.DEFAULT, false),
        TUYERE(CompatMetal.PartType.ALL, (CompatMetal) -> new TieredItem(CompatMetal.toolTier(), base(CompatMetal))),
        PICKAXE_HEAD(CompatMetal.PartType.ALL, true),
        PROPICK(CompatMetal.PartType.ALL, (CompatMetal) -> new PropickItem(CompatMetal.toolTier(), tool(CompatMetal, 0.5F, -2.8F))),
        PROPICK_HEAD(CompatMetal.PartType.ALL, true),
        AXE_HEAD(CompatMetal.PartType.ALL, true),
        SHOVEL_HEAD(CompatMetal.PartType.ALL, true),
        HOE_HEAD(CompatMetal.PartType.ALL, true),
        CHISEL(CompatMetal.PartType.ALL, (CompatMetal) -> new ChiselItem(CompatMetal.toolTier(), tool(CompatMetal, 0.27F, 1.5F))),
        CHISEL_HEAD(CompatMetal.PartType.ALL, true),
        HAMMER(CompatMetal.PartType.ALL, (CompatMetal) -> new HammerItem(CompatMetal.toolTier(), tool(CompatMetal, 1.0F, -3.0F))),
        HAMMER_HEAD(CompatMetal.PartType.ALL, true),
        SAW(CompatMetal.PartType.ALL, (CompatMetal) -> new AxeItem(CompatMetal.toolTier(), tool(CompatMetal, 0.5F, -3.0F))),
        SAW_BLADE(CompatMetal.PartType.ALL, true),
        JAVELIN(CompatMetal.PartType.ALL, (CompatMetal) -> new JavelinItem(CompatMetal.toolTier(), tool(CompatMetal, 0.7F, -2.6F))),
        JAVELIN_HEAD(CompatMetal.PartType.ALL, true),
        SWORD_BLADE(CompatMetal.PartType.ALL, true),
        MACE(CompatMetal.PartType.ALL, (CompatMetal) -> new TFCMaceItem(tool(CompatMetal, 1.3F, -3.4F).durability(CompatMetal.toolTier().getUses()))),
        MACE_HEAD(CompatMetal.PartType.ALL, true),
        KNIFE(CompatMetal.PartType.ALL, (CompatMetal) -> new ToolItem(CompatMetal.toolTier(), Blocks.MINEABLE_WITH_KNIFE, tool(CompatMetal, 0.6F, -2.0F))),
        KNIFE_BLADE(CompatMetal.PartType.ALL, true),
        SCYTHE(CompatMetal.PartType.ALL, (CompatMetal) -> new ScytheItem(CompatMetal.toolTier(), tool(CompatMetal, 0.7F, -3.2F))),
        SCYTHE_BLADE(CompatMetal.PartType.ALL, true),
        UNFINISHED_HELMET(CompatMetal.PartType.ALL, false),
        UNFINISHED_CHESTPLATE(CompatMetal.PartType.ALL, false),
        UNFINISHED_GREAVES(CompatMetal.PartType.ALL, false),
        UNFINISHED_BOOTS(CompatMetal.PartType.ALL, false),
        ;

        private final Function<ModRegistryMetal, Item> itemFactory;
        private final String serializedName;
        private final PartType type;
        private final boolean mold;

        private static Item.Properties base(ModRegistryMetal CompatMetal) {
            return (new Item.Properties()).rarity(CompatMetal.rarity());
        }

        private static Item.Properties tool(ModRegistryMetal CompatMetal, float attackDamageFactor, float attackSpeed) {
            return base(CompatMetal).attributes(ToolItem.productAttributes(CompatMetal.toolTier(), attackDamageFactor, attackSpeed));
        }

        private static Function<ModRegistryMetal, Item> armor(ArmorItem.Type type) {
            return (CompatMetal) -> new ArmorItem(CompatMetal.armorMaterial(), type, base(CompatMetal).durability(CompatMetal.armorDurability(type)));
        }

        private ItemType(PartType type, boolean mold) {
            this(type, mold, (CompatMetal) -> new Item(base(CompatMetal)));
        }

        private ItemType(PartType type, Function<ModRegistryMetal, Item> itemFactory) {
            this(type, false, itemFactory);
        }

        private ItemType(PartType type, boolean mold, Function<ModRegistryMetal, Item> itemFactory) {
            this.serializedName = this.name().toLowerCase(Locale.ROOT);
            this.type = type;
            this.mold = mold;
            this.itemFactory = itemFactory;
        }

        public String getSerializedName() {
            return this.serializedName;
        }

        public Item create(ModRegistryMetal CompatMetal) {
            return (Item)this.itemFactory.apply(CompatMetal);
        }

        public boolean has(CompatMetal CompatMetal) {
            return this.type.hasCompatMetal(CompatMetal.partType);
        }

        public boolean hasMold() {
            return this.mold;
        }

        public boolean isCommonTagPart() {
            return this.type == CompatMetal.PartType.DEFAULT;
        }
    }

    public static boolean makeItem(MetalMaterial material, CompatMetal.ItemType itemType) {
        return switch (itemType) {
            case PICKAXE_HEAD, PROPICK_HEAD, PROPICK, TUYERE, KNIFE, KNIFE_BLADE, SCYTHE, SCYTHE_BLADE, CHISEL, CHISEL_HEAD, HAMMER, HAMMER_HEAD, SAW, SAW_BLADE -> material.pickaxe() != null;
            case SHOVEL_HEAD -> material.shovel() != null;
            case AXE_HEAD -> material.axe() != null;
            case HOE_HEAD -> material.hoe() != null;
            case SWORD_BLADE, JAVELIN, JAVELIN_HEAD, MACE, MACE_HEAD -> material.sword() != null;
            case UNFINISHED_HELMET -> material.helmet() != null;
            case UNFINISHED_CHESTPLATE -> material.chestplate() != null;
            case UNFINISHED_GREAVES -> material.leggings() != null;
            case UNFINISHED_BOOTS -> material.boots() != null;
            default -> true;
        };
    }

    static enum PartType {
        DEFAULT,
        ALL;

        boolean hasCompatMetal(PartType partType) {
            return partType.ordinal() >= this.ordinal();
        }
    }

    public static int hexToInt(String hexColor) {
        // Remove # prefix if present
        String cleanHex = hexColor.startsWith("#") ? hexColor.substring(1) : hexColor;

        // Validate length (must be 6 or 8 digits)
        if (cleanHex.length() != 6 && cleanHex.length() != 8) {
            throw new IllegalArgumentException("Invalid hex color: must be 6 or 8 characters (RRGGBB or AARRGGBB), got: " + hexColor);
        }

        try {
            // Parse as unsigned int (base 16)
            long parsed = Long.parseLong(cleanHex, 16);

            // If input was 6 digits (RGB), add full opacity (FF alpha)
            if (cleanHex.length() == 6) {
                parsed = (parsed & 0xFFFFFF) | 0xFF000000L;
            }

            return (int) parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex color format: " + hexColor, e);
        }
    }
}