package com.pintor.sse_practice.domain.board_module.board.service;

import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import com.pintor.sse_practice.domain.board_module.board.repository.BoardRepository;
import com.pintor.sse_practice.domain.board_module.board.request.BoardRequest;
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
public class BoardService {

    private final BoardRepository boardRepository;

    private final EntityManager entityManager;

    public long count() {
        return this.boardRepository.count();
    }

    private Board refresh(Board board) {
        entityManager.flush();
        board = this.getBoardById(board.getId());
        entityManager.refresh(board);
        return board;
    }

    public Board create(BoardRequest.Create request, Errors errors, Member author) {

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();

        this.boardRepository.save(board);

        return this.refresh(board);
    }

    public Board getBoardById(Long id) {

        Errors errors = AppConfig.getMockErrors("board");

        return this.boardRepository.findById(id)
                .orElseThrow(() -> new ApiResponseException(
                        ResData.of(
                                ResCode.F_02_02_01,
                                errors
                        )
                ));
    }
}
