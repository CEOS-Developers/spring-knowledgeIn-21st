package com.ceos21.springknowledgein.user.service;


import com.ceos21.springknowledgein.user.UserRoleEnum;
import com.ceos21.springknowledgein.user.dto.SignupRequestDto;
import com.ceos21.springknowledgein.user.repository.Member;
import com.ceos21.springknowledgein.user.repository.MemberRepository;
import com.ceos21.springknowledgein.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto dto) {
        String username = dto.getUsername();
        String rawPassword = dto.getPassword();

        if (memberRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserRoleEnum role = UserRoleEnum.ROLE_USER;

        Member member = new Member();
        member.setUsername(username);
        member.setPassword(encodedPassword);
        member.setRole(role);

        memberRepository.save(member);
    }

}
