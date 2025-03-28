package com.ceos21.springknowledgein.domain.knowledgein.service;

import com.ceos21.springknowledgein.domain.knowledgein.Dto.PostDto;
import com.ceos21.springknowledgein.domain.knowledgein.repository.Post;
import com.ceos21.springknowledgein.domain.knowledgein.repository.PostRepository;
import com.ceos21.springknowledgein.domain.user.repository.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;


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
        PostDto dto = PostDto.of("테스트 제목", "테스트 내용", member );
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
