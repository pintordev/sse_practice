package com.pintor.sse_practice.domain.member_module.member.repository;

import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
