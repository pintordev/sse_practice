package com.pintor.sse_practice.domain.board_module.comment.service;

import com.pintor.sse_practice.domain.board_module.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;
}
