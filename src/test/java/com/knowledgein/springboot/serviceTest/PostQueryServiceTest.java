package com.knowledgein.springboot.serviceTest;

import com.knowledgein.springboot.apiPayload.code.status.ErrorStatus;
import com.knowledgein.springboot.apiPayload.exception.GeneralException;
import com.knowledgein.springboot.domain.Post;
import com.knowledgein.springboot.domain.User;
import com.knowledgein.springboot.domain.enums.PostType;
import com.knowledgein.springboot.repository.PostRepository;
import com.knowledgein.springboot.repository.UserRepository;
import com.knowledgein.springboot.service.postService.PostQueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
public class PostQueryServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostQueryService postQueryService;

    @Autowired
    private UserRepository userRepository;

    private Post savedPost;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("chistopher")
                .nickname("chis")
                .email("chris@gmail.com")
                .build();

        userRepository.save(user);

        Post post = Post.builder()
                .title("물마시고 싶어요")
                .content("이거만 하고 물마셔야지")
                .postType(PostType.QUESTION)
                .user(user)
                .build();

        savedPost = postRepository.save(post);
    }

    @Test
    @DisplayName("postId로 게시글 조회 성공")
    void getPostSuccessTest() {
        // given: test user & post

        // when
        Post result = postQueryService.getPost(savedPost.getId());

        // then
        Assertions.assertEquals(savedPost.getId(), result.getId());
    }

    @Test
    @DisplayName("postId로 게시글 조회 실패 (존재하지 않는 id)")
    void getPostFailTest() {
        // given: test user & post
        Long badId = 800L;

        // when & then
        GeneralException exception = Assertions.assertThrows(GeneralException.class, () -> {
            postQueryService.getPost(badId);
        });

        Assertions.assertEquals(ErrorStatus.POST_NOT_FOUND, exception.getCode());
    }
}
