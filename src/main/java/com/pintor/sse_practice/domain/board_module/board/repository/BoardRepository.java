package com.pintor.sse_practice.domain.board_module.board.repository;

import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
