package com.api.trip.domain.item.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> findItems(Pageable pageable);
}
