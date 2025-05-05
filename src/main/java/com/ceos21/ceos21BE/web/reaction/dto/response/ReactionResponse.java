package com.ceos21.ceos21BE.web.reaction.dto.response;

import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReactionResponse {
    private Long userId;
    private Long postId; // 게시글 id
    private ReactionType reactionType; // 'like' or 'dislike' or 'none'
}
