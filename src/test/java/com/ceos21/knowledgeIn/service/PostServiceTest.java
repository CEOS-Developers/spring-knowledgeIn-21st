package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.post.PostCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.PostResponseDTO;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.post.PostType;
import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.PostRepository;
import com.ceos21.knowledgeIn.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
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
        assertEquals(dtos.size(), 2);
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
        List<PostResponseDTO> dtos = postService.findAllPosts();

        //then
        assertEquals(dtos.size(),0);
    }
}