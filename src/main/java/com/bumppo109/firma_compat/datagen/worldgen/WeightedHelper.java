package com.bumppo109.firma_compat.datagen.worldgen;

import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.util.collections.Weighted;

import java.util.List;

public class WeightedHelper<E> extends Weighted<E> {
    private final List<Pair<E, Double>> rawWeights;

    public WeightedHelper(List<Pair<E, Double>> rawWeightedList) {
        super(rawWeightedList); // Builds the cumulative map as normal
        this.rawWeights = rawWeightedList; // Store original raw weights
    }

    // Override to return the original raw weights (not cumulative)
    @Override
    public List<Pair<E, Double>> weightedValues() {
        return rawWeights;
    }

    // Optional: if you need to recalculate raw deltas from cumulative (if not storing raw)
    public List<Pair<E, Double>> getRawDeltas() {
        List<Pair<E, Double>> cumulative = super.weightedValues();
        return cumulative.stream().reduce(
                new java.util.ArrayList<>(),
                (list, pair) -> {
                    double prev = list.isEmpty() ? 0.0 : list.get(list.size() - 1).getSecond();
                    list.add(Pair.of(pair.getFirst(), pair.getSecond() - prev));
                    return list;
                },
                (a, b) -> a
        );
    }
}
