package com.ceos21.knowledgeIn.domain.member.service;

import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.member.MemberRepository;
import com.ceos21.knowledgeIn.domain.member.domain.CustomUserDetails;
import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import com.ceos21.knowledgeIn.global.exceptionHandler.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email).orElseThrow(()->new GeneralException(Status.MEMBER_NOT_FOUND));
        CustomUserDetails userDetails = new CustomUserDetails(member);

        return userDetails;
    }
}
