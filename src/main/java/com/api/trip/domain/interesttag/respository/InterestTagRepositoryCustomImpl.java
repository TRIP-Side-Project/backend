package com.api.trip.domain.interesttag.respository;

import com.api.trip.domain.interesttag.model.InterestTag;
import com.api.trip.domain.interesttag.model.QInterestTag;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.tag.model.QTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.api.trip.domain.interesttag.model.QInterestTag.*;

public class InterestTagRepositoryCustomImpl implements InterestTagRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public InterestTagRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<String> findInterestTags(Member member) {
        return jpaQueryFactory.select(interestTag.tag.name)
                .from(interestTag)
                .where(interestTag.member.eq(member))
                .fetch();
    }

    @Override
    public List<InterestTag> findInterestTagsByTagNames(List<String> tagNames) {
        return jpaQueryFactory.select(interestTag)
                .from(interestTag)
                .innerJoin(interestTag.tag, QTag.tag).fetchJoin()
                .where(interestTag.tag.name.in(tagNames))
                .fetch();

    }

}
