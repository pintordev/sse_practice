package com.pintor.sse_practice.domain.board_module.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.sse_practice.domain.board_module.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CommentCreatedDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String content;

    private final String author;

    private final Long board;

    private final Long tag;

    private CommentCreatedDto(Comment comment) {
        this.id = comment.getId();
        this.createDate = comment.getCreateDate();
        this.modifyDate = comment.getModifyDate();
        this.content = comment.getContent();
        this.author = comment.getAuthor().getUsername();
        this.board = comment.getBoard().getId();
        this.tag = comment.getTag() == null ? null : comment.getTag().getId();
    }

    public static CommentCreatedDto of(Comment comment) {
        return new CommentCreatedDto(comment);
    }
}
