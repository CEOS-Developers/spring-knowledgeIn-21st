package com.ceos21.spring_boot.DTO.post;

import com.ceos21.spring_boot.DTO.answer.AnswerDTO;
import com.ceos21.spring_boot.Domain.post.Post;

import java.util.List;

public record PostResponseWithAnswerDTO(
        Long id,
        String title,
        String content,
        String author,
        String createdAt,
        List<String> imageUrls,
        List<String> hashtags,
        List<AnswerDTO> answers
) {
    public static PostResponseWithAnswerDTO from(Post post, List<String> imageUrls, List<String> hashtags, List<AnswerDTO> answers) {
        return new PostResponseWithAnswerDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getCreatedAt().toString(),
                imageUrls,
                hashtags,
                answers
        );
    }
}
