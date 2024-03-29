package com.pintor.sse_practice.domain.board_module.comment.controller;

import com.pintor.sse_practice.domain.board_module.comment.dto.CommentCreatedDto;
import com.pintor.sse_practice.domain.board_module.comment.dto.CommentGetDto;
import com.pintor.sse_practice.domain.board_module.comment.entity.Comment;
import com.pintor.sse_practice.domain.board_module.comment.request.CommentRequest;
import com.pintor.sse_practice.domain.board_module.comment.service.CommentService;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.domain.member_module.member.service.MemberService;
import com.pintor.sse_practice.global.response.DataModel;
import com.pintor.sse_practice.global.response.ListDataModel;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import com.pintor.sse_practice.global.util.AppConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping(value = "/api/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity createComment(@Valid @RequestBody CommentRequest.Create request, Errors errors,
                                        @AuthenticationPrincipal User user) {

        Member author = this.memberService.getMemberByUsername(user.getUsername());
        Comment comment = this.commentService.create(request, errors, author);

        ResData resData = ResData.of(
                ResCode.S_03_01,
                CommentCreatedDto.of(comment),
                linkTo(this.getClass()).slash(comment.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/comments/createComment").withRel("profile"));
        return ResponseEntity.created(linkTo(this.getClass()).slash(comment.getId()).toUri())
                .body(resData);
    }

    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity getComment(@PathVariable("id") Long id) {

        Comment comment = this.commentService.getCommentById(id);

        ResData resData = ResData.of(
                ResCode.S_03_02,
                CommentGetDto.of(comment),
                linkTo(this.getClass()).slash(comment.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/comments/getComment").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity getComments(@RequestParam("boardId") Long boardId,
                                      HttpServletRequest request) {

        List<DataModel> comment = this.commentService.getCommentsByBoard(boardId);

        ResData resData = ResData.of(
                ResCode.S_03_03,
                ListDataModel.of(comment),
                linkTo(this.getClass()).slash(request.getQueryString() != null ? "?%s".formatted(request.getQueryString()) : "")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/comments/getComments").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }
}
