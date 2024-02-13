package com.pintor.sse_practice.domain.board_module.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.sse_practice.domain.board_module.comment.entity.Comment;
import com.pintor.sse_practice.global.response.DataModel;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CommentGetDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String content;

    private final String author;

    private final Long board;

    private final Long tag;

    private final List<DataModel> children;

    private CommentGetDto(Comment comment) {
        this.id = comment.getId();
        this.createDate = comment.getCreateDate();
        this.modifyDate = comment.getModifyDate();
        this.content = comment.getContent();
        this.author = comment.getAuthor().getUsername();
        this.board = comment.getBoard().getId();
        this.tag = comment.getTag() == null ? null : comment.getTag().getId();
        this.children = comment.getChildren().stream()
                .map(child -> DataModel.of(CommentGetDto.of(child), linkTo(this.getClass()).slash(child.getId())))
                .collect(Collectors.toList());
    }

    public static CommentGetDto of(Comment comment) {
        return new CommentGetDto(comment);
    }

    public List<DataModel> getChildren() {
        if (this.children.isEmpty()) return null;
        return this.children;
    }
}
