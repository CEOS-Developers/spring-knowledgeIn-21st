package com.ceos21.springknowledgein.knowledgein.dto;

import com.ceos21.springknowledgein.knowledgein.domain.Hashtag;
import com.ceos21.springknowledgein.knowledgein.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {
    private String title;
    private String content;
    private List<Image> images;
    private List<Hashtag> hashtags;

    public static PostCreateDto of(String title, String content,List<Image> images, List<Hashtag> hashtags) {
        return new PostCreateDto(title, content, images, hashtags);
    }
}
