package com.api.trip.common.naverapi;


import com.api.trip.common.naverapi.dto.ShoppingItem;
import com.api.trip.domain.item.controller.dto.CreateItemRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NaverApiService {

    private final List<String> tagList = Arrays.asList("부산", "제주", "서울", "강원", "경남", "춘천", "목포", "여수", "안동", "경주", "대구", "대전", "전주"
            ,"눈꽃", "스키", "보드", "바다", "요트", "해양스포츠", "단풍", "골프");

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
        List<String> list = tagList.stream().filter(tag -> title.contains(tag)).toList();

        if(title.contains("양양") || title.contains("속초") || title.contains("강릉"))
            list.add("강원");
        if(title.contains("여수") || title.contains("순천"))
            list.add("여수");

        return list;
    }
}
