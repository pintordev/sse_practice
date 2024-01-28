package com.pintor.sse_practice.domain.board_module.board.service;

import com.pintor.sse_practice.domain.board_module.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BoardService {

    private BoardRepository boardRepository;
}
