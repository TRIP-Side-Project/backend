package com.api.trip.domain.interesttag.respository;

import com.api.trip.domain.interesttag.model.InterestTag;
import com.api.trip.domain.member.model.Member;

import java.util.List;

public interface InterestTagRepositoryCustom {

    List<String> findInterestTags(Member member);
    List<InterestTag> findInterestTagsByTagNames(List<String> tagNames);
}
