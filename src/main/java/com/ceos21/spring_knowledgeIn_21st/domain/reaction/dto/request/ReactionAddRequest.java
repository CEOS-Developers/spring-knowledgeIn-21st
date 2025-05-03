package com.ceos21.spring_knowledgeIn_21st.domain.reaction.dto.request;

import com.ceos21.spring_knowledgeIn_21st.domain.answer.domain.Answer;
import com.ceos21.spring_knowledgeIn_21st.domain.reaction.domain.Reaction;
import com.ceos21.spring_knowledgeIn_21st.domain.reaction.domain.ReactionType;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import jakarta.validation.constraints.NotNull;

public record ReactionAddRequest(
        @NotNull
        ReactionType type
) {
    public Reaction toEntity(User user, Answer answer) {
        return Reaction.builder()
                .user(user)
                .answer(answer)
                .type(type)
                .build();
    }
}
