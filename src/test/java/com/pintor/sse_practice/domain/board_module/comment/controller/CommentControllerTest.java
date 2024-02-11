package com.pintor.sse_practice.domain.board_module.comment.controller;

import com.pintor.sse_practice.domain.board_module.comment.request.CommentRequest;
import com.pintor.sse_practice.domain.board_module.comment.service.CommentService;
import com.pintor.sse_practice.global.controller.BaseControllerTest;
import com.pintor.sse_practice.global.errors.exception.ApiResponseException;
import com.pintor.sse_practice.global.response.ResCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends BaseControllerTest {

    @Autowired
    private CommentService commentService;

    @Transactional
    @ParameterizedTest
    @MethodSource("argsFor_createComment_Created")
    @DisplayName("post:/api/comments - created, S-03-01")
    public void createComment_Created(Long tagId, int i) throws Exception {

        // given
        long count = this.commentService.count() + i;

        String username = "member1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        String content = "test content";
        Long boardId = 1L;
        CommentRequest.Create request = CommentRequest.Create.builder()
                .content(content)
                .boardId(boardId)
                .tagId(tagId)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/comments")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-03-01"))
                .andExpect(jsonPath("message").value(ResCode.S_03_01.getMessage()))
                .andExpect(jsonPath("data.id").value(count + 1))
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.content").value(content))
                .andExpect(jsonPath("data.author").value(username))
                .andExpect(jsonPath("data.board").value(boardId))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;

        if (tagId != null) {
            resultActions
                    .andExpect(jsonPath("data.tag").value(tagId))
            ;
        }

        assertDoesNotThrow(() -> this.commentService.getCommentById(count + 1));
    }

    private static Stream<Arguments> argsFor_createComment_Created() {

        Long[] tagIds = {null, 1L};
        int i = 0;

        Stream.Builder<Arguments> argumentsBuilder = Stream.builder();

        for (Long tagId : tagIds)
            argumentsBuilder.add(Arguments.of(tagId, i++));

        return argumentsBuilder.build();
    }

    @ParameterizedTest
    @MethodSource("argsFor_createComment_BadRequest_NotBlank")
    @DisplayName("post:/api/comments - bad request not blank, F-03-01-01")
    public void createComment_BadRequest_NotBlank(String content, Long boardId) throws Exception {

        // given
        long count = this.commentService.count();

        String username = "member1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        CommentRequest.Create request = CommentRequest.Create.builder()
                .content(content)
                .boardId(boardId)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/comments")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-01-01"))
                .andExpect(jsonPath("message").value(ResCode.F_03_01_01.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;

        if (content.isBlank()) {
            resultActions
                    .andExpect(jsonPath("data[0].rejectedValue").value(" "))
            ;
        }

        assertThrows(ApiResponseException.class, () -> this.commentService.getCommentById(count + 1));
    }

    private static Stream<Arguments> argsFor_createComment_BadRequest_NotBlank() {

        String[] contents = {" ", "test content"};
        Long[] boardIds = {null, 1L};

        Stream.Builder<Arguments> argumentsBuilder = Stream.builder();

        for (String content : contents)
            for (Long boardId : boardIds)
                    if (content.isBlank() || boardId == null)
                        argumentsBuilder.add(Arguments.of(content, boardId));

        return argumentsBuilder.build();
    }
}