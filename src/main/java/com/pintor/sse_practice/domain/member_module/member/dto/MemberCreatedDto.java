package com.pintor.sse_practice.domain.member_module.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class MemberCreatedDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String username;

    private final List<String> authorities;

    private MemberCreatedDto(Member member) {
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.modifyDate = member.getModifyDate();
        this.username = member.getUsername();
        this.authorities = member.getAuthorities().stream()
                .map(e -> e.getAuthority()).toList();
    }

    public static MemberCreatedDto of(Member member) {
        return new MemberCreatedDto(member);
    }
}
