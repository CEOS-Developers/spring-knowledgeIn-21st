package com.ceos21.spring_knowledgeIn_21st.domain.answer.dto.response;

import com.ceos21.spring_knowledgeIn_21st.domain.answer.domain.Answer;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import com.ceos21.spring_knowledgeIn_21st.domain.user.dto.reponse.UserSummaryResponse;

import java.time.LocalDateTime;

public record AnswerDetailResponse (
        Long answerId,
        Long postId,
        UserSummaryResponse user,
        String content,
        Integer likeCount,
        Integer dislikeCount,
        Integer commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
    public static AnswerDetailResponse from(Answer answer) {
        return new AnswerDetailResponse(
                answer.getId(),
                answer.getPost().getId(),
                new UserSummaryResponse(
                        answer.getUser().getId(),
                        answer.getUser().getName(),
                        answer.getUser().getEmail()
                ),
                answer.getContent(),
                answer.getLikeCount(),
                answer.getDislikeCount(),
                answer.getCommentCount(),
                answer.getCreatedAt(),
                answer.getUpdatedAt()
        );
    }
}
