package com.ceos21.knowledgeIn.domain.post.dto;


import com.ceos21.knowledgeIn.domain.hashTag.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
public class PostRequestDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionCreateRequestDTO{
        private String title;
        private String content;
        private List<String> hashTags;
        private Boolean isAnonymous;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerCreateRequestDTO{
        private String content;
        private List<String> hashTags;
        private Boolean isAnonymous;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostUpdateRequestDTO{
        private String title;
        private String content;
        private List<Long> remainingHashTagIds;
        private List<String> newHashTags;
        private Boolean isAnonymous;
        private List<String> remainingImageList;
    }
}
