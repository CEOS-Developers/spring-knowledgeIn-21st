package com.ceos21.spring_boot.Service.auth;

import com.ceos21.spring_boot.DTO.auth.CustomUserDetailsDTO;
import com.ceos21.spring_boot.Domain.user.User;
import com.ceos21.spring_boot.Repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userData = userRepository.findByEmail(email);
        if(userData != null) {
            return new CustomUserDetailsDTO(userData);
        }
        return null;
    }

}
