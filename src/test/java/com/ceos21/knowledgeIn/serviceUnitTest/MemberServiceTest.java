package com.ceos21.knowledgeIn.serviceUnitTest;

import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.member.MemberRepository;
import com.ceos21.knowledgeIn.domain.member.MemberService;
import com.ceos21.knowledgeIn.domain.member.dto.MemberRequestDTO;
import com.ceos21.knowledgeIn.global.exceptionHandler.ErrorStatus;
import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 중복 가입")
    public void duplicateMemberJoin(){
        //given
        Member member1 = Member.builder().name("동영배").email("youngship@naver.com").password("1234").phone("01012341234").nickname("태양").build();
        Member member2 = Member.builder().name("서영배").email("youngship@naver.com").password("5678").phone("01011111111").nickname("태양은 둘이 될 수 없어").build();

        //when
        memberRepository.save(member1);

        //then
        Assertions.assertThrows(Exception.class, () -> memberRepository.save(member2));
    }

}
