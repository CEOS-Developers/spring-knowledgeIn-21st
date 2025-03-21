package com.knowledgein.springboot.repositoryTest;

import com.knowledgein.springboot.SpringbootApplication;
import com.knowledgein.springboot.domain.Post;
import com.knowledgein.springboot.domain.User;
import com.knowledgein.springboot.repository.PostRepository;
import com.knowledgein.springboot.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
//    @Transactional
//    @Rollback(false)
    void storePostTest() {
        // Post 저장 테스트

        // given - 유저 생성
        User user = User.builder()
                .name("tris")
                .email("tris@gmail.com")
                .nickname("trisss")
                .build();

        userRepository.save(user);

        // when - 포스트 생성
        Post post1 = Post.builder()
                .user(user)
                .title("1st post")
                .content("1st post content")
                .build();

        Post post2 = Post.builder()
                .user(user)
                .title("2nd post")
                .content("2nd post content")
                .build();

        Post post3 = Post.builder()
                .user(user)
                .title("3rd post")
                .content("3rd post content")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // then - 저장된 게시물 조회
        List<Post> postList = new ArrayList<>();
        postList = postRepository.findAll();

        Assertions.assertEquals(3, postList.size());

//        postList.forEach(post -> {
//            System.out.println("Post: " + post.getTitle());
//            System.out.println("Writer: " + post.getUser().getNickname());
//        });

    }
}
