package com.api.trip.domain.tag.repository;

import com.api.trip.domain.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
