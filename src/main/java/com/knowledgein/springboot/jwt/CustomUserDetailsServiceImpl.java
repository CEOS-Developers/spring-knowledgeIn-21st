package com.knowledgein.springboot.jwt;

import com.knowledgein.springboot.apiPayload.code.status.ErrorStatus;
import com.knowledgein.springboot.apiPayload.exception.GeneralException;
import com.knowledgein.springboot.domain.User;
import com.knowledgein.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User userData = userRepository.findByEmail(email).orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));

        return new CustomUserDetails(userData);
    }
}
