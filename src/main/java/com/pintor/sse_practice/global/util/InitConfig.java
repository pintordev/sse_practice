package com.pintor.sse_practice.global.util;

import com.pintor.sse_practice.domain.board_module.board.entity.Board;
import com.pintor.sse_practice.domain.board_module.board.request.BoardRequest;
import com.pintor.sse_practice.domain.board_module.board.service.BoardService;
import com.pintor.sse_practice.domain.board_module.comment.entity.Comment;
import com.pintor.sse_practice.domain.board_module.comment.request.CommentRequest;
import com.pintor.sse_practice.domain.board_module.comment.service.CommentService;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.domain.member_module.member.request.MemberRequest;
import com.pintor.sse_practice.domain.member_module.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitConfig {

    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;

    @Bean
    public ApplicationRunner runner() {
        return args -> {

            if (this.memberService.count() != 0) {
                log.info("데이터 초기화 미실행");
                return;
            }

            log.info("데이터 초기화 실행");

            Member admin = this.memberService.signup(
                    MemberRequest.SignUp.builder()
                            .username("admin")
                            .password("1234")
                            .passwordConfirm("1234")
                            .build(),
                    true,
                    AppConfig.getMockErrors()
            );

            Member member1 = this.memberService.signup(
                    MemberRequest.SignUp.builder()
                            .username("member1")
                            .password("1234")
                            .passwordConfirm("1234")
                            .build(),
                    true,
                    AppConfig.getMockErrors()
            );

            Member member2 = this.memberService.signup(
                    MemberRequest.SignUp.builder()
                            .username("member2")
                            .password("1234")
                            .passwordConfirm("1234")
                            .build(),
                    true,
                    AppConfig.getMockErrors()
            );

            Board board1 = this.boardService.create(
                    BoardRequest.Create.builder()
                            .title("board1 title")
                            .content("board1 content")
                            .build(),
                    AppConfig.getMockErrors(),
                    member1
            );

            Board board2 = this.boardService.create(
                    BoardRequest.Create.builder()
                            .title("board2 title")
                            .content("board2 content")
                            .build(),
                    AppConfig.getMockErrors(),
                    member2
            );

            Comment comment1 = this.commentService.create(
                    CommentRequest.Create.builder()
                            .content("comment1 content")
                            .boardId(board1.getId())
                            .build(),
                    AppConfig.getMockErrors(),
                    member1
            );

            Comment comment2 = this.commentService.create(
                    CommentRequest.Create.builder()
                            .content("comment2 content")
                            .boardId(board1.getId())
                            .tagId(comment1.getId())
                            .build(),
                    AppConfig.getMockErrors(),
                    member2
            );
        };
    }
}
