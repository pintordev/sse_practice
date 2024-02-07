package com.pintor.sse_practice.domain.board_module.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import com.pintor.sse_practice.domain.board_module.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class BoardGetDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String title;

    private final String content;

    private final List<Comment> comments;

    private final String author;

    private BoardGetDto(Board board) {
        this.id = board.getId();
        this.createDate = board.getCreateDate();
        this.modifyDate = board.getModifyDate();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.comments = board.getComments();
        this.author = board.getAuthor().getUsername();
    }

    public static BoardGetDto of(Board board) {
        return new BoardGetDto(board);
    }

    public List<Comment> getComments() {
        if (this.comments.size() == 0) return null;
        else return this.comments;
    }
}
