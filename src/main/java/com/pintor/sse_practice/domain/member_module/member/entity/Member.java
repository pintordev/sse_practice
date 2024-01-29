package com.pintor.sse_practice.domain.member_module.member.entity;

import com.pintor.sse_practice.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Member extends BaseEntity {

    @Column(unique = true)
    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private List<MemberRole> authorities;

    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.authorities.stream()
                .map(a -> new SimpleGrantedAuthority(a.getType()))
                .collect(Collectors.toList());
    }
}
