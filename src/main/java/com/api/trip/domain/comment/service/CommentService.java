package com.api.trip.domain.comment.service;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.article.repository.ArticleRepository;
import com.api.trip.domain.comment.controller.dto.CreateCommentRequest;
import com.api.trip.domain.comment.controller.dto.GetCommentsResponse;
import com.api.trip.domain.comment.controller.dto.GetMyCommentsResponse;
import com.api.trip.domain.comment.controller.dto.UpdateCommentRequest;
import com.api.trip.domain.comment.model.Comment;
import com.api.trip.domain.comment.repository.CommentRepository;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public Long createComment(CreateCommentRequest request, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        Article article = articleRepository.findById(request.getArticleId()).orElseThrow();

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId()).orElseThrow();
            if (parent.getParent() != null || parent.getArticle() != article) {
                throw new RuntimeException("잘못된 요청입니다.");
            }
        }

        Comment comment = request.toEntity(member, article, parent);

        return commentRepository.save(comment).getId();
    }

    public void updateComment(Long commentId, UpdateCommentRequest request, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (comment.getWriter() != member) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        comment.modify(request.getContent());
    }

    public void deleteComment(Long commentId, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (comment.getWriter() != member) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public GetCommentsResponse getComments(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();

        List<Comment> comments = commentRepository.findComments(article);

        return GetCommentsResponse.of(comments);
    }

    @Transactional(readOnly = true)
    public GetMyCommentsResponse getMyComments(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        List<Comment> comments = commentRepository.findAllByWriterOrderByIdDesc(member);

        return GetMyCommentsResponse.of(comments);
    }
}
