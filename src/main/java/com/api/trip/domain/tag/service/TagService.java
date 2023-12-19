package com.api.trip.domain.tag.service;

import com.api.trip.domain.tag.model.Tag;
import com.api.trip.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public void createTag(String tagName){
        Tag tag = Tag.builder().name(tagName).build();
        tagRepository.save(tag);
    }
    public List<Tag> getTags(List<String> tagNames){
        return tagRepository.findByNameIn(tagNames);
    }

    public Tag getTag(String tagName){
        return tagRepository.findByName(tagName);
    }
    /*
     @Todo
     필요 시 삭제 기능 구현
     */
}
