package com.ceos21.spring_boot.post;

import com.ceos21.spring_boot.domain.entity.Image;
import com.ceos21.spring_boot.domain.entity.Post;
import com.ceos21.spring_boot.domain.entity.User;
import com.ceos21.spring_boot.repository.ImageRepository;
import com.ceos21.spring_boot.repository.PostRepository;
import com.ceos21.spring_boot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

    @DataJpaTest
    @Transactional(propagation = Propagation.NOT_SUPPORTED) //DB 확인용
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public class PostRepositoryTest {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PostRepository postRepository;

        @Autowired
        private ImageRepository imageRepository;

        private User user;

        @BeforeEach
        public void setUp() {
            // 테스트에 사용할 사용자 데이터 생성
            user = User.builder()
                    .nickname("dohyun")
                    .email("dohyun@naver.com")
                    .password("1234")
                    .build();

            userRepository.save(user);
        }

        @Test
        public void testFindByWriter() {
            // given

            //첫번째 질문글 (사진 X)
            Post post1 = Post.builder()
                    .title("Post 1")
                    .content("hello")
                    .writer(user)
                    .build();
            postRepository.save(post1);

            Image image = Image.builder()
                    .imageUrl("image.jpg") // 이미지 URL 설정
                    .post(null)  // 아직 Post와 연결되지 않음
                    .build();

            //2번째 질문글 (사진 1장)
            Post post2 = Post.builder()
                    .title("Post 2")
                    .content("one picture")
                    .images(Collections.singletonList(image))
                    .writer(user)
                    .build();
            image.setPost(post2);
            postRepository.save(post2);


            //3번쨰 질문글 (사진 2장)
            Post post3 = Post.builder()
                    .title("Post 3")
                    .content("two pictures")
                    .images(Arrays.asList())
                    .writer(user)
                    .build();
            postRepository.save(post3);

            Image image1 = Image.builder()
                    .imageUrl("image_url_1")
                    .post(post3)
                    .build();

            Image image2 = Image.builder()
                    .imageUrl("image_url_2")
                    .post(post3)
                    .build();

            imageRepository.save(image1);
            imageRepository.save(image2);

            // when
            List<Post> posts = postRepository.findByPostWriter(user);

            // then
            assertThat(posts).hasSize(3);
            assertThat(posts).extracting(Post::getTitle).containsExactly("Post 1", "Post 2", "Post 3");
        }
    }
