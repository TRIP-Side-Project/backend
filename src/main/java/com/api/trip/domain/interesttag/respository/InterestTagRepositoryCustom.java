package com.api.trip.domain.interesttag.respository;

import com.api.trip.domain.member.model.Member;

import java.util.List;

public interface InterestTagRepositoryCustom {

    List<String> findInterestTags(Member member);
}
