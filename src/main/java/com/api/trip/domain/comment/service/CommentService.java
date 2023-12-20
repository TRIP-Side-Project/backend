package com.api.trip.domain.comment.service;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;
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

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public Long createComment(CreateCommentRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

            if (parent.getParent() != null || parent.getArticle() != article) {
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }
        }

        return commentRepository.save(
                        Comment.builder()
                                .writer(member)
                                .article(article)
                                .content(request.getContent())
                                .parent(parent)
                                .build()
                )
                .getId();
    }

    public void updateComment(Long commentId, UpdateCommentRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (comment.getWriter() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        comment.modify(request.getContent());
    }

    public void deleteComment(Long commentId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (comment.getWriter() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public GetCommentsResponse getComments(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        List<Comment> comments = commentRepository.findComments(article);

        return GetCommentsResponse.of(comments);
    }

    @Transactional(readOnly = true)
    public GetMyCommentsResponse getMyComments(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        List<Comment> comments = commentRepository.findAllByWriterOrderByIdDesc(member);

        return GetMyCommentsResponse.of(comments);
    }
}
