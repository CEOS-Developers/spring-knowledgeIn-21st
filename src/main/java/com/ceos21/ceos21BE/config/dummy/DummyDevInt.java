package com.ceos21.ceos21BE.config.dummy;

import com.ceos21.ceos21BE.domain.Member;
import com.ceos21.ceos21BE.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyDevInt extends DummyObject{

    @Profile("dev")
    @Bean
    CommandLineRunner init(MemberRepository memberRepository) {
        return (args -> {
            Member member1 = memberRepository.save(newMockMember("hyesu", "hyhy1234", "hyesu@gmail.com", "hyesu", "hyesuuu"));
            Member member2 = memberRepository.save(newMockMember("ceos", "password1212", " ceos@gmail.com", "ceos", "ceos21"));
            Member member3 = memberRepository.save(newMockMember("hello", "junitTest", "hello@naver.com", "hello", "hello21"));
        });
    }
}
