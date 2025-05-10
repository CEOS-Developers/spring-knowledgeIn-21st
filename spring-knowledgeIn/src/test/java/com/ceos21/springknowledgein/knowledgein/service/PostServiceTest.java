package com.ceos21.springknowledgein.knowledgein.service;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

/*
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void createPost() {
        // given

        Member member = new Member("username", "password");
        PostCreateDto dto = PostCreateDto.of("테스트 제목", "테스트 내용", member );
        Post post = new Post("테스트 제목", "테스트 내용", member);

        when(postRepository.save(any(Post.class))).thenReturn(post);

        // when
        Post result = postService.createPost(dto);

        // then
        assertThat(result.getTitle()).isEqualTo("테스트 제목");
        assertThat(result.getContent()).isEqualTo("테스트 내용");
        assertThat(result.getMember()).isEqualTo(member);
    }
}
 */
