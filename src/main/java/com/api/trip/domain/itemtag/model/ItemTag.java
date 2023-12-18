package com.api.trip.domain.itemtag.model;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.tag.model.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    private ItemTag(Tag tag, Item item) {
        this.tag = tag;
        this.item = item;
    }
}
