package com.api.trip.domain.item.controller;
import com.api.trip.domain.item.controller.dto.CreateItemRequest;
import com.api.trip.domain.item.controller.dto.GetItemResponse;
import com.api.trip.domain.item.controller.dto.GetItemsResponse;
import com.api.trip.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Long> createItem(@RequestBody CreateItemRequest itemRequest) {

        return ResponseEntity.ok(itemService.createItem(itemRequest));
    }
    @GetMapping("/{ItemId}")
    public ResponseEntity<GetItemResponse> getItem(@PathVariable Long ItemId) {
        return ResponseEntity.ok(itemService.getItemDetail(ItemId));
    }

    @GetMapping
    public ResponseEntity<GetItemsResponse> getItems(
            @PageableDefault(size = 8) Pageable pageable,
            @RequestParam int sortCode,
            @RequestParam String search
    ) {

        return ResponseEntity.ok(itemService.getItemsDetail(pageable, sortCode, search));
    }


    @DeleteMapping("/{ItemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long ItemId) {
        itemService.deleteItem(ItemId);
        return ResponseEntity.ok().build();
    }

}
