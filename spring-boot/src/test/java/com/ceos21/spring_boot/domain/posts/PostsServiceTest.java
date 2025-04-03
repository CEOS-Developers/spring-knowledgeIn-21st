package com.ceos21.spring_boot.domain.posts;

import com.ceos21.spring_boot.domain.users.Users;
import com.ceos21.spring_boot.domain.users.UsersService;
import com.ceos21.spring_boot.domain.posts.dto.PostsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostsServiceTest {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PostsService postsService;

    private Users mockUser;

    @BeforeEach
    void setUp() {
        mockUser = Users.builder()
                .id(1L)
                .name("홍길동")
                .email("test@test.com")
                .build();
    }

    @Test
    void testCreatePost() {
        PostsRequest request = PostsRequest.builder()
                .userId(1L)
                .texts("테스트 내용")
                .tags("태그")
                .pictures("pic.jpg")
                .comment("댓글")
                .build();


        Users user = usersService.getUserById(1L);


        Posts postEntity = postsRepository.save(request.toEntity(user));


        Posts response = postsService.createPost(postEntity);
        assertEquals("테스트 내용", response.getTexts());
    }

    @Test
    void testGetPostById_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            postsService.getPostById(999L); // 존재하지 않는 id
        });
    }
}
