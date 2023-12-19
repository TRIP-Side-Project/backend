package com.api.trip.domain.item.controller.dto;

import com.api.trip.domain.article.controller.dto.GetArticlesResponse;
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

    private Pagination pagination;
    private List<GetItemResponse> itemList;


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

        private static Pagination of(Page<Item> page) {
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

    public static GetItemsResponse of(Page<Item> page)
    {

        return GetItemsResponse.builder()
                .pagination(Pagination.of(page))
                .itemList(page.getContent().stream().map(GetItemResponse::of).collect(Collectors.toList()))
                .build();
    }
}
