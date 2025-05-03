package com.ceos21.spring_knowledgeIn_21st.domain.comment.dto.request;

import com.ceos21.spring_knowledgeIn_21st.domain.answer.domain.Answer;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.AnswerComment;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.PostComment;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import jakarta.validation.constraints.NotBlank;

public record CommentAddRequest(
        @NotBlank
        String content
){
    public PostComment toPostCommentEntity(Post post, User user) {
        return PostComment.builder()
                .content(content)
                .post(post)
                .user(user)
                .build();
    }

    public AnswerComment toAnswerCommentEntity(Answer answer, User user) {
        return AnswerComment.builder()
                .content(content)
                .answer(answer)
                .user(user)
                .build();
    }
}