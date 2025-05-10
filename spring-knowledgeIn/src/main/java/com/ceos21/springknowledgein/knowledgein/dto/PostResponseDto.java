package com.ceos21.springknowledgein.knowledgein.dto;

import com.ceos21.springknowledgein.knowledgein.domain.Hashtag;
import com.ceos21.springknowledgein.knowledgein.domain.Image;
import com.ceos21.springknowledgein.user.repository.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private List<Image> images;
    private List<Hashtag> hashtags;
    private Member member;

    public static PostResponseDto of(Long id, String title, String content, List<Image> images, List<Hashtag> hashtags, Member member){
        return new PostResponseDto(id, title, content, images, hashtags, member);
    }
}
