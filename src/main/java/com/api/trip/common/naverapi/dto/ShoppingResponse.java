package com.api.trip.common.naverapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingResponse {
    private Date lastBuildDate;     //검색결과를 생성한 시간
    private String total;   //총 검색 결과
    private List<ShoppingItem> items;   //아이템을 받을 리스트


}