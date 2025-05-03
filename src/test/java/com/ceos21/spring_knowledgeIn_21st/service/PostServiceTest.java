package com.ceos21.spring_knowledgeIn_21st.service;

import com.ceos21.spring_knowledgeIn_21st.domain.post.dao.PostRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import com.ceos21.spring_knowledgeIn_21st.domain.user.dao.UserRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;

@SpringBootTest
@Rollback(false)
@Transactional
public class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Post post;

    @Test
    void storePostTest(){
        // given
        User user = User.builder()
                .name("yeonee")
                .email("amlily9011@gmail.com")
                .password("1234")
                .build();

        userRepository.save(user);

        // when
        Post post1 = Post.builder()
                .user(user)
                .title("1st post")
                .content("첫번째 포스트인가요?")
                .build();

        Post post2 = Post.builder()
                .user(user)
                .title("2nd post")
                .content("두번째 포스트인가요?")
                .build();

        Post post3 = Post.builder()
                .user(user)
                .title("3rd post")
                .content("세번째 포스트인가요?")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // 콘솔에 출력
        System.out.println("Saved Posts:");
        postRepository.findAll().forEach(post ->
                System.out.println(post.getTitle() + " - " + post.getContent())
        );

        // then
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(3);

    }

}
