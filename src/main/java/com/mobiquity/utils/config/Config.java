package com.mobiquity.utils.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Getter
@NoArgsConstructor
public class Config {

    private static Double maxPackageWeight;
    private static Integer maxItemsNumber;
    private static Double maxItemWeight;
    private static Integer maxItemCost;

    public static void init(final String configFile) {
        final Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream(configFile)));
        } catch (IOException e) {
            log.debug("Error occurred while reading config file: " + e.getMessage());
        }

        maxPackageWeight = Double.parseDouble(properties.getProperty("max.package.weight"));
        maxItemsNumber = Integer.parseInt(properties.getProperty("max.items.number"));
        maxItemWeight = Double.parseDouble(properties.getProperty("max.item.weight"));
        maxItemCost = Integer.parseInt(properties.getProperty("max.item.cost"));
    }

    public static Double getMaxPackageWeight() {
        return maxPackageWeight;
    }

    public static Integer getMaxItemsNumber() {
        return maxItemsNumber;
    }

    public static Double getMaxItemWeight() {
        return maxItemWeight;
    }

    public static Integer getMaxItemCost() {
        return maxItemCost;
    }
}
