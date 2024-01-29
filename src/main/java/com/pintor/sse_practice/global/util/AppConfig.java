package com.pintor.sse_practice.global.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String getBaseURL() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    public static String getIndexURL() {
        return getBaseURL() + "/api/swagger-ui/index.html";
    }

    public static String getQueryString(MultiValueMap<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join("&", entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    public static User toUser(Member member) {
        return new User(member.getUsername(), member.getPassword(), member.getAuthorities());
    }

    public static String toCamelCase(String property) {
        String[] bits = property.split("_");
        return IntStream.range(0, bits.length)
                .mapToObj(i -> i != 0 ? bits[i].substring(0, 1).toUpperCase() + bits[i].substring(1) : bits[i])
                .collect(Collectors.joining());
    }

    public static Errors getMockErrors() {
        return new BeanPropertyBindingResult(null, "request");
    }

    public static Errors getMockErrors(String objectName) {
        return new BeanPropertyBindingResult(null, objectName);
    }

    public static Errors getMockErrors(Object object, String objectName) {
        return new BeanPropertyBindingResult(object, objectName);
    }

    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static Map<String, Object> toMap(String jsonString) {
        try {
            return new ObjectMapper().readValue(jsonString, LinkedHashMap.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String responseSerialize(ResData resData) {

        try {
            StringWriter writer = new StringWriter();
            JsonGenerator gen = new JsonFactory().createGenerator(writer);

            gen.writeStartObject();

            gen.writeStringField("status", resData.getStatus().name());
            gen.writeBooleanField("success", resData.isSuccess());
            gen.writeStringField("code", resData.getCode().toString());
            gen.writeStringField("message", resData.getMessage().toString());

            if (!resData.getLinks().isEmpty()) {
                gen.writeFieldName("_links");
                gen.writeStartObject();

                resData.getLinks().forEach(link -> {
                    try {
                        gen.writeFieldName(link.getRel().toString());
                        gen.writeStartObject();
                        gen.writeStringField("href", link.getHref());
                        gen.writeEndObject();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                gen.writeEndObject();
            }

            gen.writeEndObject();
            gen.close();
            writer.close();
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
