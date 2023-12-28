package com.api.trip.domain.interesttag.service;

import com.api.trip.domain.interesttag.model.InterestTag;
import com.api.trip.domain.interesttag.respository.InterestTagRepository;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.tag.model.Tag;
import com.api.trip.domain.tag.repository.TagRepository;
import com.api.trip.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class InterestTagService {

    private final InterestTagRepository interestTagRepository;
    private final TagRepository tagRepository;

    // 관심 태그 저장
    public void createTag(Member member, List<String> tagNames) {

        // 태그 테이블에서 태그 검색
        List<Tag> tags = tagRepository.findByNameIn(tagNames);

        // 관심 태그 테이블에 저장
        for (Tag tag : tags) {
            InterestTag interestTag = InterestTag.builder()
                    .member(member)
                    .tag(tag)
                    .build();

            interestTagRepository.save(interestTag);
        }
    }

    @Transactional(readOnly = true)
    public List<String> getInterestTag(Member member) {
        return interestTagRepository.findInterestTags(member);
    }

    @Transactional(readOnly = true)
    public List<Member> getMemberByTags(List<String> tagNames){
        List<InterestTag> interestTags = interestTagRepository.findInterestTagsByTagNames(tagNames);
        return interestTags.stream().map(InterestTag::getMember).toList();
    }
}
