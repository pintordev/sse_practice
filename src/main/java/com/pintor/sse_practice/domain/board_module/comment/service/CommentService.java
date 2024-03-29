package com.pintor.sse_practice.domain.board_module.comment.service;

import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import com.pintor.sse_practice.domain.board_module.board.repository.BoardRepository;
import com.pintor.sse_practice.domain.board_module.board.service.BoardService;
import com.pintor.sse_practice.domain.board_module.comment.dto.CommentGetDto;
import com.pintor.sse_practice.domain.board_module.comment.entity.Comment;
import com.pintor.sse_practice.domain.board_module.comment.repository.CommentRepository;
import com.pintor.sse_practice.domain.board_module.comment.request.CommentRequest;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.global.errors.exception.ApiResponseException;
import com.pintor.sse_practice.global.response.DataModel;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import com.pintor.sse_practice.global.util.AppConfig;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardService boardService;
    private final BoardRepository boardRepository;

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

        this.createValidate(request, errors);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(author)
                .board(this.boardService.getBoardById(request.getBoardId()))
                .tag(request.getTagId() == null ? null : this.getCommentById(request.getTagId()))
                .build();

        this.commentRepository.save(comment);

        return this.refresh(comment);
    }

    private void createValidate(CommentRequest.Create request, Errors errors) {

        if (errors.hasErrors()) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_01_01,
                            errors
                    )
            );
        }

        if (!this.boardRepository.existsById(request.getBoardId())) {

            errors.rejectValue("boardId", "not found", "board that has id is not found");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_01_02,
                            errors
                    )
            );
        }

        if (request.getTagId() != null && !this.commentRepository.existsById(request.getTagId())) {

            errors.rejectValue("tagId", "not found", "tag that has id is not found");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_01_03,
                            errors
                    )
            );
        }
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

    public List<DataModel> getCommentsByBoard(Long boardId) {

        this.getCommentsByBoardValidate(boardId);

        Board board = this.boardService.getBoardById(boardId);

        return this.commentRepository.findAllByBoard(board).stream()
                .filter(comment -> comment.getTag() == null)
                .map(comment -> DataModel.of(CommentGetDto.of(comment), linkTo(this.getClass()).slash(comment.getId())))
                .collect(Collectors.toList());
    }

    private void getCommentsByBoardValidate(Long boardId) {

        Errors errors = AppConfig.getMockErrors("comment");

        if (!this.boardRepository.existsById(boardId)) {

            errors.reject("not found", new Object[]{boardId}, "board that has id requested for comments is not found");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_03_03_01,
                            errors
                    )
            );
        }
    }
}
