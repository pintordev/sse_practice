package com.pintor.sse_practice.global.errors.exception;

import com.pintor.sse_practice.global.response.ResData;
import lombok.Getter;

@Getter
public class ApiResponseException extends RuntimeException {

    private final ResData resData;

    public ApiResponseException(ResData resData) {
        super("response failed");
        this.resData = resData;
    }
}
