package com.api.trip.domain.item.controller.dto;

import com.api.trip.common.naverapi.dto.ShoppingItem;
import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.member.model.Member;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CreateItemRequest {

    @NotBlank(message = "상품 id는 필수 입력값 입니다.")
    private Long productId;

    @NotBlank(message = "상품 제목은 필수 입력값 입니다.")
    private String title;

    @NotBlank(message = "상품 구매처는 필수 입력값 입니다.")
    private String shopName;

    @NotBlank(message = "상품 구매 url은 필수 입력값 입니다.")
    private String buyUrl;

    @Min(value=0, message="상품의 최고가는 0원 이상입니다.")
    private Integer maxPrice;

    @Min(value=0, message="상품의 최저가는 0원 이상입니다.")
    private Integer minPrice;

    @NotBlank(message = "상품 id는 필수 입력값 입니다.")
    private String imageUrl;

    private List<String> tagNames;

    @Builder
    private CreateItemRequest(Long productId, String title, String shopName, String buyUrl, Integer maxPrice, Integer minPrice, String imageUrl, List<String> tagNames) {
        this.productId = productId;
        this.title = title;
        this.shopName = shopName;
        this.buyUrl = buyUrl;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.imageUrl = imageUrl;
        this.tagNames = tagNames;
    }

    public static CreateItemRequest of(ShoppingItem shoppingItem, List<String> tagNames){
        return CreateItemRequest.builder()
                .productId(shoppingItem.getProductId())
                .title(shoppingItem.getTitle())
                .shopName(shoppingItem.getMallName())
                .buyUrl(shoppingItem.getLink())
                .maxPrice(shoppingItem.getHprice())
                .minPrice(shoppingItem.getLprice())
                .imageUrl(shoppingItem.getImage())
                .tagNames(tagNames)
                .build();
    }

    public Item toEntity(){
        return Item.builder()
                .productId(productId)
                .title(title)
                .shopName(shopName)
                .buyUrl(buyUrl)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .imageUrl(imageUrl)
                .build();

    }
}
