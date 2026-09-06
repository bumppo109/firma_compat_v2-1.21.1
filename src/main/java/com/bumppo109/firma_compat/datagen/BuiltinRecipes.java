package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompatHelpers;
import com.bumppo109.firma_compat.datagen.recipes.ModCraftingRecipes;
import com.bumppo109.firma_compat.datagen.recipes.ModRecipes;
import com.mojang.serialization.Codec;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.*;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.DataGenerationHelpers;
import net.dries007.tfc.util.Helpers;
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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class BuiltinRecipes extends RecipeProvider implements ModRecipes,
        ModCraftingRecipes
        /*ModRemoveRecipes,
        ModHeatRecipes,
        ModAlloyRecipes,
        ModCastingRecipe,
        ModAnvilRecipes,
        ModBarrelRecipes,
        ModChiselRecipes,
        ModQuernRecipes,
        ModWeldingRecipes,
        ModKnappingRecipes,
        ModPotRecipes*/

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
        craftingRecipes();
        /*
        removeRecipes();
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
         */

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
    }

    @Override
    public HolderLookup.Provider lookup()
    {
        return lookup;
    }

    private void stonecutting(ItemLike input, ItemLike output, int count)
    {
        add(
                "stonecutting",
                nameOf(output) + "_from_" + nameOf(input),
                new StonecutterRecipe(
                        "",
                        Ingredient.of(input),
                        new ItemStack(output, count)
                )
        );
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

    private void shapelessCondition(String name, ItemLike input, ItemLike result, int count, ICondition... conditions) {
        NonNullList<Ingredient> ingredientList = NonNullList.create();
        ingredientList.add(Ingredient.of(input));

        add(name, new ShapelessRecipe("firma_compat", CraftingBookCategory.MISC,
                new ItemStack(result, count),
                ingredientList
        ),conditions);
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