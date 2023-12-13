package com.api.trip.domain.item.service;


import com.api.trip.common.security.util.SecurityUtils;
import com.api.trip.domain.item.controller.dto.ItemResponse;
import com.api.trip.domain.item.controller.dto.ItemsResponse;
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

    public ItemResponse getItem(Long ItemId) {
        Item item = itemRepository.findById(ItemId).orElseThrow();

        item.increaseViewCount();
        return ItemResponse.of(item);
    }

    @Transactional(readOnly = true)
    public ItemsResponse getItems(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findItems(pageable);

        return ItemsResponse.of(itemPage);
    }

    @Transactional(readOnly = true)
    public ItemsResponse getMyItems() {
        Member member = memberService.getAuthenticationMember();

        List<Item> Items = itemRepository.findAllByWriterOrderByIdDesc(member);

        return ItemsResponse.of(Items);
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
