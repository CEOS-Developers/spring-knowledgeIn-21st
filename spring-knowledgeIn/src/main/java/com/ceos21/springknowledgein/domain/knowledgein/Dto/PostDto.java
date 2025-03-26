package com.ceos21.springknowledgein.domain.knowledgein.Dto;

import com.ceos21.springknowledgein.domain.user.repository.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String title;
    private String content;
    private Member member;
}
