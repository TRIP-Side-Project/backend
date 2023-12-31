package com.api.trip.domain.item.model;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import com.api.trip.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item  extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String buyUrl;

    private Integer maxPrice;

    @Column(nullable = false)
    private Integer minPrice;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false)
    private Integer likeCount;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;


    @Builder
    private Item(Long productId, String title, String shopName, String buyUrl, Integer maxPrice, Integer minPrice, String imageUrl, Member writer) {
        this.productId = productId;
        this.title = title;
        this.shopName = shopName;
        this.buyUrl = buyUrl;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.imageUrl = imageUrl;
        this.viewCount = 0;
        this.likeCount = 0;
        this.writer = writer;
    }

    public void setWriter(Member writer){
        this.writer = writer;
    }

    public void delete(){
        if(this.isDeleted == true)
            new RuntimeException("이미 삭제된 아이템입니다.");
        this.isDeleted = true;
    }
}