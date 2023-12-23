package com.api.trip.common.naverapi;


import com.api.trip.common.naverapi.dto.ShoppingItem;
import com.api.trip.domain.item.controller.dto.CreateItemRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NaverApiService {

    private final List<String> tagList = Arrays.asList("부산", "제주", "서울", "크루즈", "일출", "해수욕장");

    public List<ShoppingItem> doFilterCategory(List<ShoppingItem> items){
        List<ShoppingItem> filteredItems = new ArrayList<>();
        for (ShoppingItem item: items)
        {
            if(
                item.getCategory1().equals("국내패키지/기타") ||
                item.getCategory2().equals("국내패키지/기타") ||
                item.getCategory3().equals("국내패키지/기타") ||
                item.getCategory4().equals("국내패키지/기타")
            )
                filteredItems.add(item);
        }
        return filteredItems;
    }

    public List<CreateItemRequest> toCreateItemRequest(List<ShoppingItem> items){
        List<CreateItemRequest> createItemRequests = new ArrayList<>();


        for(ShoppingItem item : items){

            List<String> tagsNames = extractTagNames(item.getTitle());
            createItemRequests.add(CreateItemRequest.of(item, tagsNames));

        }
        return createItemRequests;
    }

    public List<String> extractTagNames(String title){
        return tagList.stream().filter(tag -> title.contains(tag)).toList();
    }
}
