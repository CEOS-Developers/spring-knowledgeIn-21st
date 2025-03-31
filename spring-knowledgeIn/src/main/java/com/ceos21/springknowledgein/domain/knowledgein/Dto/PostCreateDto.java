package com.ceos21.springknowledgein.domain.knowledgein.Dto;

import com.ceos21.springknowledgein.domain.knowledgein.repository.Hashtag;
import com.ceos21.springknowledgein.domain.knowledgein.repository.Image;
import com.ceos21.springknowledgein.domain.user.repository.Member;
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
    private Member member;
    private List<Image> images;
    private List<Hashtag> hashtags;

    public static PostCreateDto of(String title, String content, Member member,List<Image> images, List<Hashtag> hashtags) {
        return new PostCreateDto(title, content, member, images, hashtags);
    }
}
