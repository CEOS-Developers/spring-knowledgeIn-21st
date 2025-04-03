package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.post.CommentCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.CommentResponseDTO;
import com.ceos21.knowledgeIn.domain.post.Comment;
import com.ceos21.knowledgeIn.domain.post.Post;
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
class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    User user = null;
    Post post = null;
    @BeforeEach
    void init() {
        user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();
        userRepository.save(user);

        post = Post.createQuestion("title", "content", user);
        postRepository.save(post);
    }

    @Test
    void createComment()
    {
        //when
        Long commentId = commentService.createComment(CommentCreateDTO.builder()
                .content("content")
                .postId(post.getId())
                .userId(user.getId())
                .build());

        //then
        assertThat(commentId).isNotNull();
    }

    @Test
    void findComment() {
        //given
        String content = "content";

        Long commentId = commentService.createComment(CommentCreateDTO.builder()
                .content(content)
                .postId(post.getId())
                .userId(user.getId())
                .build());
        //when
        CommentResponseDTO dto = commentService.findComment(commentId);

        //then
        assertEquals(dto.getContent(),content);
    }

    @Test
    void findAllComments() {
        //given
        String content1 = "content1";

        Long commentId1 = commentService.createComment(CommentCreateDTO.builder()
                .content(content1)
                .postId(post.getId())
                .userId(user.getId())
                .build());

        String content2 = "content2";
        Long commentId2 = commentService.createComment(CommentCreateDTO.builder()
                .content(content2)
                .postId(post.getId())
                .userId(user.getId())
                .build());

        //when
        List<CommentResponseDTO> dtos = commentService.findAllComments();

        //then
        assertEquals(dtos.size(), 2);

    }

    @Test
    void deleteComment() {
        //given
        String content1 = "content1";

        Long commentId1 = commentService.createComment(CommentCreateDTO.builder()
                .content(content1)
                .postId(post.getId())
                .userId(user.getId())
                .build());

        String content2 = "content2";
        Long commentId2 = commentService.createComment(CommentCreateDTO.builder()
                .content(content2)
                .postId(post.getId())
                .userId(user.getId())
                .build());

        //when
        commentService.deleteComment(commentId1);

        //then
        List<CommentResponseDTO> dtos = commentService.findAllComments();

        //then
        assertEquals(dtos.size(), 1);
    }
}