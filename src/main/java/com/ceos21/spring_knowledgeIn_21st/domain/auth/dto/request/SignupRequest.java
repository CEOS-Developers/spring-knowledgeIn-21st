package com.ceos21.spring_knowledgeIn_21st.domain.auth.dto.request;

import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import com.ceos21.spring_knowledgeIn_21st.domain.user.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

public record SignupRequest (
        String name,
        String email,
        String password
){
    public User toEntity(PasswordEncoder encoder) {
        return User.builder()
                .name(name)
                .email(email)
                .password(encoder.encode(password))
                .role(UserRole.USER)
                .build();
    }
}
