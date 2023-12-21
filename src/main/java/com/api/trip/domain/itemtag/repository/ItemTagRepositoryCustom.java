package com.api.trip.domain.itemtag.repository;

import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.tag.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemTagRepositoryCustom {
    Page<Item> findItemsByTag(Pageable pageable, int sortCode, Tag tag);
}
