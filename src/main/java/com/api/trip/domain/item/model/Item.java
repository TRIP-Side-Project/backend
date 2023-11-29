package com.api.trip.domain.item.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer productId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String itemUrl;

    @Column(nullable = false)
    private Integer minPrice;

    @Column(nullable = false)
    private Integer maxPrice;

    @Column(nullable = false)
    private Integer viewCount;
}
