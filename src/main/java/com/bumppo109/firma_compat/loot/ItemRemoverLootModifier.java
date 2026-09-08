package com.bumppo109.firma_compat.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.stream.Collectors;

public class ItemRemoverLootModifier extends LootModifier {
    public static final MapCodec<ItemRemoverLootModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> LootModifier.codecStart(instance).and(
            // Add a field for the item to remove (e.g., "minecraft:diamond")
            net.minecraft.core.registries.BuiltInRegistries.ITEM.byNameCodec().fieldOf("item_to_remove").forGetter(m -> m.itemToRemove)
    ).apply(instance, ItemRemoverLootModifier::new));

    private final Item itemToRemove;

    public ItemRemoverLootModifier(LootItemCondition[] conditions, Item itemToRemove) {
        super(conditions);
        this.itemToRemove = itemToRemove;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Filter out all stacks matching the item to remove
        return generatedLoot.stream()
                .filter(stack -> !stack.is(this.itemToRemove))
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }
}