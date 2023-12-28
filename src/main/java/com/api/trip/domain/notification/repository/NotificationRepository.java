package com.api.trip.domain.notification.repository;

import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {

    List<Notification> findAllByMember(Member member);

    Optional<Notification> findByMemberIdAndItemId(Long memberId, Long itemId);
}
