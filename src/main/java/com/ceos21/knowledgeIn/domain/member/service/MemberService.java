package com.ceos21.knowledgeIn.domain.member.service;

import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.member.MemberRepository;
import com.ceos21.knowledgeIn.domain.member.domain.CustomUserDetails;
import com.ceos21.knowledgeIn.domain.member.domain.MemberRole;
import com.ceos21.knowledgeIn.domain.member.dto.JwtTokenDTO;
import com.ceos21.knowledgeIn.domain.member.dto.MemberRequestDTO;
import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import com.ceos21.knowledgeIn.global.exceptionHandler.Status;
import com.ceos21.knowledgeIn.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Member memberJoin(MemberRequestDTO.JoinDTO requestDTO) {

        Member member = Member.builder()
                .email(requestDTO.getEmail())
                .nickname(requestDTO.getNickname())
                .password(bCryptPasswordEncoder.encode(requestDTO.getPassword()))
                .phone(requestDTO.getPhone())
                .name(requestDTO.getName())
                .role(MemberRole.MEMBER)
                .build();

        memberRepository.save(member);

        return member;
    }

    @Transactional
    public JwtTokenDTO signIn(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new GeneralException(Status.MEMBER_NOT_FOUND));

        //1. Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        //2. 실제 검증
        //authenticate() 메소드가 실행될 때 CustomUserDetailService에 오버라이드한 loadUserByUsername 실행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        //3. 인증 정보 기반 jwt토큰 생성
        JwtTokenDTO token = jwtTokenProvider.generateToken(authentication);

        return token;
    }
}
