package com.ceos21.knowledgeIn.security.auth.user.detail;

import com.ceos21.knowledgeIn.domain.user.Role;
import com.ceos21.knowledgeIn.domain.user.User;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDetails implements UserDetails {
    private Long userId;
    private String name;
    private String nickname;
    private String username;
    private String password;
    private List<Role> roles;

    static public CustomUserDetails from(User entity) {
        return CustomUserDetails.builder()
                .userId(entity.getId())
                .name(entity.getName())
                .nickname(entity.getNickname())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .roles(new ArrayList<>(entity.getRoles()))
                .build();
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        return authorities; // 필요시 ROLE_USER 등 추가 가능
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
