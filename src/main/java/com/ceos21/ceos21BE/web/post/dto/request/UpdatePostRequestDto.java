package com.ceos21.ceos21BE.web.post.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdatePostRequestDto {

    // type은 변경 불가능, parentpostI는 수정 시점에 변경 불가
    // 질문일 경우만 수정 가능
    private String title;

    private String content;

    private List<String> hashtags;

    private List<String> imageUrls;
}
