package com.pintor.sse_practice.domain.member_module.member.service;

import com.pintor.sse_practice.domain.member_module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;
}
