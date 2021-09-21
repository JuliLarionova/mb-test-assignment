package com.mobiquity.utils;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.model.Package;
import com.mobiquity.packer.model.Package.Item;
import com.mobiquity.utils.config.Config;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class LinesParser {
    private static Config properties = new Config();
    private static final String COLON_DELIMITER = ":";

    /**
     * Parses each line of file with package information
     *
     * @param linesToParse lines of file
     * @return list of packages from lines
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

        Integer lineLength = lineToParse.length();
        String packageItems = lineToParse.substring(indexOfColon + 1, lineLength);
        Double maxWeight = Double
                .parseDouble(lineToParse.substring(0, indexOfColon).trim());
        Package parsedLine = Package.builder()
                .maxWeight(maxWeight)
                .packageItems(parsePackageItems(packageItems))
                .build();

        return parsedLine;
    }

    private static List<Item> parsePackageItems(String packageItems) {
        final Pattern p = Pattern.compile("(\\d+)\\,(\\d+\\.?\\d*)\\,(\\D\\d+)");
        final Matcher m = p.matcher(packageItems);

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

    private static void validatePackageInfo(Package packageInfo) throws APIException {
        if (packageInfo.getMaxWeight().compareTo(Config.getMaxPackageWeight())>0) {
            throw new APIException("Package weight is greater than max allowed weight");
        }

        Predicate<Item> condition = c -> c.getIndex() > Config.getMaxItemsNumber()
                || c.getWeight().compareTo(Config.getMaxItemWeight()) > 0 || c.getCost() > Config.getMaxItemCost();
        checkPackageItemByCondition(packageInfo.getPackageItems(), condition, "Package item does not meet the requirement");
    }

    private static void checkPackageItemByCondition(List<Item> packageInfo, Predicate<Item> condition, String errorMsg) throws APIException {
        Optional<Item> unsatisfiedItemExists = packageInfo.stream()
                .filter(condition)
                .findAny();
        if (unsatisfiedItemExists.isPresent()) {
            throw new APIException(errorMsg);
        }
    }
}

