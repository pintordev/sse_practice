package com.pintor.sse_practice.domain.board_module.board.controller;

import com.pintor.sse_practice.domain.board_module.board.request.BoardRequest;
import com.pintor.sse_practice.domain.board_module.board.service.BoardService;
import com.pintor.sse_practice.global.controller.BaseControllerTest;
import com.pintor.sse_practice.global.errors.exception.ApiResponseException;
import com.pintor.sse_practice.global.response.ResCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

class BoardControllerTest extends BaseControllerTest {

    @Autowired
    private BoardService boardService;

    @Transactional
    @Test
    @DisplayName("post:/api/boards - created, S-02-01")
    public void createBoard_Created() throws Exception {

        // given
        long count = this.boardService.count();

        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        String title = "test title";
        String content = "test content";
        BoardRequest.Create request = BoardRequest.Create.builder()
                .title(title)
                .content(content)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/boards")
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
                .andExpect(jsonPath("code").value("S-02-01"))
                .andExpect(jsonPath("message").value(ResCode.S_02_01.getMessage()))
                .andExpect(jsonPath("data.id").value(count + 1))
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.title").value(title))
                .andExpect(jsonPath("data.content").value(content))
                .andExpect(jsonPath("data.author").value(username))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;

        assertDoesNotThrow(() -> this.boardService.getBoardById(count + 1));
    }

    @ParameterizedTest
    @MethodSource("argsFor_createBoard_BadRequest_NotBlank")
    @DisplayName("post:/api/boards - bad request not blank, F-02-01-01")
    public void createBoard_BadRequest_NotBlank(String title, String content) throws Exception {

        // given
        long count = this.boardService.count();

        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        BoardRequest.Create request = BoardRequest.Create.builder()
                .title(title)
                .content(content)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/boards")
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
                .andExpect(jsonPath("code").value("F-02-01-01"))
                .andExpect(jsonPath("message").value(ResCode.F_02_01_01.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(" "))
                .andExpect(jsonPath("_links.index").exists())
        ;

        assertThrows(ApiResponseException.class, () -> this.boardService.getBoardById(count + 1));
    }

    private static Stream<Arguments> argsFor_createBoard_BadRequest_NotBlank() {

        String[] titles = {" ", "test title"};
        String[] contents = {" ", "test content"};

        Stream.Builder<Arguments> argumentsBuilder = Stream.builder();

        for (String title : titles)
            for (String content : contents)
                if (title.isBlank() || content.isBlank())
                    argumentsBuilder.add(Arguments.of(title, content));

        return argumentsBuilder.build();
    }

}