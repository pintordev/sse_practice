package com.pintor.sse_practice.domain.board_module.comment.repository;

import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import com.pintor.sse_practice.domain.board_module.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoard(Board board);
}
