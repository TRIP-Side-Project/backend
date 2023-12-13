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

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String buyUrl;

    @Column(nullable = false)
    private long maxPrice;

    @Column(nullable = false)
    private long minPrice;

    @Column(nullable = false)
    private long viewCount;

    @Column
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;




    public void increaseViewCount() {
        this.viewCount++;
    }
}