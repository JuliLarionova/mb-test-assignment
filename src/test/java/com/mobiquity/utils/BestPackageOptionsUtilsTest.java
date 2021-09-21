package com.mobiquity.utils;

import com.mobiquity.packer.model.Package;
import org.junit.Test;

import java.util.Arrays;

import static com.mobiquity.utils.TestDataFactory.*;
import static org.junit.Assert.assertEquals;

public class BestPackageOptionsUtilsTest {

    @Test
    public void returnsCombinationWithTheHighestCost() {
        //given
        Package pack = aPackageBuilder().build();
        //when
        String stringOfItemIndexes = BestPackageOptionsUtils.suitableItemIndexes(pack);
        //then
        String expectedString = "2,3";
        assertEquals(stringOfItemIndexes, expectedString);
    }

    @Test
    public void returnsCombinationWithTheHighestCostAndTheLowestWeight() {
         /*chooses from 2 combination (2, 3) with weight 15 and cost 29
        and (3, 4) with weight 13 and cost 29*/
        //given
        Package pack = aPackageBuilder()
                .packageItems(Arrays.asList(anItem1(), anItem2(), anItem3(), anItem4()))
                .build();
        //when
        String stringOfItemIndexes = BestPackageOptionsUtils.suitableItemIndexes(pack);
        //then
        String expectedString = "3,4";
        assertEquals(stringOfItemIndexes, expectedString);
    }

    @Test
    public void returnsEmptyString_allItemWeightsAreGreaterThanMaxWeightOfPackage() {
        //given
        Package pack = aPackageBuilder()
                .maxWeight(1d)
                .packageItems(Arrays.asList(anItem1(), anItem2(), anItem3(), anItem4()))
                .build();
        //when
        String stringOfItemIndexes = BestPackageOptionsUtils.suitableItemIndexes(pack);
        //then
        assertEquals(stringOfItemIndexes, "");
    }
}