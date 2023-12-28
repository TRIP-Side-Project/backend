package com.api.trip.domain.notification.service;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;
import com.api.trip.domain.interesttag.service.InterestTagService;
import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.itemtag.model.ItemTag;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
import com.api.trip.domain.notification.controller.dto.GetMyNotificationsResponse;
import com.api.trip.domain.notification.domain.Notification;
import com.api.trip.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final InterestTagService interestTagService;

    public void createNotification(Item item, List<String> tagNames){

        List<Member> receivers = interestTagService.getMemberByTags(tagNames);

        receivers.stream().forEach(member -> {
            notificationRepository.save(Notification.builder()
                    .item(item)
                    .member(member).build());
        });


    }

    @Transactional(readOnly = true)
    public GetMyNotificationsResponse getMyNotifications(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        List<Notification> notifications = notificationRepository.findNotifications(member);

        List<ItemTag> itemTags = notificationRepository.findItemTags(
                notifications.stream()
                        .map(Notification::getItem)
                        .toList()
        );

        return GetMyNotificationsResponse.of(notifications, itemTags);
    }

    public void readNotification(Long notificationId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (notification.getMember() == member) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        notification.read();
    }

    public void deleteNotification(Long notificationId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (notification.getMember() == member) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        notificationRepository.delete(notification);
    }

    public void deleteMyNotifications(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        List<Notification> notifications = notificationRepository.findAllByMember(member);

        notificationRepository.deleteAllInBatch(notifications);
    }
}
