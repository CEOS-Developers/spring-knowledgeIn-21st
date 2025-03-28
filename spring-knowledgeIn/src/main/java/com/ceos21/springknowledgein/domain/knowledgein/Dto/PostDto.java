package com.ceos21.springknowledgein.domain.knowledgein.Dto;

import com.ceos21.springknowledgein.domain.user.repository.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String title;
    private String content;
    private Member member;

    public static PostDto of(String title, String content, Member member) {
        return new PostDto(title, content, member);
    }

}
