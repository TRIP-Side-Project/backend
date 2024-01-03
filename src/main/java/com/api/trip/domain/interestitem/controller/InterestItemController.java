package com.api.trip.domain.interestitem.controller;

import com.api.trip.domain.interestitem.controller.dto.CreateInterestItemRequest;
import com.api.trip.domain.interestitem.controller.dto.GetInterestItemsResponse;
import com.api.trip.domain.interestitem.service.InterestItemService;
import com.api.trip.domain.item.controller.dto.CreateItemRequest;
import com.api.trip.domain.item.controller.dto.GetItemResponse;
import com.api.trip.domain.item.controller.dto.GetItemsResponse;
import com.api.trip.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interest-items")
@RequiredArgsConstructor
public class InterestItemController {

    private final InterestItemService interestItemService;


    @PostMapping
    public ResponseEntity<Void> createInterestItem(@RequestBody CreateInterestItemRequest itemRequest) {

        interestItemService.createInterestItem(itemRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<GetInterestItemsResponse> getInterestItems(
            @PageableDefault(size = 6) Pageable pageable
    ) {
        return ResponseEntity.ok(interestItemService.getInterestItems(pageable));
    }


    @DeleteMapping("/{ItemId}")
    public ResponseEntity<Void> cancelInterestItem(@PathVariable Long ItemId) {
        interestItemService.cancelInterestItem(ItemId);
        return ResponseEntity.ok().build();
    }
}
