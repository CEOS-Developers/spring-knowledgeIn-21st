package com.ceos21.spring_knowledgeIn_21st.domain.comment.dto.response;

import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.AnswerComment;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.BaseComment;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.PostComment;
import com.ceos21.spring_knowledgeIn_21st.domain.user.dto.reponse.UserSummaryResponse;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserSummaryResponse user
) {
    public static CommentResponse from(PostComment postComment) {
        return new CommentResponse(
                postComment.getId(),
                postComment.getContent(),
                postComment.getCreatedAt(),
                postComment.getUpdatedAt(),
                new UserSummaryResponse(
                        postComment.getUser().getId(),
                        postComment.getUser().getName(),
                        postComment.getUser().getEmail()
                )
        );
    }

    public static CommentResponse from(AnswerComment answerComment) {
        return new CommentResponse(
                answerComment.getId(),
                answerComment.getContent(),
                answerComment.getCreatedAt(),
                answerComment.getUpdatedAt(),
                new UserSummaryResponse(
                        answerComment.getUser().getId(),
                        answerComment.getUser().getName(),
                        answerComment.getUser().getEmail()
                )
        );
    }
}
