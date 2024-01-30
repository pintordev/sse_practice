package com.pintor.sse_practice.global.errors.exception_handler;

import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import com.pintor.sse_practice.global.util.AppConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiAuthorizationExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType(MediaTypes.HAL_JSON_VALUE);
        response.setStatus(ResCode.F_99_99_02.getStatus().value());
        ResData resData = ResData.of(ResCode.F_99_99_02);
        resData.add(Link.of(AppConfig.getIndexURL()).withRel("index"));
        response.getWriter().write(AppConfig.responseSerialize(resData));
    }
}
