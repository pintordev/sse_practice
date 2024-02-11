package com.pintor.sse_practice.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum ResCode {

    // member controller success code
    S_01_01(HttpStatus.CREATED, "S-01-01", "회원가입이 완료되었습니다"),
    S_01_02(HttpStatus.OK, "S-01-02", "액세스 토큰이 생성되었습니다"),

    // board controller success code
    S_02_01(HttpStatus.CREATED, "S-02-01", "게시글 등록이 완료되었습니다"),
    S_02_02(HttpStatus.OK, "S-02-02", "해당 게시글 정보를 반환합니다"),
    S_02_03(HttpStatus.OK, "S-02-03", "해당 게시글 목록 정보를 반환합니다"),

    // comment controller success code
    S_03_01(HttpStatus.CREATED, "S-03-01", "댓글 등록이 완료되었습니다"),

    // notification controller success code
    S_04_01(HttpStatus.CREATED, "S-04-01", "서버 연결이 완료되었습니다"),

    // member controller fail code
    F_01_01_01(HttpStatus.BAD_REQUEST, "F-01-01-01", "요청 값이 올바르지 않습니다"),
    F_01_01_02(HttpStatus.BAD_REQUEST, "F-01-01-02", "이미 존재하는 회원 이름입니다"),
    F_01_01_03(HttpStatus.BAD_REQUEST, "F-01-01-03", "비밀번호가 서로 일치하지 않습니다"),

    F_01_02_01(HttpStatus.BAD_REQUEST, "F-01-02-01", "요청 값이 올바르지 않습니다"),
    F_01_02_02(HttpStatus.NOT_FOUND, "F-01-02-02", "해당 아이디를 갖는 회원을 찾을 수 없습니다"),
    F_01_02_03(HttpStatus.BAD_REQUEST, "F-01-02-03", "비밀번호가 일치하지 않습니다"),

    F_01_03_01(HttpStatus.NOT_FOUND, "F-01-03-01", "해당 회원을 찾을 수 없습니다"),

    // board controller fail code
    F_02_01_01(HttpStatus.BAD_REQUEST, "F-02-01-01", "요청 값이 올바르지 않습니다"),

    F_02_02_01(HttpStatus.NOT_FOUND, "F-02-02-01", "해당 게시글을 찾을 수 없습니다"),

    F_02_03_01(HttpStatus.BAD_REQUEST, "F-02-03-01", "해당 페이지가 존재하지 않습니다"),

    // comment controller fail code
    F_03_01_01(HttpStatus.BAD_REQUEST, "F-03-01-01", "요청 값이 올바르지 않습니다"),

    F_03_02_01(HttpStatus.NOT_FOUND, "F-03-02-01", "해당 댓글을 찾을 수 없습니다"),

    // notification controller fail code
    F_04_01_01(HttpStatus.INTERNAL_SERVER_ERROR, "F-04-01-01", "서버 연결 중 오류가 발생했습니다"),

    // system fail code
    F_99_99_01(HttpStatus.UNAUTHORIZED, "F-99-99-01", "로그인이 필요한 요청입니다"),
    F_99_99_02(HttpStatus.FORBIDDEN, "F-99-99-02", "접근 권한이 없습니다"),
    F_99_99_03(HttpStatus.BAD_REQUEST, "F-99-99-03", "입력 양식이 올바르지 않습니다"),
    F_99_99_04(HttpStatus.BAD_REQUEST, "F-99-99-04", "필수 입력 요소가 존재하지 않습니다"),
    F_99_99_05(HttpStatus.BAD_REQUEST, "F-99-99-05", "입력 타입이 올바르지 않습니다"),
    F_99_99_99(HttpStatus.INTERNAL_SERVER_ERROR, "F-99-99-99", "알 수 없는 에러가 발생했습니다");

    private HttpStatus status;
    private String code;
    private String message;

    ResCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static ResCode fromCode(String code) {
        return Arrays.stream(ResCode.values())
                .filter(resCode -> resCode.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
