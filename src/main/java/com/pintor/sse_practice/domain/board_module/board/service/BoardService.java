package com.pintor.sse_practice.domain.board_module.board.service;

import com.pintor.sse_practice.domain.board_module.board.controller.BoardController;
import com.pintor.sse_practice.domain.board_module.board.dto.BoardGetDto;
import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import com.pintor.sse_practice.domain.board_module.board.repository.BoardRepository;
import com.pintor.sse_practice.domain.board_module.board.request.BoardRequest;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.global.errors.exception.ApiResponseException;
import com.pintor.sse_practice.global.response.DataModel;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import com.pintor.sse_practice.global.util.AppConfig;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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

        this.createValidate(request, errors);

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();

        this.boardRepository.save(board);

        return this.refresh(board);
    }

    private void createValidate(BoardRequest.Create request, Errors errors) {

        if (errors.hasErrors()) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_02_01_01,
                            errors
                    )
            );
        }
    }

    public Board getBoardById(Long id) {

        return this.boardRepository.findById(id)
                .orElseThrow(() -> {

                    Errors errors = AppConfig.getMockErrors("board");
                    errors.reject("not found", new Object[]{id}, "board that has id is not found");

                    return new ApiResponseException(
                            ResData.of(
                                    ResCode.F_02_02_01,
                                    errors
                            )
                    );
                });
    }

    public Page<DataModel> getBoardPages(int page, int size) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sorts));

        return this.boardRepository.findAll(pageable)
                .map(board -> {
                    DataModel dataModel = DataModel.of(
                            BoardGetDto.of(board),
                            linkTo(BoardController.class).slash(board.getId())
                    );
                    return dataModel;
                });
    }
}
