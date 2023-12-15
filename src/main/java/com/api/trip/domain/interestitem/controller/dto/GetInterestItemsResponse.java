package com.api.trip.domain.interestitem.controller.dto;

import com.api.trip.domain.interestitem.model.InterestItem;
import com.api.trip.domain.item.controller.dto.GetItemResponse;
import com.api.trip.domain.item.controller.dto.GetItemsResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetInterestItemsResponse {

    private long totalCount;
    private int currentPage;
    private int totalPage;
    private List<GetItemResponse> itemList;

    public static GetItemsResponse of(Page<InterestItem> page)
    {
        return GetItemsResponse.builder()
                .currentPage(page.getNumber())
                .totalCount(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .itemList(page.getContent().stream().map(InterestItem::getItem).map(GetItemResponse::of).collect(Collectors.toList()))
                .build();
    }

}
