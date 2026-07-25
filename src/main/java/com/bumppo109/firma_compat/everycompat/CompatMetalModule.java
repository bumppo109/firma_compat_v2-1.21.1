package com.bumppo109.firma_compat.everycompat;

import com.bumppo109.firma_compat.FirmaCompat;
import net.dries007.tfc.common.items.PropickItem;
import net.mehvahdjukaar.every_compat.api.ItemOnlyEntrySet;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.xelbayria.gems_realm.api.GemsRealmModule;
import net.xelbayria.gems_realm.api.MetalPaletteStrategies;
import net.xelbayria.gems_realm.api.set.metal.MetalType;
import net.xelbayria.gems_realm.api.set.metal.VanillaMetalTypes;

import java.util.function.Supplier;

import static net.xelbayria.gems_realm.api.set.metal.VanillaMetalChildKeys.INGOT;

public class CompatMetalModule extends GemsRealmModule {

    public final ItemOnlyEntrySet<MetalType, Item> DOUBLE_INGOT, SHEET, DOUBLE_SHEET, ROD;
    public final ItemOnlyEntrySet<MetalType, Item> SWORD_BLADE, PICKAXE_HEAD, SHOVEL_HEAD, AXE_HEAD, HOE_HEAD;
    /*
    public final ItemOnlyEntrySet<MetalType, Item> CHISEL_HEAD, HAMMER_HEAD, KNIFE_BLADE, PROPICK_HEAD, SAW_BLADE, SCYTHE_BLADE;
    public final ItemOnlyEntrySet<MetalType, Item> CHISEL, HAMMER, KNIFE, PROPICK, SAW, SCYTHE;
    public final ItemOnlyEntrySet<MetalType, Item> JAVELIN_HEAD, MACE_HEAD;
    public final ItemOnlyEntrySet<MetalType, Item> JAVELIN, MACE;
    public final ItemOnlyEntrySet<MetalType, Item> TUYERE;
     */

    public final ItemOnlyEntrySet<MetalType, Item> UNFINISHED_HELMET;
    public final ItemOnlyEntrySet<MetalType, Item> UNFINISHED_CHESTPLATE;
    public final ItemOnlyEntrySet<MetalType, Item> UNFINISHED_LEGGINGS;
    public final ItemOnlyEntrySet<MetalType, Item> UNFINISHED_BOOTS;

    public CompatMetalModule(String modId) {
        super(modId, FirmaCompat.MODID);

        Supplier<CreativeModeTab> tab = getModTab(FirmaCompat.MODID);

        ROD = ItemOnlyEntrySet.builder(MetalType.class, "rod",
                        getModItem("iron_rod"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_rod"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(ROD);

        DOUBLE_INGOT = ItemOnlyEntrySet.builder(MetalType.class, "double_ingot",
                        getModItem("iron_double_ingot"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_double_ingot"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(DOUBLE_INGOT);

        SHEET = ItemOnlyEntrySet.builder(MetalType.class, "sheet",
                        getModItem("iron_sheet"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_sheet"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(SHEET);

        DOUBLE_SHEET = ItemOnlyEntrySet.builder(MetalType.class, "double_sheet",
                        getModItem("iron_double_sheet"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTexture(modRes("item/iron_double_sheet"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(DOUBLE_SHEET);

        SWORD_BLADE = ItemOnlyEntrySet.builder(MetalType.class, "sword_blade",
                        getModItem("iron_sword_blade"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_sword_blade"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(SWORD_BLADE);

        PICKAXE_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "pickaxe_head",
                        getModItem("iron_pickaxe_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_pickaxe_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(PICKAXE_HEAD);

        SHOVEL_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "shovel_head",
                        getModItem("iron_shovel_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_shovel_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(SHOVEL_HEAD);

        AXE_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "axe_head",
                        getModItem("iron_axe_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_axe_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(AXE_HEAD);

        HOE_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "hoe_head",
                        getModItem("iron_hoe_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_hoe_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(HOE_HEAD);

        UNFINISHED_HELMET = ItemOnlyEntrySet.builder(MetalType.class, "unfinished_helmet",
                        getModItem("iron_unfinished_helmet"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_unfinished_helmet"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(UNFINISHED_HELMET);

        UNFINISHED_CHESTPLATE = ItemOnlyEntrySet.builder(MetalType.class, "unfinished_chestplate",
                        getModItem("iron_unfinished_chestplate"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_unfinished_chestplate"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(UNFINISHED_CHESTPLATE);

        UNFINISHED_LEGGINGS = ItemOnlyEntrySet.builder(MetalType.class, "unfinished_leggings",
                        getModItem("iron_unfinished_leggings"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_unfinished_leggings"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(UNFINISHED_LEGGINGS);

        UNFINISHED_BOOTS = ItemOnlyEntrySet.builder(MetalType.class, "unfinished_boots",
                        getModItem("iron_unfinished_boots"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_unfinished_boots"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(UNFINISHED_BOOTS);

        PROPICK_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "propick_head",
                        getModItem("iron_propick_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTexture(modRes("item/iron_propick_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(PROPICK_HEAD);

        CHISEL_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "chisel_head",
                        getModItem("iron_chisel_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTexture(modRes("item/iron_chisel_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(CHISEL_HEAD);

        SAW_BLADE = ItemOnlyEntrySet.builder(MetalType.class, "saw_blade",
                        getModItem("iron_saw_blade"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTexture(modRes("item/iron_saw_blade"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(SAW_BLADE);

        SCYTHE_BLADE = ItemOnlyEntrySet.builder(MetalType.class, "scythe_blade",
                        getModItem("iron_scythe_blade"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTexture(modRes("item/iron_scythe_blade"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(SCYTHE_BLADE);

        HAMMER_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "hammer_head",
                        getModItem("iron_hammer_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTexture(modRes("item/iron_hammer_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(HAMMER_HEAD);

        JAVELIN_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "javelin_head",
                        getModItem("iron_javelin_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTexture(modRes("item/iron_javelin_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(JAVELIN_HEAD);

        KNIFE_BLADE = ItemOnlyEntrySet.builder(MetalType.class, "knife_blade",
                        getModItem("iron_knife_blade"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTexture(modRes("item/iron_knife_blade"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(KNIFE_BLADE);

        KNIFE = ItemOnlyEntrySet.builder(MetalType.class, "knife",
                        getModItem("iron_knife"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTextureM(modRes("item/iron_knife"), modRes("template/item/knife_mask"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(KNIFE);

        PROPICK = ItemOnlyEntrySet.builder(MetalType.class, "propick",
                        getModItem("iron_propick"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTextureM(modRes("item/iron_propick"), modRes("template/item/propick_mask"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(PROPICK);

        SCYTHE = ItemOnlyEntrySet.builder(MetalType.class, "scythe",
                        getModItem("iron_scythe"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTextureM(modRes("item/iron_scythe"), modRes("template/item/scythe_mask"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(SCYTHE);

        HAMMER = ItemOnlyEntrySet.builder(MetalType.class, "hammer",
                        getModItem("iron_hammer"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTextureM(modRes("item/iron_hammer"), modRes("template/item/hammer_mask"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(HAMMER);

        CHISEL = ItemOnlyEntrySet.builder(MetalType.class, "chisel",
                        getModItem("iron_chisel"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTextureM(modRes("item/iron_chisel"), modRes("template/item/chisel_mask"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(CHISEL);

        JAVELIN = ItemOnlyEntrySet.builder(MetalType.class, "javelin",
                        getModItem("iron_javelin"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTextureM(modRes("item/iron_javelin"), modRes("template/item/javelin_mask"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(JAVELIN);

        MACE = ItemOnlyEntrySet.builder(MetalType.class, "mace",
                        getModItem("iron_mace"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTextureM(modRes("item/iron_mace"), modRes("template/item/mace_mask"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(MACE);

        SAW = ItemOnlyEntrySet.builder(MetalType.class, "saw",
                        getModItem("iron_saw"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTextureM(modRes("item/iron_saw"), modRes("template/item/saw_mask"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(SAW);

        TUYERE = ItemOnlyEntrySet.builder(MetalType.class, "tuyere",
                        getModItem("iron_tuyere"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTexture(modRes("item/iron_tuyere"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(TUYERE);

        MACE_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "mace_head",
                        getModItem("iron_mace_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT)
                .addTexture(modRes("item/iron_mace_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("firmalife:.*")
                .setTab(tab)
                .build();
        this.addEntry(MACE_HEAD);
    }

    @Override
    public boolean isEntryAlreadyRegistered(String entrySetId, ResourceLocation blockId, BlockType blockType, Registry<?> registry) {
        return false;
    }
}