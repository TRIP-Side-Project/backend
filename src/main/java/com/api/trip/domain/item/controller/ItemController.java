package com.api.trip.domain.item.controller;
import com.api.trip.domain.item.controller.dto.CreateItemRequest;
import com.api.trip.domain.item.controller.dto.GetItemResponse;
import com.api.trip.domain.item.controller.dto.GetItemsResponse;
import com.api.trip.domain.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @PostMapping
    public ResponseEntity<Long> createItem(@RequestBody @Valid CreateItemRequest itemRequest) {

        return ResponseEntity.ok(itemService.createItemByDirect(itemRequest));
    }

    @GetMapping("/{ItemId}")
    public ResponseEntity<GetItemResponse> getItem(@PathVariable Long ItemId) {
        return ResponseEntity.ok(itemService.getItemDetail(ItemId));
    }

    @GetMapping
    public ResponseEntity<GetItemsResponse> getItems(
            @PageableDefault(size = 8) Pageable pageable,
            @RequestParam(value = "sortCode", defaultValue = "0") int sortCode,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tag", required = false) String tagName
    ) {
        GetItemsResponse itemsDetail;

        if(tagName == null) {
            itemsDetail = itemService.getItemsDetail(pageable, sortCode, title);
        }
        else
            itemsDetail = itemService.getItemsDetailByTag(pageable, sortCode, tagName);

        return ResponseEntity.ok(itemsDetail);
    }


    @DeleteMapping("/{ItemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long ItemId) {
        itemService.deleteItem(ItemId);
        return ResponseEntity.ok().build();
    }

}
