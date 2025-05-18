package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.post.PostCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.PostResponseDTO;
import com.ceos21.knowledgeIn.domain.post.PostType;
import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.post.PostRepository;
import com.ceos21.knowledgeIn.repository.user.UserRepository;
import com.ceos21.knowledgeIn.service.post.PostService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@ActiveProfiles("test")
@Transactional
@SpringBootTest
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;
    User user = null;
    @BeforeEach
    void init() {
        user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();
        userRepository.save(user);
    }
    @Test
    void createPost() {

        //when
        Long questionId = postService.createPost(PostCreateDTO.builder()
                .title("title1")
                .content("content1")
                .postType(PostType.QUESTION)
                .userId(user.getId())
                .build());

        Long answerId = postService.createPost(PostCreateDTO.builder()
                .title("title2")
                .content("content2")
                .postType(PostType.ANSWER)
                .userId(user.getId())
                .parentPostId(questionId)
                .build());

        //then
        assertThat(questionId).isNotNull();
        assertThat(answerId).isNotNull();
    }

    @Test
    void findPost() {
        //given
        Long questionId = postService.createPost(PostCreateDTO.builder()
                .title("title1")
                .content("content1")
                .postType(PostType.QUESTION)
                .userId(user.getId())
                .build());

        Long answerId = postService.createPost(PostCreateDTO.builder()
                .title("title2")
                .content("content2")
                .postType(PostType.ANSWER)
                .userId(user.getId())
                .parentPostId(questionId)
                .build());

        //when
        PostResponseDTO questionDTO = postService.findPost(questionId);
        PostResponseDTO answerDTO = postService.findPost(answerId);

        //then
        assertThat(questionDTO.getContent()).isEqualTo("content1");
        assertThat(answerDTO.getContent()).isEqualTo("content2");
    }

    @Test
    void findAllPosts() {
        //given
        Long questionId = postService.createPost(PostCreateDTO.builder()
                .title("title1")
                .content("content1")
                .postType(PostType.QUESTION)
                .userId(user.getId())
                .build());

        Long answerId = postService.createPost(PostCreateDTO.builder()
                .title("title2")
                .content("content2")
                .postType(PostType.ANSWER)
                .userId(user.getId())
                .parentPostId(questionId)
                .build());

        //when
        List<PostResponseDTO> dtos = postService.findAllPosts();

        //then
        assertEquals(2, dtos.size());
    }

    @Test
    void deletePost() {
        //given
        Long questionId = postService.createPost(PostCreateDTO.builder()
                .title("title1")
                .content("content1")
                .postType(PostType.QUESTION)
                .userId(user.getId())
                .build());
        //when
        postService.deletePost(questionId);
        //em.flush, clear가 필요함(delete쿼리는 나갔어도 rollback이 안됐으므로 영속성컨텍스트에서 관리됨)
        em.flush();
        em.clear();
        List<PostResponseDTO> dtos = postService.findAllPosts();

        //then
        assertEquals(0,dtos.size());
    }
}