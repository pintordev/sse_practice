package com.pintor.sse_practice.domain.board_module.board.controller;

import com.jayway.jsonpath.JsonPath;
import com.pintor.sse_practice.domain.board_module.board.request.BoardRequest;
import com.pintor.sse_practice.domain.board_module.board.service.BoardService;
import com.pintor.sse_practice.global.controller.BaseControllerTest;
import com.pintor.sse_practice.global.errors.exception.ApiResponseException;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.util.AppConfig;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("get:/api/boards/{id} - ok, S-02-02")
    public void getBoard_OK() throws Exception {

        // given
        Long id = 1L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/boards/%s".formatted(id))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-02-02"))
                .andExpect(jsonPath("message").value(ResCode.S_02_02.getMessage()))
                .andExpect(jsonPath("data.id").value(id))
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.title").exists())
                .andExpect(jsonPath("data.content").exists())
                .andExpect(jsonPath("data.author").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Test
    @DisplayName("get:/api/boards/{id} - bad request not found, F-02-02-01")
    public void getBoard_BadRequest_NotFound() throws Exception {

        // given
        Long id = this.boardService.count() + 1;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/boards/%s".formatted(id))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value("NOT_FOUND"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-02-02-01"))
                .andExpect(jsonPath("message").value(ResCode.F_02_02_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").value("board"))
                .andExpect(jsonPath("data[0].code").value("not found"))
                .andExpect(jsonPath("data[0].defaultMessage").value("board that has id is not found"))
                .andExpect(jsonPath("data[0].rejectedValue").value(id.toString()))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @ParameterizedTest
    @MethodSource("argsFor_getBoards_OK")
    @DisplayName("get:/api/boards - ok, S-02-03")
    public void getBoards_OK(Integer page, Integer size) throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (page != null) params.add("page", page.toString());
        if (size != null) params.add("size", size.toString());

        String query = AppConfig.getBaseURL() + ":8080/api/boards" + (AppConfig.getQueryString(params).isBlank() ? "" : "?%s".formatted(AppConfig.getQueryString(params)));

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/boards?%s".formatted(AppConfig.getQueryString(params)))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-02-03"))
                .andExpect(jsonPath("message").value(ResCode.S_02_03.getMessage()))
                .andExpect(jsonPath("data.page").value(1))
                .andExpect(jsonPath("data.sort[0].property").value("createDate"))
                .andExpect(jsonPath("data.sort[0].direction").value("desc"))
                .andExpect(jsonPath("_links.self.href").value(query))
                .andExpect(jsonPath("_links.profile").exists())
        ;

        int len = JsonPath.read(resultActions.andReturn().getResponse().getContentAsString(), "data.list.length()");

        if (len > 0) {
            resultActions
                    .andExpect(jsonPath("data.list[0].id").exists())
                    .andExpect(jsonPath("data.list[0].createDate").exists())
                    .andExpect(jsonPath("data.list[0].modifyDate").exists())
                    .andExpect(jsonPath("data.list[0].title").exists())
                    .andExpect(jsonPath("data.list[0].content").exists())
                    .andExpect(jsonPath("data.list[0].author").exists())
                    .andExpect(jsonPath("data.list[0]._links.self").exists())
            ;
        }

        if (size == null || size == 20) {
            resultActions
                    .andExpect(jsonPath("data.size").value(20))
            ;
        } else {
            resultActions
                    .andExpect(jsonPath("data.size").value(size))
            ;
        }
    }

    private static Stream<Arguments> argsFor_getBoards_OK() {

        Integer[] pages = {null, 1};
        Integer[] sizes = {null, 20, 50, 100};

        Stream.Builder<Arguments> argumentsBuilder = Stream.builder();

        for (Integer page : pages)
            for (Integer size : sizes)
                argumentsBuilder.add(Arguments.of(page, size));

        return argumentsBuilder.build();
    }

    @ParameterizedTest
    @MethodSource("argsFor_getBoards_BadRequest_PageNotExist")
    @DisplayName("get:/api/boards - bad request page not exist, F-02-03-01")
    public void getBoards_BadRequest_PageNotExist(Integer page, Integer size) throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (page != null) params.add("page", page.toString());
        if (size != null) params.add("size", size.toString());

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/boards?%s".formatted(AppConfig.getQueryString(params)))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-02-03-01"))
                .andExpect(jsonPath("message").value(ResCode.F_02_03_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").value("board"))
                .andExpect(jsonPath("data[0].code").value("not exist"))
                .andExpect(jsonPath("data[0].defaultMessage").value("requested page does not exist"))
                .andExpect(jsonPath("data[0].rejectedValue").value(page.toString()))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    private static Stream<Arguments> argsFor_getBoards_BadRequest_PageNotExist() {

        Integer[] pages = {-1, 0, 1000000};
        Integer[] sizes = {null, 20, 50, 100};

        Stream.Builder<Arguments> argumentsBuilder = Stream.builder();

        for (Integer page : pages)
            for (Integer size : sizes)
                argumentsBuilder.add(Arguments.of(page, size));

        return argumentsBuilder.build();
    }
}