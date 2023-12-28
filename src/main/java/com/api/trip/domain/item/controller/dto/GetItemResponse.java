package com.api.trip.domain.item.controller.dto;

import com.api.trip.domain.item.model.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetItemResponse {

    private Long id;

    private Long productId;

    private String title;

    private String shopName;

    private String buyUrl;

    private Integer maxPrice;

    private Integer minPrice;

    private String imageUrl;

    public static GetItemResponse of(Item item){
        return GetItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .title(item.getTitle())
                .shopName(item.getShopName())
                .buyUrl(item.getBuyUrl())
                .maxPrice(item.getMaxPrice())
                .minPrice(item.getMinPrice())
                .imageUrl(item.getImageUrl())
                .build();

    }
}
