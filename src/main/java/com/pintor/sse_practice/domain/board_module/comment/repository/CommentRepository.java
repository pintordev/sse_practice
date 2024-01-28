package com.pintor.sse_practice.domain.board_module.comment.repository;

import com.pintor.sse_practice.domain.board_module.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
