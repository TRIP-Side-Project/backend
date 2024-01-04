package com.api.trip.domain.member.repository;

import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.model.SocialCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    
    Optional<Member> findByEmailAndSocialCode(String email, SocialCode socialCode);

}
