package com.api.trip.domain.itemtag.service;

import com.api.trip.domain.item.controller.dto.GetItemsResponse;
import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.itemtag.model.ItemTag;
import com.api.trip.domain.itemtag.repository.ItemTagRepository;
import com.api.trip.domain.tag.model.Tag;
import com.api.trip.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemTagService {

    private final ItemTagRepository itemTagRepository;
    private final TagService tagService;

    public void createItemTag(Item item, List<String> tagNames){
        List<Tag> tags = tagService.getTags(tagNames);
        
        for(Tag tag : tags){
            itemTagRepository.save(
                    ItemTag.builder()
                            .item(item)
                            .tag(tag)
                            .build()
            );

        }
    }

    public Page<Item> getItemsByTag(Pageable pageable, int sortCode, List<String> tagNames) {
        List<Tag> tags = tagService.getTags(tagNames);
        return itemTagRepository.findItemsByTags(pageable, sortCode, tags);
    }
}
