package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createPost(){

        //given
        Member member = Member.builder().name("박채연").email("grace7759@naver.com").password("1234").nickname("사랑먼지").build();
        Member savedMember = memberRepository.save(member);
        Post post = Post.builder().title("테스트").member(savedMember).content("테스트 해봅니다").isAnonymous(false).build();

        //when
        Post savedPost = postRepository.save(post);
        Post foundPost = postRepository.findById(savedPost.getId()).orElseThrow(()->new RuntimeException("해당 게시글을 찾을 수 없습니다"));

        //then

    }

    @Transactional
    public void memberCheck(){
        Member foundMember = memberRepository.findById(1L).orElseThrow(()->new RuntimeException("회원을 찾을 수 없습니다"));
        System.out.println("크기:"+foundMember.getPosts().size());
    }
}
