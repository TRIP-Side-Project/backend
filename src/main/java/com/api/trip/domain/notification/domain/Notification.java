package com.api.trip.domain.notification.domain;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "item_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @Column(name = "read_or_not", nullable = false)
    private boolean read;

    @Builder
    private Notification(Member member, Item item, boolean read) {
        this.member = member;
        this.item = item;
        this.read = read;
    }

    public void read() {
        read = true;
    }
}
