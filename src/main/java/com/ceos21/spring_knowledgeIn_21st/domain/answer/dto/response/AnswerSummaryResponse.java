package com.ceos21.spring_knowledgeIn_21st.domain.answer.dto.response;

import com.ceos21.spring_knowledgeIn_21st.domain.answer.domain.Answer;

import java.time.LocalDateTime;

public record AnswerSummaryResponse(
        Long answerId,
        Long userId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
    public static AnswerSummaryResponse from(Answer answer) {
        return new AnswerSummaryResponse(
                answer.getId(),
                answer.getUser().getId(),
                answer.getContent(),
                answer.getCreatedAt(),
                answer.getUpdatedAt()
        );
    }
}
