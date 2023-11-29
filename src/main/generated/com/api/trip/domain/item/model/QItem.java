package com.api.trip.domain.item.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -1932957423L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemUrl = createString("itemUrl");

    public final NumberPath<Integer> maxPrice = createNumber("maxPrice", Integer.class);

    public final NumberPath<Integer> minPrice = createNumber("minPrice", Integer.class);

    public final NumberPath<Integer> productId = createNumber("productId", Integer.class);

    public final StringPath shopName = createString("shopName");

    public final ListPath<com.api.trip.domain.itemtag.model.ItemTag, com.api.trip.domain.itemtag.model.QItemTag> tags = this.<com.api.trip.domain.itemtag.model.ItemTag, com.api.trip.domain.itemtag.model.QItemTag>createList("tags", com.api.trip.domain.itemtag.model.ItemTag.class, com.api.trip.domain.itemtag.model.QItemTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public final com.api.trip.domain.member.model.QMember writer;

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writer = inits.isInitialized("writer") ? new com.api.trip.domain.member.model.QMember(forProperty("writer")) : null;
    }

}

