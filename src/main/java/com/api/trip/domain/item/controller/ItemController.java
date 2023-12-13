package com.api.trip.domain.item.controller;
import com.api.trip.domain.item.controller.dto.ItemResponse;
import com.api.trip.domain.item.controller.dto.ItemsResponse;
import com.api.trip.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/{ItemId}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long ItemId) {
        return ResponseEntity.ok(itemService.getItem(ItemId));
    }

    @GetMapping
    public ResponseEntity<ItemsResponse> getItems(
            @PageableDefault(size = 8) Pageable pageable,
            @RequestParam(value = "filter", required = false) String filter
    ) {
        return ResponseEntity.ok(itemService.getItems(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<ItemsResponse> getMyItems() {
        return ResponseEntity.ok(itemService.getMyItems());
    }

    @DeleteMapping("/{ItemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long ItemId) {
        itemService.deleteItem(ItemId);
        return ResponseEntity.ok().build();
    }

}
