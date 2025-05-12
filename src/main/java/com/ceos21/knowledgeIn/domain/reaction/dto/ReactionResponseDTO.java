package com.ceos21.knowledgeIn.domain.reaction.dto;

import com.ceos21.knowledgeIn.domain.reaction.domain.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReactionResponseDTO {

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class reactionResultDTO {
        private ReactionType reactionType;
    }
}
