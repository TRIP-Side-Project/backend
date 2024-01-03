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

    private Pagination pagination;
    private List<GetInterestItemResponse> itemList;

    @Getter
    @Builder
    private static class Pagination {

        private int totalPages;
        private long totalElements;
        private int page;
        private boolean hasNext;
        private boolean hasPrevious;
        private int requestSize;
        private int articleSize;

        private static Pagination of(Page<InterestItem> page) {
            return builder()
                    .totalPages(page.getTotalPages())
                    .totalElements(page.getTotalElements())
                    .page(page.getNumber() + 1)
                    .hasNext(page.hasNext())
                    .hasPrevious(page.hasPrevious())
                    .requestSize(page.getSize())
                    .articleSize(page.getNumberOfElements())
                    .build();
        }
    }


    public static GetInterestItemsResponse of(Page<InterestItem> page)
    {
        return GetInterestItemsResponse.builder()
                .pagination(Pagination.of(page))
                .itemList(page.getContent().stream().map(InterestItem::getItem).map(GetInterestItemResponse::of).collect(Collectors.toList()))
                .build();
    }

}
