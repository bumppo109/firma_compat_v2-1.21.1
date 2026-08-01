package com.bumppo109.firma_compat.addon;

import com.google.gson.*;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EveryCompatHelper {

    public static Map<ResourceLocation, JsonObject> getRecipeJsons(ResourceManager manager) {
        Map<ResourceLocation, JsonObject> recipes = new HashMap<>();

        Map<ResourceLocation, Resource> resources =
                manager.listResources(
                        "recipes",
                        id -> id.getPath().endsWith(".json")
                );

        Gson gson = new Gson();

        for (var entry : resources.entrySet()) {
            try (Reader reader = entry.getValue().openAsReader()) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                        entry.getKey().getNamespace(),
                        entry.getKey().getPath()
                                .substring("recipes/".length(), entry.getKey().getPath().length() - ".json".length())
                );

                recipes.put(recipeId, json);

            } catch (Exception ignored) {
            }
        }

        return recipes;
    }

    public static List<ResourceLocation> getRecipesCreatingItem(
            ResourceManager manager,
            ItemLike target
    ) {
        List<ResourceLocation> ids = new ArrayList<>();

        ResourceLocation targetId = Utils.getID(target.asItem());

        Map<ResourceLocation, Resource> resources =
                manager.listResources(
                        "recipes",
                        id -> id.getPath().endsWith(".json")
                );

        for (var entry : resources.entrySet()) {
            try (Reader reader = entry.getValue().openAsReader()) {

                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                if (!json.has("result")) {
                    continue;
                }

                JsonElement result = json.get("result");

                String resultId = null;

                // Handles:
                // "result": "minecraft:oak_trapdoor"
                if (result.isJsonPrimitive()) {
                    resultId = result.getAsString();
                }

                // Handles:
                // "result": {"id":"minecraft:oak_trapdoor"}
                // "result": {"item":"minecraft:oak_trapdoor"}
                else if (result.isJsonObject()) {
                    JsonObject resultObj = result.getAsJsonObject();

                    if (resultObj.has("id")) {
                        resultId = resultObj.get("id").getAsString();
                    } else if (resultObj.has("item")) {
                        resultId = resultObj.get("item").getAsString();
                    }
                }

                if (targetId.toString().equals(resultId)) {
                    ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                            entry.getKey().getNamespace(),
                            entry.getKey().getPath()
                                    .substring(
                                            "recipes/".length(),
                                            entry.getKey().getPath().length() - ".json".length()
                                    )
                    );

                    ids.add(recipeId);
                }

            } catch (Exception e) {
                // optionally log malformed recipe JSONs
            }
        }

        return ids;
    }

    public static void generateRecipeDisableCondition(
            ResourceSink sink,
            ResourceLocation recipeId
    ) {
        JsonObject condition = new JsonObject();

        condition.addProperty(
                "condition",
                "minecraft:false"
        );

        ResourceLocation outputId = ResourceLocation.fromNamespaceAndPath(
                recipeId.getNamespace(),
                recipeId.getPath()
        );

        sink.addJson(
                outputId,
                condition,
                ResType.RECIPES
        );
    }

    public static void replaceJsonString(
            JsonElement element,
            String from,
            String to
    ) {
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                JsonElement value = entry.getValue();

                if (value.isJsonPrimitive()
                        && value.getAsJsonPrimitive().isString()
                        && value.getAsString().equals(from)) {

                    entry.setValue(new JsonPrimitive(to));

                } else {
                    replaceJsonString(value, from, to);
                }
            }

        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();

            for (int i = 0; i < array.size(); i++) {
                JsonElement value = array.get(i);

                if (value.isJsonPrimitive()
                        && value.getAsJsonPrimitive().isString()
                        && value.getAsString().equals(from)) {

                    array.set(i, new JsonPrimitive(to));

                } else {
                    replaceJsonString(value, from, to);
                }
            }
        }
    }
}