package com.ceos21.ceos21BE.web.question.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class QuestionRequest {

    private Long userId;
    private String title;
    private String content;
    private List<String> hashtags;
}
