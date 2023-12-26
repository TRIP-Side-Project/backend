package com.api.trip.common.naverapi.dto;

import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor  //기본 생성자 만들기
public class ShoppingRequest {
    private String query;   //검색어
    private String sort;
    private Integer display;   //검색결과 갯수는 1개만
    private Integer start;

    @Builder
    public ShoppingRequest(Integer display, Integer start) {
        this.query = "국내패키지";
        this.sort = "date";
        this.display = display;
        this.start = start;
    }

    public MultiValueMap map() {    //파라미터를 넘기기 위한 맵
        LinkedMultiValueMap<String, String > map = new LinkedMultiValueMap();
        map.add("query", query);
        map.add("display", String.valueOf(display));
        map.add("sort", sort);
        map.add("start", String.valueOf(start));
        return map;
    }


}