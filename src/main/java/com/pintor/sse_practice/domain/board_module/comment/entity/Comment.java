package com.pintor.sse_practice.domain.board_module.comment.entity;

import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Comment extends BaseEntity {

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment tag;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> children;
}
