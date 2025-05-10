package com.ceos21.ceos21BE.web.post.dto.response;

import lombok.Builder;

@Builder
public class AnswerSummaryResponseDto {
    // 질문에 포함되는 답변
    private Long postId;
    private String content;
    private String username;
    private int likeCount;
    private int dislikeCount;
}
