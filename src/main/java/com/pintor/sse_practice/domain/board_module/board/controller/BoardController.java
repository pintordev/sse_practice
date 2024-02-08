package com.pintor.sse_practice.domain.board_module.board.controller;

import com.pintor.sse_practice.domain.board_module.board.dto.BoardCreatedDto;
import com.pintor.sse_practice.domain.board_module.board.dto.BoardGetDto;
import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import com.pintor.sse_practice.domain.board_module.board.request.BoardRequest;
import com.pintor.sse_practice.domain.board_module.board.service.BoardService;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.domain.member_module.member.service.MemberService;
import com.pintor.sse_practice.global.response.DataModel;
import com.pintor.sse_practice.global.response.PagedDataModel;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import com.pintor.sse_practice.global.util.AppConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping(value = "/api/boards", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity createBoard(@Valid @RequestBody BoardRequest.Create request, Errors errors,
                                      @AuthenticationPrincipal User user) {

        Member author = this.memberService.getMemberByUsername(user.getUsername());
        Board board = this.boardService.create(request, errors, author);

        ResData resData = ResData.of(
                ResCode.S_02_01,
                BoardCreatedDto.of(board),
                linkTo(this.getClass()).slash(board.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/boards/createBoard").withRel("profile"));
        return ResponseEntity.created(linkTo(this.getClass()).slash(board.getId()).toUri())
                .body(resData);
    }

    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity getBoard(@PathVariable("id") Long id) {

        Board board = this.boardService.getBoardById(id);

        ResData resData = ResData.of(
                ResCode.S_02_02,
                BoardGetDto.of(board),
                linkTo(this.getClass()).slash(board.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/boards/getBoard").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity getBoards(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "20") int size,
                                    HttpServletRequest request) {


        Page<DataModel> boardPages = this.boardService.getBoardPages(page, size);

        ResData resData = ResData.of(
                ResCode.S_02_03,
                PagedDataModel.of(boardPages),
                linkTo(this.getClass()).slash(request.getQueryString() != null ? "?%s".formatted(request.getQueryString()) : "")
        );

        resData.add(Link.of(AppConfig.getBaseURL() + "/boards/getBoards").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

}
