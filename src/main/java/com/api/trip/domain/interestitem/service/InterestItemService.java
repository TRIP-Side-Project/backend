package com.api.trip.domain.interestitem.service;

import com.api.trip.domain.interestitem.model.InterestItem;
import com.api.trip.domain.interestitem.controller.dto.CreateInterestItemRequest;
import com.api.trip.domain.interestitem.controller.dto.GetInterestItemsResponse;
import com.api.trip.domain.interestitem.repository.InterestItemRepository;
import com.api.trip.domain.item.controller.dto.GetItemsResponse;
import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.item.repository.ItemRepository;
import com.api.trip.domain.item.service.ItemService;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestItemService {

    private final MemberService memberService;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final InterestItemRepository interestItemRepository;


    public void createInterestItem(CreateInterestItemRequest itemRequest) {
        Member member = memberService.getAuthenticationMember();
        Item item = itemService.getItem(itemRequest.getItemId());

        InterestItem interestItem = InterestItem.builder()
                .item(item)
                .member(member).build();

        interestItemRepository.save(interestItem);
        itemRepository.increaseLikeCount(item);

    }

    @Transactional(readOnly = true)
    public GetItemsResponse getInterestItems(Pageable pageable) {
        Member member = memberService.getAuthenticationMember();
        Page<InterestItem> page = interestItemRepository.findByMember(member, pageable);

        return GetInterestItemsResponse.of(page);
    }

    public void cancelInterestItem(Long itemId) {
        InterestItem interestItem = interestItemRepository.findByItem_Id(itemId);
        interestItemRepository.delete(interestItem);
        itemRepository.decreaseLikeCount(interestItem.getItem());
    }
}
