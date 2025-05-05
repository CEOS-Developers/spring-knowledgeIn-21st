package com.ceos21.knowledgein.security.service;

import com.ceos21.knowledgein.security.dto.PrincipalUserDetails;
import com.ceos21.knowledgein.user.domain.UserEntity;
import com.ceos21.knowledgein.user.exception.AuthException;
import com.ceos21.knowledgein.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.ceos21.knowledgein.user.exception.AuthErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUsersDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new AuthException(USER_NOT_FOUND));

        return new PrincipalUserDetails(userEntity);
    }
}
