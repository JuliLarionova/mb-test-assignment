package com.mobiquity.utils;

import com.mobiquity.packer.model.Package;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BestPackageOptionsUtils {

    public static String suitableItemIndexes(Package pack) {
        val maxWeight = pack.getMaxWeight();
        val packageItems = pack.getPackageItems();
        List<Package.Item> itemsWithSuitableWeight = packageItems.stream()
                .filter(c -> !(c.getWeight().compareTo(maxWeight) > 0))
                .collect(Collectors.toList());
        List<List<Package.Item>> itemsCombination = findAllPossibleItemsCombinations(itemsWithSuitableWeight, maxWeight);

        return findCombinationToPack(itemsCombination).stream()
                .map(c->c.getIndex().toString())
                .collect(Collectors.joining(","));
    }

    private static List<Package.Item> findCombinationToPack(List<List<Package.Item>> itemsCombination) {
        List<Package.Item> bestItemsToPack = new ArrayList<>();
        double bestWeight = 0.0;
        double bestCost =  0.0;

        for (List<Package.Item> comb: itemsCombination) {
            double sumCostOfCombination = comb.stream().mapToDouble(Package.Item::getCost).sum();
            double sumWeightOfCombination = comb.stream().mapToDouble(Package.Item::getWeight).sum();
            if (bestCost < sumCostOfCombination) {
                bestWeight = sumWeightOfCombination;
                bestCost = sumCostOfCombination;
                bestItemsToPack = comb;
            }
            if (bestCost == sumCostOfCombination) {
                if (sumWeightOfCombination < bestWeight) {
                    bestWeight = sumWeightOfCombination;
                    bestItemsToPack = comb;
                }
            }
        }
        return bestItemsToPack;
    }

    private static List<List<Package.Item>> findAllPossibleItemsCombinations(List<Package.Item> itemsList, Double maxWeight) {
        final List<List<Package.Item>> allPossibleCombinations = new ArrayList<>();
        itemsList.forEach(item -> {
            List<List<Package.Item>> generatedCombinations = new ArrayList<>();
            allPossibleCombinations.forEach(list -> {
                List<Package.Item> newCombination = new ArrayList<>(list);
                newCombination.add(item);
                if (!isMaxWeightExceeded(newCombination, maxWeight)) {
                    generatedCombinations.add(newCombination);
                }
            });
            allPossibleCombinations.addAll(generatedCombinations);
            allPossibleCombinations.add(Collections.singletonList(item));
        });
        return allPossibleCombinations;
    }

    private static boolean isMaxWeightExceeded(List<Package.Item> itemsCombination, Double maxWeight) {
        Double weightOfCombination = itemsCombination.stream().mapToDouble(Package.Item::getWeight).sum();
        return weightOfCombination.compareTo(maxWeight) > 0;
    }
}
