package com.pintor.sse_practice.global.util;

import com.pintor.sse_practice.domain.board_module.board.service.BoardService;
import com.pintor.sse_practice.domain.board_module.comment.service.CommentService;
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

        };
    }
}
