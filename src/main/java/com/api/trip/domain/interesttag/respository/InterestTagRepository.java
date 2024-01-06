package com.api.trip.domain.interesttag.respository;

import com.api.trip.domain.interesttag.model.InterestTag;
import com.api.trip.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestTagRepository extends JpaRepository<InterestTag, Long>, InterestTagRepositoryCustom {

    boolean existsByMemberAndTag_Name(Member member, String tagName);

    void deleteByTag_Name(String tagName);

    void deleteAllByMember(Member member);
}
