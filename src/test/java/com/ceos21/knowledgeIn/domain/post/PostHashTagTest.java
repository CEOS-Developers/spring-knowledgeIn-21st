package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.repository.HashTagRepository;
import com.ceos21.knowledgeIn.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostHashTagTest {
    @Autowired
    PostRepository postRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    @Autowired
    EntityManager em;

    @Test
    void createPostHashTag() {
        //given
        Post post = Post.createQuestion("title", "content");
        postRepository.save(post);

        String content = "hallo content";
        HashTag hashTag = HashTag.builder()
                .content(content)
                .build();
        hashTagRepository.save(hashTag);

        PostHashTag postHashTag = PostHashTag.create(hashTag, post);


        //when
        post.addPostHashTag(postHashTag);
        em.flush();

        Post findPost = postRepository.findById(post.getId()).get();
        PostHashTag findPostHashTag = findPost.getHashTags().get(0);

        //then
        Assertions.assertThat(findPostHashTag).isEqualTo(postHashTag);
    }

    @Test
    void deletePostHashTag() {
        //given
        Post post = Post.createQuestion("title", "content");
        postRepository.save(post);

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