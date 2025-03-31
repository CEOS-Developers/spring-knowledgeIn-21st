package com.ceos21.springknowledgein.domain.knowledgein.Dto;

import com.ceos21.springknowledgein.domain.knowledgein.repository.Hashtag;
import com.ceos21.springknowledgein.domain.knowledgein.repository.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateDto {
    private Long id;
    private String title;
    private String content;
    private List<Image> images;
    private List<Hashtag> hashtags;

    public static PostUpdateDto of(Long id, String title, String content, List<Image> images, List<Hashtag> hashtags) {
        return new PostUpdateDto(id, title, content,images, hashtags);
    }

}
