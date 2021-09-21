package com.mobiquity.packer.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Package {

    @NonNull
    private Double maxWeight;
    @NonNull
    private List<Item> packageItems;

    @Getter
    @Builder
    public static class Item {
        @NonNull
        private Integer index;
        @NonNull
        private Double weight;
        @NonNull
        private Integer cost;
    }
}
