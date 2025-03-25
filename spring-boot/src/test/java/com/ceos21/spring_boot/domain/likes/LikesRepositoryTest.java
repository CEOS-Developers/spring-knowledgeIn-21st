package com.ceos21.spring_boot.domain.likes;

import com.ceos21.spring_boot.domain.posts.Posts;
import com.ceos21.spring_boot.domain.posts.PostsRepository;
import com.ceos21.spring_boot.domain.users.Users;
import com.ceos21.spring_boot.domain.users.UsersRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LikesRepositoryTest {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Test
    @Transactional
    public void likesGetTest() {
        // given
        Users user = usersRepository.save(Users.builder()
                .name("tester")
                .email("test@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .provider("local")
                .hashPassword("hashed")
                .salt("salt")
                .build());

        Posts post = postsRepository.save(Posts.builder()
                .user(user)
                .texts("postsContent")
                .tags("test")
                .comment("testComment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        Likes like = likesRepository.save(Likes.builder()
                .user(user)
                .post(post)
                .typeOfLike(TypeOfLikes.LIKE)
                .createdAt(LocalDateTime.now())
                .build());

        // when
        Likes result = likesRepository.findById(like.getId()).orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUser().getEmail()).isEqualTo("test@example.com");
        assertThat(result.getPost().getTexts()).isEqualTo("postsContent");
        assertThat(result.getTypeOfLike()).isEqualTo(TypeOfLikes.LIKE);
    }
}
