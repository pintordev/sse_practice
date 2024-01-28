package com.pintor.sse_practice.domain.board_module.comment.controller;

import com.pintor.sse_practice.domain.board_module.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
}
