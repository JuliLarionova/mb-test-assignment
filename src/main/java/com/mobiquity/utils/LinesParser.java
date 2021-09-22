package com.mobiquity.utils;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.model.Package;
import com.mobiquity.packer.model.Package.Item;
import com.mobiquity.utils.config.Config;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@NoArgsConstructor
public class LinesParser {
    private static final String COLON_DELIMITER = ":";
    private static final String COMMA_DELIMITER = ",";

    /**
     * Parses each line of file with package information
     *
     * @param linesToParse lines of file
     * @return List of packages from lines
     * @throws APIException
     */
    public static List<Package> parseLines(List<String> linesToParse) throws APIException {
        List<Package> packageList = new ArrayList<>();
        for (String c : linesToParse) {
            Package packageInfo = parsePackage(c);
            validatePackageInfo(packageInfo);
            packageList.add(packageInfo);
        }

        return packageList;
    }

    private static Package parsePackage(String lineToParse) throws APIException {
        String[] splitResult = lineToParse.split(COLON_DELIMITER);
        if (splitResult.length != 2) {
            throw new APIException("There is more than one colon delimiter: " + lineToParse);
        }
        int indexOfColon = lineToParse.indexOf(COLON_DELIMITER);
        if (indexOfColon == -1) {
            throw new APIException("There is no max weight in a line: " + lineToParse);
        }

        int lineLength = lineToParse.length();
        String packageItems = lineToParse.substring(indexOfColon + 1, lineLength);
        Double maxWeight = Double
                .parseDouble(lineToParse.substring(0, indexOfColon).trim());

        return Package.builder()
                .maxWeight(maxWeight)
                .packageItems(parsePackageItemsWithRegex(packageItems))
                .build();
    }

    /**
     * The first option how to parse string with data in parentheses using regex
     *
     * @param packageItemsString tring with unparsed items in line
     * @return list of items in line
     */
    private static List<Item> parsePackageItemsWithRegex(String packageItemsString) {
        final Pattern p = Pattern.compile("(\\d+)\\,(\\d+\\.?\\d*)\\,(\\D\\d+)");
        final Matcher m = p.matcher(packageItemsString);

        List<Item> items = new LinkedList<>();
        while (m.find()) {
            Item item = Item.builder()
                    .index(Integer.valueOf(m.group(1)))
                    .weight(Double.valueOf(m.group(2)))
                    .cost(Integer.valueOf(m.group(3).substring(1)))
                    .build();
            items.add(item);
        }

        return items;
    }

    /**
     * The second option how to parse string with data in parentheses
     *
     * @param packageItemsString string with unparsed items in line
     * @return list of items in line
     */
    private static List<Item> parsePackageItemsWithoutRegex(String packageItemsString) {
        return Arrays.stream(packageItemsString.split("[()]"))
                .filter(c -> !c.equals(" "))
                .map(c -> {
                    String[] splittedItem = c.split(COMMA_DELIMITER);
                    return Item.builder()
                            .index(Integer.valueOf(splittedItem[0]))
                            .weight(Double.valueOf(splittedItem[1]))
                            .cost(Integer.valueOf(splittedItem[2].substring(1)))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private static void validatePackageInfo(Package packageInfo) throws APIException {
        if (packageInfo.getMaxWeight().compareTo(Config.getMaxPackageWeight()) > 0) {
            throw new APIException("Package weight is greater than max allowed weight");
        }

        Predicate<Item> condition = c -> c.getIndex() > Config.getMaxItemsNumber()
                || c.getWeight().compareTo(Config.getMaxItemWeight()) > 0 || c.getCost() > Config.getMaxItemCost();
        Optional<Item> unsatisfiedItemExists = packageInfo.getPackageItems().stream()
                .filter(condition)
                .findAny();
        if (unsatisfiedItemExists.isPresent()) {
            throw new APIException("Package item does not meet the requirement");
        }
    }
}

