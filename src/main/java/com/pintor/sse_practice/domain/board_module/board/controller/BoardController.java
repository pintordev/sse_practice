package com.pintor.sse_practice.domain.board_module.board.controller;

import com.pintor.sse_practice.domain.board_module.board.dto.BoardCreatedDto;
import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import com.pintor.sse_practice.domain.board_module.board.request.BoardRequest;
import com.pintor.sse_practice.domain.board_module.board.service.BoardService;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.domain.member_module.member.service.MemberService;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import com.pintor.sse_practice.global.util.AppConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
