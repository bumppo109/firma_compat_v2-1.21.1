package com.bumppo109.firma_compat.loot.loot_modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class ItemSwapModifier extends LootModifier {
    public static final MapCodec<ItemSwapModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).and(inst.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("from").forGetter(m -> m.fromItem),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("to").forGetter(m -> m.toItem)
            )).apply(inst, ItemSwapModifier::new)  // Method reference now matches array constructor
    );

    private final Item fromItem;
    private final Item toItem;

    // Constructor now takes array (matches super and codec).
    public ItemSwapModifier(LootItemCondition[] conditions, Item fromItem, Item toItem) {
        super(conditions);  // Now compatible—no List conversion needed
        this.fromItem = fromItem;
        this.toItem = toItem;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.replaceAll(stack -> {
            if (stack.is(this.fromItem)) {
                return new ItemStack(this.toItem, stack.getCount());
            }
            return stack;
        });
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}