package com.api.trip.domain.item.controller.dto;

import com.api.trip.domain.item.model.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetItemsResponse {

    private long totalCount;
    private int currentPage;
    private int totalPage;
    private List<GetItemResponse> itemList;

    public static GetItemsResponse of(Page<Item> page)
    {
        return GetItemsResponse.builder()
                .currentPage(page.getNumber())
                .totalCount(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .itemList(page.getContent().stream().map(GetItemResponse::of).collect(Collectors.toList()))
                .build();
    }
}
