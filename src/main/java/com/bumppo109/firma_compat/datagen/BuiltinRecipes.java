package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.addon.firmalife.modules.CompatFLBlocks;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.CompatWoodMaterial;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.datagen.recipe.*;
import com.bumppo109.firma_compat.item.ModItems;
import com.eerussianguy.firmalife.common.items.FLItems;
import com.mojang.serialization.Codec;
import com.therighthon.rnr.common.block.RNRBlocks;
import com.therighthon.rnr.common.item.RNRItems;
import com.therighthon.rnr.common.recipe.BlockModRecipe;
import com.therighthon.rnr.common.recipe.MattockRecipe;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.*;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.DataGenerationHelpers;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BuiltinRecipes extends RecipeProvider implements ModRecipes,
        ModRemoveRecipes,
        ModCraftingRecipes,
        ModHeatRecipes,
        ModAlloyRecipes,
        ModCastingRecipe,
        ModAnvilRecipes,
        ModBarrelRecipes,
        ModChiselRecipes,
        ModQuernRecipes,
        ModWeldingRecipes,
        ModKnappingRecipes,
        ModPotRecipes

{
    final Set<ResourceLocation> removedRecipes = new HashSet<>();
    ModLoadedCondition flLoaded = new ModLoadedCondition("firmalife");
    ModLoadedCondition rnrLoaded = new ModLoadedCondition("rnr");
    ModLoadedCondition beneathLoaded = new ModLoadedCondition("beneath");

    final Codec<Unit> emptyRecipeCodec = Codec.STRING.fieldOf("type")
            .codec()
            .listOf()
            .fieldOf("neoforge:conditions")
            .xmap(l -> Unit.INSTANCE, r -> List.of("neoforge:false"))
            .codec();

    private RecipeOutput output;
    private HolderLookup.Provider lookup;
    private final List<BuiltinItemHeat.MeltingRecipe> meltingRecipes;
    final CompletableFuture<?> before;

    public BuiltinRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, CompletableFuture<?> before, BuiltinItemHeat itemHeat)
    {
        super(output, registries);
        this.before = CompletableFuture.allOf(before, itemHeat.output());
        this.meltingRecipes = itemHeat.meltingRecipes;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider lookup)
    {

        this.lookup = lookup;
        return before.thenCompose(v -> CompletableFuture.allOf(
                super.run(output, lookup),
                CompletableFuture.allOf(removedRecipes
                        .stream()
                        .map(id -> DataProvider.saveStable(output, lookup, emptyRecipeCodec, Unit.INSTANCE, recipePathProvider.json(id)))
                        .toArray(CompletableFuture[]::new))
        ));
    }

    @Override
    public void buildRecipes(RecipeOutput output)
    {
        this.output = output;
        removeRecipes();
        craftingRecipes();
        heatRecipes();
        alloyRecipes();
        castingRecipes();
        anvilRecipes();
        barrelRecipes();
        chiselRecipes();
        quernRecipes();
        weldingRecipes();
        knappingRecipes();
        potRecipes();

        // Heat Recipes from Melting
        for (BuiltinItemHeat.MeltingRecipe melt : meltingRecipes)
        {
            add(nameOf(melt.item()), new HeatingRecipe(
                    Ingredient.of(melt.item()),
                    ItemStackProvider.empty(),
                    new FluidStack(fluidOf(melt.metal()), melt.units()),
                    temperatureOf(melt.metal()),
                    false
            ));
        }

    //Landslide Recipe
        add(new LandslideRecipe(BlockIngredient.of(Blocks.RED_SAND), Blocks.RED_SAND.defaultBlockState()));
        add(new LandslideRecipe(BlockIngredient.of(Blocks.PACKED_MUD), Blocks.PACKED_MUD.defaultBlockState()));
        add("grass_block", new LandslideRecipe(BlockIngredient.of(Blocks.GRASS_BLOCK), Blocks.DIRT.defaultBlockState()));
        add("podzol", new LandslideRecipe(BlockIngredient.of(Blocks.PODZOL), Blocks.DIRT.defaultBlockState()));
        add("mycelium", new LandslideRecipe(BlockIngredient.of(Blocks.MYCELIUM), Blocks.DIRT.defaultBlockState()));
        add("dirt_path", new LandslideRecipe(BlockIngredient.of(Blocks.DIRT_PATH), Blocks.DIRT.defaultBlockState()));
        add("coarse_dirt", new LandslideRecipe(BlockIngredient.of(Blocks.COARSE_DIRT), Blocks.DIRT.defaultBlockState()));
        add("farmland", new LandslideRecipe(BlockIngredient.of(Blocks.FARMLAND), Blocks.DIRT.defaultBlockState()));
        add("rooted_dirt", new LandslideRecipe(BlockIngredient.of(Blocks.ROOTED_DIRT), Blocks.DIRT.defaultBlockState()));

        add(new LandslideRecipe(BlockIngredient.of(ModBlocks.CLAY_DIRT.get()), ModBlocks.CLAY_DIRT.get().defaultBlockState()));
        add("clay_grass_block", new LandslideRecipe(BlockIngredient.of(ModBlocks.CLAY_GRASS_BLOCK.get()), ModBlocks.CLAY_DIRT.get().defaultBlockState()));
        add("clay_podzol", new LandslideRecipe(BlockIngredient.of(ModBlocks.CLAY_PODZOL.get()), ModBlocks.CLAY_DIRT.get().defaultBlockState()));
        add(new LandslideRecipe(BlockIngredient.of(ModBlocks.KAOLIN_CLAY_DIRT.get()), ModBlocks.KAOLIN_CLAY_DIRT.get().defaultBlockState()));
        add("kaolin_clay_grass_block", new LandslideRecipe(BlockIngredient.of(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get()), ModBlocks.KAOLIN_CLAY_DIRT.get().defaultBlockState()));
        add("kaolin_clay_podzol", new LandslideRecipe(BlockIngredient.of(ModBlocks.KAOLIN_CLAY_PODZOL.get()), ModBlocks.KAOLIN_CLAY_DIRT.get().defaultBlockState()));

        add(new LandslideRecipe(BlockIngredient.of(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get()), ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get().defaultBlockState()));
        add(new LandslideRecipe(BlockIngredient.of(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get()), ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get().defaultBlockState()));
        add(new LandslideRecipe(BlockIngredient.of(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get()), ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get().defaultBlockState()));
        add(new LandslideRecipe(BlockIngredient.of(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get()), ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get().defaultBlockState()));


    // =============== Firmalife ================
        for (CompatWood wood : CompatWood.VALUES) {
            CompatWoodMaterial material = wood.compatWoodMaterial();

            Block foodShelfBlock = CompatFLBlocks.FOOD_SHELVES.get(wood).get();
            Block hangerBlock = CompatFLBlocks.HANGERS.get(wood).get();
            Block jarbnetBlock = CompatFLBlocks.JARBNETS.get(wood).get();
            Block kegBlock = CompatFLBlocks.KEGS.get(wood).get();
            Block stompBarrelBlock = CompatFLBlocks.STOMPING_BARRELS.get(wood).get();
            Block barrelPressBlock = CompatFLBlocks.BARREL_PRESSES.get(wood).get();
            Block wineShelfBlock = CompatFLBlocks.WINE_SHELVES.get(wood).get();

            shapedCondition(Map.of('L', Ingredient.of(ModItems.LUMBER.get(wood).get()), 'P', Ingredient.of(material.planks())), List.of("PPP", "LLL", "PPP"), foodShelfBlock, flLoaded);
            shapedCondition(Map.of('S', Ingredient.of(Tags.Items.STRINGS), 'P', Ingredient.of(material.planks())), List.of("PPP", " S ", " S "), hangerBlock, flLoaded);
            shapedCondition(Map.of('B', Ingredient.of(TFCItems.METAL_ITEMS.get(Metal.BRASS).get(Metal.ItemType.ROD).get()), 'W', Ingredient.of(material.log()), 'L', Ingredient.of(ModItems.LUMBER.get(wood).get())), List.of("W  ", "BLL", "W  "), jarbnetBlock, flLoaded);
            shapedCondition(Map.of('W', Ingredient.of(material.log()), 'S', Ingredient.of(FLItems.BARREL_STAVE), 'G', Ingredient.of(TFCItems.GLUE)), List.of("WSW", "SGS", "WSW"), kegBlock, flLoaded);
            shapedCondition(Map.of('L', Ingredient.of(ModItems.LUMBER.get(wood).get()), 'G', Ingredient.of(TFCItems.GLUE)), List.of("LGL", "LLL", "GGG"), stompBarrelBlock, flLoaded);
            shapedCondition(Map.of('B', Ingredient.of(stompBarrelBlock), 'R', Ingredient.of(TFCItems.METAL_ITEMS.get(Metal.WROUGHT_IRON).get(Metal.ItemType.ROD).get()), 'S', Ingredient.of(TFCItems.METAL_ITEMS.get(Metal.WROUGHT_IRON).get(Metal.ItemType.SHEET).get()), 'M', Ingredient.of(TFCItems.BRASS_MECHANISMS)), List.of("BR ", "SM "), barrelPressBlock, flLoaded);
            shapedCondition(Map.of('L', Ingredient.of(FLItems.TREATED_LUMBER.get()), 'W', Ingredient.of(material.log())), List.of("WLW", "WLW", "WLW"), wineShelfBlock, flLoaded);
        }
    }

    @Override
    public HolderLookup.Provider lookup()
    {
        return lookup;
    }

    private void mattock(BlockIngredient in, BlockState out, Holder<ChiselMode> mode, String suffix) {
        this.add("mattock", this.nameOf(out.getBlock().asItem()) + (Objects.equals(suffix, "") ? "" : "_") + suffix, new MattockRecipe(in, out, (ChiselMode)mode.value(), ItemStackProvider.empty()));
    }

    private void mattock(BlockIngredient in, BlockState out, Holder<ChiselMode> mode, String suffix, ICondition... conditions) {
        this.add("mattock",
                this.nameOf(out.getBlock().asItem()) + (Objects.equals(suffix, "") ? "" : "_") + suffix,
                new MattockRecipe(in, out, mode.value(), ItemStackProvider.empty()),
                conditions);
    }

    private void chisel(BlockIngredient in, BlockState out, Holder<ChiselMode> mode, String suffix, ICondition... conditions) {
        this.add("chisel",
                this.nameOf(out.getBlock().asItem()) + (Objects.equals(suffix, "") ? "" : "_") + suffix,
                new ChiselRecipe(in, out, mode.value(), ItemStackProvider.empty()),
                conditions);
    }

    private void chisel(BlockIngredient in, BlockState out, Item extra, Holder<ChiselMode> mode, String suffix, ICondition... conditions) {
        this.add("chisel",
                this.nameOf(out.getBlock().asItem()) + (Objects.equals(suffix, "") ? "" : "_") + suffix,
                new ChiselRecipe(in, out, mode.value(), ItemStackProvider.of(extra)),
                conditions);
    }

    private void collapse(String suffix, BlockIngredient in, BlockState out, ICondition... conditions) {
        this.add(this.nameOf(out.getBlock().asItem()) + (Objects.equals(suffix, "") ? "" : "_") + suffix,
                new CollapseRecipe(in, out), conditions);
    }

    private void shapedCondition(Map<Character, Ingredient> ingredientMap, List<String> pattern, ItemLike result, ICondition... conditions) {
        add(new ShapedRecipe("firma_compat", CraftingBookCategory.MISC,
                ShapedRecipePattern.of(ingredientMap, pattern),
                new ItemStack(result)),conditions);
    }

    /**
     * @return A builder for a new recipe with a name inferred from the output.
     */
    private DataGenerationHelpers.Builder recipe()
    {
        return new DataGenerationHelpers.Builder((name, r) -> {
            if (name != null) add(name, r);
            else add(r);
        });
    }

    @Override
    public void add(String prefix, String name, Recipe<?> recipe)
    {
        output.accept(FirmaCompatHelpers.modIdentifier((prefix + "/" + name).toLowerCase(Locale.ROOT)), recipe, null);
    }

    public void add(Recipe<?> recipe, ICondition... conditions) {
        // Uses the default chain: add(nameOf(recipe)) → add(prefix, name, recipe)
        add(nameOf(recipe), recipe, conditions);
    }

    public void add(String name, Recipe<?> recipe, ICondition... conditions) {
        // Uses: add(prefix from type, name, recipe, conditions)
        add(Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType()), "No recipe type").getPath(),
                name,
                recipe,
                conditions);
    }

    public void add(String prefix, String name, Recipe<?> recipe, ICondition... conditions) {
        ResourceLocation id = FirmaCompatHelpers.modIdentifier((prefix + "/" + name).toLowerCase(Locale.ROOT));
        this.output.accept(id, recipe, null, conditions);  // ← passes varargs conditions
    }

    @Override
    public void remove(String... names)
    {
        for (String name : names)
        {
            final ResourceLocation id = Helpers.identifierMC(name);
            removedRecipes.add(id);
        }
    }

    @Override
    public void removeTFC(String... names)
    {
        for (String name : names)
        {
            final ResourceLocation id = Helpers.identifier(name);
            removedRecipes.add(id);
        }
    }

    //TODO - ?
    @Override
    public void replace(String name, Recipe<?> recipe) {}

}