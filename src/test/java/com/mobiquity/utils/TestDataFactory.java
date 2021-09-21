package com.mobiquity.utils;

import com.mobiquity.packer.model.Package;

import java.util.Arrays;

public class TestDataFactory {
    private static Double MAX_WEIGHT = Double.valueOf("15");
    private static Double ITEM_WEIGHT1 = Double.valueOf("7");
    private static Double ITEM_WEIGHT2 = Double.valueOf("7");
    private static Double ITEM_WEIGHT3 = Double.valueOf("8");
    private static Double ITEM_WEIGHT4 = Double.valueOf("5");
    private static Integer ITEM_COST1 = 11;
    private static Integer ITEM_COST2 = 12;
    private static Integer ITEM_COST3 = 17;
    private static Integer ITEM_COST4 = 12;

    public static Package.Item anItem1() {
        return Package.Item.builder()
                .index(1)
                .weight(ITEM_WEIGHT1)
                .cost(ITEM_COST1)
                .build();
    }
    public static Package.Item anItem2() {
        return Package.Item.builder()
                .index(2)
                .weight(ITEM_WEIGHT2)
                .cost(ITEM_COST2)
                .build();
    }

    public static Package.Item anItem3() {
        return Package.Item.builder()
                .index(3)
                .weight(ITEM_WEIGHT3)
                .cost(ITEM_COST3)
                .build();
    }

    public static Package.Item anItem4() {
        return Package.Item.builder()
                .index(4)
                .weight(ITEM_WEIGHT4)
                .cost(ITEM_COST4)
                .build();
    }

    public static Package.PackageBuilder aPackageBuilder() {
     return Package.builder()
             .maxWeight(MAX_WEIGHT)
             .packageItems(Arrays.asList(anItem1(), anItem2(), anItem3()));
    }
}
