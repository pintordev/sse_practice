package com.pintor.sse_practice.domain.board_module.comment.controller;

import com.pintor.sse_practice.domain.board_module.comment.dto.CommentCreatedDto;
import com.pintor.sse_practice.domain.board_module.comment.entity.Comment;
import com.pintor.sse_practice.domain.board_module.comment.request.CommentRequest;
import com.pintor.sse_practice.domain.board_module.comment.service.CommentService;
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
}
