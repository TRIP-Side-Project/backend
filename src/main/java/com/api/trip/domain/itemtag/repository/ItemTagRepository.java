package com.api.trip.domain.itemtag.repository;

import com.api.trip.domain.itemtag.model.ItemTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTagRepository extends JpaRepository<ItemTag, Long> {
}
