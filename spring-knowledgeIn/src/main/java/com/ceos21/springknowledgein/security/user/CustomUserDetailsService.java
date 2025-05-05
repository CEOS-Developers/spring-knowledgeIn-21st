package com.ceos21.springknowledgein.security.user;

import com.ceos21.springknowledgein.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(
                memberRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"))
        );
    }
}