package com.ceos21.ceos21BE.web.reaction.dto.request;

import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReactionRequest {
    // TargetType은 Answer만 가능
    private ReactionType reactionType; // 'like' or 'dislike' or 'none'
    private Long userId; // 유저 id
    private Long postId; // 게시글 id
}
