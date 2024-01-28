package com.pintor.sse_practice.domain.board_module.board.controller;

import com.pintor.sse_practice.domain.board_module.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/boards", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class BoardController {

    private BoardService boardService;
}
