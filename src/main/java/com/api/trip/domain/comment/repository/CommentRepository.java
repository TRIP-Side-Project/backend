package com.api.trip.domain.comment.repository;

import com.api.trip.domain.comment.model.Comment;
import com.api.trip.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    List<Comment> findAllByWriterOrderByIdDesc(Member writer);

    Long countByWriter_Id(Long memberId);
}
