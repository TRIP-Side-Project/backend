package com.api.trip.domain.item.service;


import com.api.trip.common.exception.ErrorCode;
import com.api.trip.common.exception.custom_exception.ForbiddenException;
import com.api.trip.common.exception.custom_exception.NotFoundException;
import com.api.trip.domain.item.controller.dto.CreateItemRequest;
import com.api.trip.domain.item.controller.dto.GetItemResponse;
import com.api.trip.domain.item.controller.dto.GetItemsResponse;
import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.item.repository.ItemRepository;
import com.api.trip.domain.itemtag.service.ItemTagService;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.model.MemberRole;
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
    private final ItemTagService itemTagService;

    public Long createItemByDirect(CreateItemRequest itemRequest) {
        Member member = memberService.getAuthenticationMember();

        if(!member.getRole().equals(MemberRole.ADMIN))
            throw new ForbiddenException(ErrorCode.FORBIDDEN_CREATE);

        Item item = itemRequest.toEntity();
        item.setWriter(member);
        itemTagService.createItemTag(item, itemRequest.getTagNames());

        return itemRepository.save(item).getId();
    }
    public Item createItem(CreateItemRequest itemRequest){
        Item item = itemRequest.toEntity();
        itemTagService.createItemTag(item, itemRequest.getTagNames());
        return itemRepository.save(item);

    }


    @Transactional(readOnly = true)
    public Item getItem(Long itemId){
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ITEM));
    }

    @Transactional(readOnly = true)
    public GetItemResponse getItemDetail(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ITEM));

        itemRepository.increaseViewCount(item);

        return GetItemResponse.of(item);
    }

    @Transactional(readOnly = true)
    public GetItemsResponse getItemsDetail(Pageable pageable, int sortCode, String search) {
        Page<Item> itemPage = itemRepository.findItems(pageable, sortCode, search);

        return GetItemsResponse.of(itemPage);
    }

    @Transactional(readOnly = true)
    public GetItemsResponse getItemsDetailByTag(Pageable pageable, int sortCode, String tagName) {
        Page<Item> itemsByTag = itemTagService.getItemsByTag(pageable, sortCode, tagName);

        return GetItemsResponse.of(itemsByTag);
    }

    public void deleteItem(Long itemId) {
        Member member = memberService.getAuthenticationMember();

        if (!member.getRole().equals("ADMIN")) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_DELETE);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ITEM));


        item.delete();
    }

}
