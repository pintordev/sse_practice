package com.pintor.sse_practice.domain.member_module.member.service;

import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.domain.member_module.member.entity.MemberRole;
import com.pintor.sse_practice.domain.member_module.member.repository.MemberRepository;
import com.pintor.sse_practice.domain.member_module.member.request.MemberRequest;
import com.pintor.sse_practice.global.errors.exception.ApiResponseException;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import com.pintor.sse_practice.global.util.AppConfig;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public long count() {
        return this.memberRepository.count();
    }

    private Member refresh(Member member) {
        entityManager.flush();
        member = this.getMemberById(member.getId());
        entityManager.refresh(member);
        return member;
    }

    @Transactional
    public Member signup(MemberRequest.SignUp request, Errors errors) {

        Member member = Member.builder()
                .username(request.getUsername())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .authorities(List.of(MemberRole.USER))
                .build();

        this.memberRepository.save(member);

        return this.refresh(member);
    }

    public Member getMemberById(Long id) {

        Errors errors = AppConfig.getMockErrors("member");

        return this.memberRepository.findById(id)
                .orElseThrow(() -> new ApiResponseException(
                        ResData.of(
                                ResCode.F_01_03_01,
                                errors
                        )
                ));
    }
}
