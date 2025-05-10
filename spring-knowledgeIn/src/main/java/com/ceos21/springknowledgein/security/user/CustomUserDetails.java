package com.ceos21.springknowledgein.security.user;

import com.ceos21.springknowledgein.user.repository.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Member member;

    //사용자의 권환을 반환하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(member.getRole().name()));
    }

    @Override public String getPassword() { return member.getPassword();}
    @Override public String getUsername() { return member.getUsername();}
    @Override public boolean isAccountNonExpired() {return true; } //만료된 계정인가?
    @Override public boolean isAccountNonLocked() {return true; } // 잠겨있는가?
    @Override public boolean isCredentialsNonExpired() {return true;} //비밀번호 만료인가?
    @Override public boolean isEnabled() { return true;} //계정 활성화인가?
    // 위 4개 boolean은 고려하지 않는 4개의 상황이여서 그냥 true반환하게 함.

    public Member getMember() { return member; }
}
