package com.api.trip.domain.item.controller.dto;

import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.member.model.Member;
import jakarta.persistence.Column;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateItemRequest {


    private Long productId;

    private String title;

    private String shopName;

    private String buyUrl;

    private long maxPrice;

    private long minPrice;

    private List<String> tagNames;

    public Item toEntity(Member writer){
        return Item.builder()
                .productId(productId)
                .title(title)
                .shopName(shopName)
                .buyUrl(buyUrl)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .writer(writer)
                .build();

    }
}
