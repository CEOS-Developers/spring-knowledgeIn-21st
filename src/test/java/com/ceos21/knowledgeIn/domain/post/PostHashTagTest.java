package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.post.HashTagRepository;
import com.ceos21.knowledgeIn.repository.post.PostRepository;
import com.ceos21.knowledgeIn.repository.user.UserRepository;
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
class PostHashTagTest {
    @Autowired
    PostRepository postRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    Post post = null;

    User user = null;

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
    void createPostHashTag() {
        //given
        String content = "hallo content";
        HashTag hashTag = HashTag.builder()
                .content(content)
                .build();
        hashTagRepository.save(hashTag);

        PostHashTag postHashTag = PostHashTag.create(hashTag, post);


        //when
        em.flush();

        Post findPost = postRepository.findById(post.getId()).get();
        PostHashTag findPostHashTag = findPost.getHashTags().get(0);

        //then
        Assertions.assertThat(findPostHashTag).isEqualTo(postHashTag);
    }

    @Test
    void deletePostHashTag() {
        //given
        String content = "hallo content";
        HashTag hashTag = HashTag.builder()
                .content(content)
                .build();
        hashTagRepository.save(hashTag);




        //when
        PostHashTag postHashTag = PostHashTag.create(hashTag, post);
        em.flush();

        Post findPost = postRepository.findById(post.getId()).get();
        List<PostHashTag> hashTags = findPost.getHashTags();
        hashTags.remove(postHashTag);
        em.flush();

        //then
        Post findPost2 = postRepository.findById(post.getId()).get();
        List<PostHashTag> hashTags2 = findPost.getHashTags();
        Assertions.assertThat(hashTags2.size()).isEqualTo(0);
    }
}