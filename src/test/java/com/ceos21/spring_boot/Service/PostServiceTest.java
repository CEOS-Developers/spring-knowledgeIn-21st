/*package com.ceos21.spring_boot.Service;

import com.ceos21.spring_boot.DTO.post.PostRequestDTO;
import com.ceos21.spring_boot.DTO.post.PostResponseDTO;
import com.ceos21.spring_boot.Domain.user.User;
import com.ceos21.spring_boot.Repository.user.UserRepository;
import com.ceos21.spring_boot.Service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuserid","password", "testuser", "email@example.com", LocalDate.of(2000, 1, 1),"010-0000-0000");
        userRepository.save(user);
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    void testCreatePost() {
        // Given
        PostRequestDTO postCreateDTO = new PostRequestDTO(
                "Test Title",
                "Test Content",
                Arrays.asList("tag1", "tag2"),
                Arrays.asList("url1", "url2")
        );

        // When
        PostResponseDTO result = postService.createPost(postCreateDTO, user);

        // Then
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals("testuser", result.getAuthor());
        assertEquals(2, result.getImageUrls().size());
        assertEquals(2, result.getHashtags().size());
    }

}
 */
