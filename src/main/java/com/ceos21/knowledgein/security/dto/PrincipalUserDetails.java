package com.ceos21.knowledgein.security.dto;

import com.ceos21.knowledgein.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PrincipalUserDetails implements UserDetails, OAuth2User {

    private final UserEntity userEntity;
    private final Map<String, Object> attributes;

    public PrincipalUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.attributes = new HashMap<>();
    }

    // 일반 user
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleValue = userEntity.getRole().getValue();
        return List.of(new SimpleGrantedAuthority(roleValue));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
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
        return userEntity.isEnabled();
    }


    // OAuth2User

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return userEntity.getEmail();
    }
}
