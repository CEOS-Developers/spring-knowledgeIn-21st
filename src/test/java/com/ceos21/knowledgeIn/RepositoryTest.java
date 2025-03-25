package com.ceos21.knowledgeIn;

import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.member.MemberRepository;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.post.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;


//@SpringBootTest

@DataJpaTest
public class RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;

    @DisplayName("멤버 추가")
    @Test
    @Transactional
    void createMember(){


        //given
        Member member = Member.builder().name("박채연").email("pcy@naver.com").password("1234").nickname("사랑먼지").build();
        Member member2 = Member.builder().name("동영배").email("pcy@naver.com").password("2222").nickname("태양").build();

        //when
        Member savedMember = memberRepository.save(member);
        //        //Member savedMember2 = memberRepository.save(member2); //실패해야하는 코드

        Member foundMember = memberRepository.findById(savedMember.getId()).orElseThrow(()->new RuntimeException("존재하지 않는 회원"));

        //then
        Assertions.assertThat(foundMember.getId()).isEqualTo(savedMember.getId());

    }

    @DisplayName("게시글 작성")
    @Test
    void createPost(){

        //given
        Member member = Member.builder().name("박채연").email("pcy@naver.com").password("1234").nickname("사랑먼지").build();
        Member savedMember = memberRepository.save(member);
        Post post1 = Post.builder().title("테스트1").member(savedMember).content("테스트 해봅니다").isAnonymous(false).build();
        Post post2 = Post.builder().title("테스트2").member(savedMember).content("테스트 해봅니다").isAnonymous(false).build();
        Post post3 = Post.builder().title("테스트3").member(savedMember).content("테스트 해봅니다").isAnonymous(false).build();

        //when
        Post savedPost1 = postRepository.save(post1);
        Post savedPost2 = postRepository.save(post2);
        Post savedPost3 = postRepository.save(post3);
        Post foundPost1 = postRepository.findById(savedPost1.getId()).orElseThrow(()->new RuntimeException("해당 게시글을 찾을 수 없습니다"));
        Post foundPost2 = postRepository.findById(savedPost2.getId()).orElseThrow(()->new RuntimeException("해당 게시글을 찾을 수 없습니다"));
        Post foundPost3 = postRepository.findById(savedPost3.getId()).orElseThrow(()->new RuntimeException("해당 게시글을 찾을 수 없습니다"));

        //then
        Assertions.assertThat(foundPost1.getTitle()).isEqualTo(post1.getTitle());
        Assertions.assertThat(foundPost2.getTitle()).isEqualTo(post2.getTitle());
        Assertions.assertThat(foundPost3.getTitle()).isEqualTo(post3.getTitle());

        Member foundMember = memberRepository.findById(savedMember.getId()).orElseThrow(()->new RuntimeException("회원을 찾을 수 없습니다"));
        //Assertions.assertThat(foundMember.getPosts().size()).isEqualTo(1); //실패해야하는 코드


    }


}
