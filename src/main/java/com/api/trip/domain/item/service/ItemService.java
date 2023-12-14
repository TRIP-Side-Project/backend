package com.api.trip.domain.item.service;


import com.api.trip.domain.item.controller.dto.CreateItemRequest;
import com.api.trip.domain.item.controller.dto.GetItemResponse;
import com.api.trip.domain.item.controller.dto.GetItemsResponse;
import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.item.repository.ItemRepository;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final MemberService memberService;
    private final ItemRepository itemRepository;

    public Long createItem(CreateItemRequest itemRequest) {
        Member member = memberService.getAuthenticationMember();
        Item item = itemRequest.toEntity(member);

        return itemRepository.save(item).getId();
    }

    @Transactional(readOnly = true)
    public Item getItem(Long itemId){
        return itemRepository.findById(itemId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public GetItemResponse getItemDetail(Long ItemId) {
        Item item = itemRepository.findById(ItemId).orElseThrow();

        item.increaseViewCount();
        return GetItemResponse.of(item);
    }

    @Transactional(readOnly = true)
    public GetItemsResponse getItemsDetail(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findItems(pageable);
        itemPage.getContent();

        return GetItemsResponse.of(itemPage);
    }


    public void deleteItem(Long ItemId) {
        Member member = memberService.getAuthenticationMember();

        Item item = itemRepository.findById(ItemId).orElseThrow();
        if (item.getWriter() != member) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        item.delete();
    }

}
