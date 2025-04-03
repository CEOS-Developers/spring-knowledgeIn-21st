package com.ceos21.knowledgeIn.domain.member;

import com.ceos21.knowledgeIn.domain.member.dto.MemberRequestDTO;
import com.ceos21.knowledgeIn.global.exceptionHandler.Status;
import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public String memberJoin(MemberRequestDTO.MemberJoinDTO requestDTO) {

        if(memberRepository.existsByEmail(requestDTO.getEmail())){
            throw new GeneralException(Status.ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .email(requestDTO.getEmail())
                .nickname(requestDTO.getNickname())
                .password(requestDTO.getPassword())
                .phone(requestDTO.getPhone())
                .name(requestDTO.getName())
                .build();

        memberRepository.save(member);

        return member.getName();
    }
}
