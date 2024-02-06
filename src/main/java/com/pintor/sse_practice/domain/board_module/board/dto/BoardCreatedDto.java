package com.pintor.sse_practice.domain.board_module.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class BoardCreatedDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String title;

    private final String content;

    private final String author;

    private BoardCreatedDto(Board board) {
        this.id = board.getId();
        this.createDate = board.getCreateDate();
        this.modifyDate = board.getModifyDate();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.author = board.getAuthor().getUsername();
    }

    public static BoardCreatedDto of(Board board) {
        return new BoardCreatedDto(board);
    }
}
