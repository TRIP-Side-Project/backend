package com.api.trip.domain.item.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    @Modifying
    @Query("UPDATE Item i SET i.viewCount = i.viewCount + 1 WHERE i = :item")
    void increaseViewCount(@Param("item") Item item);

    @Modifying
    @Query("UPDATE Item i SET i.likeCount = i.likeCount + 1 WHERE i = :item")
    void increaseLikeCount(@Param("item") Item item);

    @Modifying
    @Query("UPDATE Item i SET i.likeCount = i.likeCount - 1 WHERE i = :item")
    void decreaseLikeCount(@Param("item") Item item);
}
