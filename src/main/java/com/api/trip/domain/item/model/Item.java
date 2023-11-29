package com.api.trip.domain.item.model;

import com.api.trip.domain.Interestitem.model.InterestItem;
import com.api.trip.domain.itemtag.model.ItemTag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "item")
    private List<ItemTag> tags = new ArrayList<>();
}
