package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.repository.PostRepository;
import org.assertj.core.api.Assertions;
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

    @Test
    void createPost() {
        //given
        Post post = Post.createQuestion("title", "content");

        Long id = postRepository.save(post).getId();

        //when
        Optional<Post> findPost = postRepository.findById(id);
        Assertions.assertThat(findPost).isPresent();
    }
    @Test
    void deletePost() {
        //given
        Post post = Post.createQuestion("title", "content");

        Long id = postRepository.save(post).getId();
        postRepository.deleteById(id);

        //when
        Optional<Post> findPost = postRepository.findById(id);
        Assertions.assertThat(findPost).isEmpty();
    }


}