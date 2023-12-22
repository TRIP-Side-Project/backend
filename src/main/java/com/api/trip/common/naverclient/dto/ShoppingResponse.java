package com.api.trip.common.naverclient.dto;

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShoppingItem {
        private String title;   //상품이름
        private String link;    //상품 url
        private String image;   //상품 이미지 url
        private Integer lprice; //최저가
        private Integer hprice; //최고가
        private String mallName;    //상품을 판매하는 쇼핑몰
        private String maker;   //제조사
        private String brand;   //브랜드
    }
}