package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.PostRepository;
import com.ceos21.knowledgeIn.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
//@DataJpaTest
@SpringBootTest
class PostTest {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    User user = null;

    @BeforeEach
    public void init() {
        user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();
        userRepository.save(user);
    }

    @Test
    void createPost() {
        //given
        Post post = Post.createQuestion("title", "content",user);

        Long id = postRepository.save(post).getId();

        //when
        Optional<Post> findPost = postRepository.findById(id);
        Assertions.assertThat(findPost).isPresent();
    }
    @Test
    void deletePost() {
        //given
        Post post = Post.createQuestion("title", "content",user);

        Long id = postRepository.save(post).getId();
        postRepository.deleteById(id);

        //when
        Optional<Post> findPost = postRepository.findById(id);
        Assertions.assertThat(findPost).isEmpty();
    }


}