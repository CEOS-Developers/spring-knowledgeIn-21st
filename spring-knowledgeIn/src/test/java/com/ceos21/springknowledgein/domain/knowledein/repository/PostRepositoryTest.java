package com.ceos21.springknowledgein.domain.knowledein.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ceos21.springknowledgein.knowledgein.domain.Post;
import com.ceos21.springknowledgein.knowledgein.repository.PostRepository;
import com.ceos21.springknowledgein.user.UserRoleEnum;
import com.ceos21.springknowledgein.user.repository.Member;
import com.ceos21.springknowledgein.user.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml") // MySQL 설정 적용
@Transactional
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Post 저장 및 조회 테스트")
    void saveAndFindPosts() {
        //given: 테스트 데이터 생성
        Member member = new Member("testuser", "testpassword", UserRoleEnum.ROLE_USER);
        memberRepository.save(member);

        Post post1 = new Post("Title1", "Content1", member);
        Post post2 = new Post("Title2", "Content2", member);
        Post post3 = new Post("Title3", "Content3", member);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        //when => 저장된 데이터 조회
        List<Post> posts = postRepository.findAll();

        // then => 데이터 검증
        assertThat(posts).hasSize(3);
        assertThat(posts.get(0).getTitle()).isEqualTo("Title1");
        assertThat(posts.get(1).getTitle()).isEqualTo("Title2");
        assertThat(posts.get(2).getTitle()).isEqualTo("Title3");

    }
}
