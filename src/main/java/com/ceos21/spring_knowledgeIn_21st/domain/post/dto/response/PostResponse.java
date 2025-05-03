package com.ceos21.spring_knowledgeIn_21st.domain.post.dto.response;

import com.ceos21.spring_knowledgeIn_21st.domain.image.domain.Image;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PostResponse (
    Long id,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long userId,
    List<String> hashtags,
    List<String> imageUrls
){
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getUser().getId(),
                post.getPostHashtags().stream()
                        .map(postHashtag -> postHashtag.getHashtag().getContent())
                        .collect(Collectors.toList()),
                post.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList())
        );
    }
}
