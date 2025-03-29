package com.ceos21.ceos21BE.web.question.dto.response;

import com.ceos21.ceos21BE.web.hashtag.entity.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllQuestionResponse {

    private Long userId;
    private String username;
    private String title;
    private String content;
    private List<String> hashtags;
}
