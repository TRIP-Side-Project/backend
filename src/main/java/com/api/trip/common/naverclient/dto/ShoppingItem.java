package com.api.trip.common.naverclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingItem {
    private String title;   //상품이름
    private String link;    //상품 url
    private String image;   //상품 이미지 url
    private Integer lprice; //최저가
    private Integer hprice; //최고가
    private String mallName;    //상품을 판매하는 쇼핑몰
    private String maker;   //제조사
    private String brand;   //브랜드
}