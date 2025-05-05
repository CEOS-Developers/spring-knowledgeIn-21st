package com.ceos21.spring_boot.Service.auth;

import com.ceos21.spring_boot.DTO.auth.JoinDTO;
import com.ceos21.spring_boot.Domain.user.User;
import com.ceos21.spring_boot.Repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO) {
        String userId = joinDTO.getUserId();
        String password = joinDTO.getPassword();
        String username = joinDTO.getUsername();
        String email = joinDTO.getEmail();
        LocalDate birthdate = joinDTO.getBirthdate();
        String phoneNumber = joinDTO.getPhoneNumber();

        Boolean isExist = userRepository.existsByUsername(username);

        if(isExist) {
            return;
        }

        User data = User.builder()
                .userId(userId)
                .password(bCryptPasswordEncoder.encode(password))
                .username(username)
                .email(email)
                .birthdate(birthdate)
                .phoneNumber(phoneNumber)
                .role("ROLE_ADMIN")
                .build();


        userRepository.save(data);
    }
}
