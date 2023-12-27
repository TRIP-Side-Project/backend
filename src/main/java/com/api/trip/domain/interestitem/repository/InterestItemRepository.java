package com.api.trip.domain.interestitem.repository;

import com.api.trip.domain.interestitem.model.InterestItem;
import com.api.trip.domain.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestItemRepository extends JpaRepository<InterestItem, Long> {

    Page<InterestItem> findByMember(Member member, Pageable pageable);

    InterestItem findByItem_Id(Long itemId);

    Long countByMember_Id(Long memberId);
}
