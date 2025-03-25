package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.PostRepository;
import com.ceos21.knowledgeIn.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@Transactional
class PostThumbTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;
    @BeforeEach
    void init() {

    }

    @Test
    void createPostThumb() {
        //given
        Post post = Post.createQuestion("title", "content");
        postRepository.save(post);

        User user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();
        userRepository.save(user);

        PostThumb postThumb = PostThumb.create(post, user);

        //when
        post.addPostThumb(postThumb);
        em.flush();
        em.clear();

        //then
        Post findPost = postRepository.findById(post.getId()).get();
        List<PostThumb> postThumbs = findPost.getPostThumbs();
        Assertions.assertThat(postThumbs.size()).isEqualTo(1);
    }
    @Test
    void deletePostThumb() {
        //given
        Post post = Post.createQuestion("title", "content");
        postRepository.save(post);

        User user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();
        userRepository.save(user);

        PostThumb postThumb = PostThumb.create(post, user);

        //when
        post.addPostThumb(postThumb);
        List<PostThumb> postThumbs = post.getPostThumbs();
        postThumbs.remove(0);
        em.flush();
        em.clear();

        //then
        Post findPost = postRepository.findById(post.getId()).get();
        int size = findPost.getPostThumbs().size();

        Assertions.assertThat(size).isEqualTo(0);

    }
}