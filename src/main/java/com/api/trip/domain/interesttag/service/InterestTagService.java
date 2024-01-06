package com.api.trip.domain.interesttag.service;

import com.api.trip.common.exception.ErrorCode;
import com.api.trip.common.exception.custom_exception.BadRequestException;
import com.api.trip.domain.interesttag.model.InterestTag;
import com.api.trip.domain.interesttag.respository.InterestTagRepository;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.tag.model.Tag;
import com.api.trip.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class InterestTagService {

    private final InterestTagRepository interestTagRepository;
    private final TagRepository tagRepository;

    // 관심 태그 저장
    public void createTag(Member member, List<String> tagNames) {

        if (tagNames.isEmpty()) {
            interestTagRepository.deleteAllByMember(member);
        }

        // 태그 테이블에서 태그 검색
        List<String> tags = tagRepository.findByNameIn(tagNames)
                .stream()
                .map(Tag::getName)
                .toList();

        List<String> currentTags = getInterestTag(member);

        for (String currentTag : currentTags) {
            if (!tags.contains(currentTag)) {
                interestTagRepository.deleteByTag_Name(currentTag);
            }
        }

        for (String tagName : tags) {
            if (!isExistTag(member, tagName)) {
                Tag tag = tagRepository.findByName(tagName);
                InterestTag interestTag = InterestTag.builder()
                        .member(member)
                        .tag(tag)
                        .build();

                interestTagRepository.save(interestTag);
            }
        }
    }

    @Transactional(readOnly = true)
    public boolean isExistTag(Member member, String tagName) {
        return interestTagRepository.existsByMemberAndTag_Name(member, tagName);
    }

    @Transactional(readOnly = true)
    public List<String> getInterestTag(Member member) {
        return interestTagRepository.findInterestTags(member);
    }

    @Transactional(readOnly = true)
    public Set<Member> getMembersByTagNames(List<String> tagNames) {
        List<InterestTag> interestTags = interestTagRepository.findInterestTagsByTagNames(tagNames);
        return interestTags.stream().map(InterestTag::getMember).collect(Collectors.toSet());
    }
}
