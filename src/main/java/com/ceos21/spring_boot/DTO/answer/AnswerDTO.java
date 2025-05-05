package com.ceos21.spring_boot.DTO.answer;

import com.ceos21.spring_boot.Domain.post.Answer;
import com.ceos21.spring_boot.Domain.post.Image;

import java.util.List;

public record AnswerDTO(
        Long id,
        String content,
        String author,
        String createdAt,
        List<String> imageUrls
) {
    public static AnswerDTO from(Answer answer) {
        return new AnswerDTO(
                answer.getId(),
                answer.getContent(),
                answer.getUser().getUsername(),
                answer.getCreatedAt().toString(),
                answer.getImages().stream()
                        .map(Image::getPostImageUrl)
                        .toList()
        );
    }
}

