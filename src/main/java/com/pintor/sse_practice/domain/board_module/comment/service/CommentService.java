package com.pintor.sse_practice.domain.board_module.comment.service;

import com.pintor.sse_practice.domain.board_module.board.service.BoardService;
import com.pintor.sse_practice.domain.board_module.comment.entity.Comment;
import com.pintor.sse_practice.domain.board_module.comment.repository.CommentRepository;
import com.pintor.sse_practice.domain.board_module.comment.request.CommentRequest;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.global.errors.exception.ApiResponseException;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import com.pintor.sse_practice.global.util.AppConfig;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardService boardService;

    private final EntityManager entityManager;

    public long count() {
        return this.commentRepository.count();
    }

    private Comment refresh(Comment comment) {
        entityManager.flush();
        comment = this.getCommentById(comment.getId());
        entityManager.refresh(comment);
        return comment;
    }

    @Transactional
    public Comment create(CommentRequest.Create request, Errors errors, Member author) {

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(author)
                .board(this.boardService.getBoardById(request.getBoardId()))
                .tag(request.getTagId() == null ? null : this.getCommentById(request.getTagId()))
                .build();

        this.commentRepository.save(comment);

        return this.refresh(comment);
    }

    public Comment getCommentById(Long id) {

        return this.commentRepository.findById(id)
                .orElseThrow(() -> {

                    Errors errors = AppConfig.getMockErrors("comment");
                    errors.reject("not found", new Object[]{id}, "comment that has id is not found");

                    return new ApiResponseException(
                            ResData.of(
                                    ResCode.F_03_02_01,
                                    errors
                            )
                    );
                });
    }
}
