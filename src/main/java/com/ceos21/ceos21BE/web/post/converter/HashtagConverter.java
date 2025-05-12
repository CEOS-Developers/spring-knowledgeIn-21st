package com.ceos21.ceos21BE.web.post.converter;

import com.ceos21.ceos21BE.web.post.dto.response.HashtagPostResponseDTO;
import com.ceos21.ceos21BE.web.post.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class HashtagConverter {

    public HashtagPostResponseDTO toPostDto(Post post) {
        return HashtagPostResponseDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
