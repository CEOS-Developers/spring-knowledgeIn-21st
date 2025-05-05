package com.ceos21.ceos21BE.web.reaction.converter;

import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.reaction.dto.request.ReactionRequest;
import com.ceos21.ceos21BE.web.reaction.dto.response.ReactionResponse;
import com.ceos21.ceos21BE.web.reaction.entity.Reaction;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ReactionConverter {
    public Reaction toEntity(ReactionType reactionType, UserEntity user, Post post) {
        return Reaction.builder()
                .user(user)
                .post(post)
                .reactionType(reactionType)
                .build();
    }

    public ReactionResponse toResponse(Reaction reaction) {
        return ReactionResponse.builder()
                .userId(reaction.getUser().getUserId())
                .postId(reaction.getPost().getPostId())
                .reactionType(reaction.getReactionType())
                .build();
    }
}
