package com.api.trip.domain.interestitem.controller.dto;

import com.api.trip.domain.item.controller.dto.GetItemResponse;
import com.api.trip.domain.item.model.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetInterestItemResponse {

    private Long id;

    private String title;

    private String shopName;

    private Integer minPrice;

    private String imageUrl;

    public static GetInterestItemResponse of(Item item){
        return GetInterestItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .shopName(item.getShopName())
                .minPrice(item.getMinPrice())
                .imageUrl(item.getImageUrl())
                .build();

    }
}
