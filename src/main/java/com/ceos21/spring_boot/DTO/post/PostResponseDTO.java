package com.ceos21.spring_boot.DTO.post;

import com.ceos21.spring_boot.Domain.post.Post;

import java.util.List;

public record PostResponseDTO(
        Long id,
        String title,
        String content,
        String author,
        String createdAt,
        List<String> imageUrls,
        List<String> hashtags
) {
    public static PostResponseDTO from(Post post, List<String> imageUrls, List<String> hashtags) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getCreatedAt().toString(),
                imageUrls,
                hashtags
        );
    }

}