package com.ceos21.knowledgeIn.domain.member.service;

import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.member.MemberRepository;
import com.ceos21.knowledgeIn.domain.member.dto.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Member memberJoin(MemberRequestDTO.MemberJoinDTO requestDTO) {

        Member member = Member.builder()
                .email(requestDTO.getEmail())
                .nickname(requestDTO.getNickname())
                .password(bCryptPasswordEncoder.encode(requestDTO.getPassword()))
                .phone(requestDTO.getPhone())
                .name(requestDTO.getName())
                .build();

        memberRepository.save(member);

        return member;
    }
}
