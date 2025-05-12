package com.ceos21.ceos21BE.web.reaction.converter;

import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.reaction.dto.response.ReactionResponseDTO;
import com.ceos21.ceos21BE.web.reaction.entity.Reaction;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReactionConverter {
    public Reaction toEntity(ReactionType reactionType, User user, Post post) {
        return Reaction.builder()
                .user(user)
                .post(post)
                .reactionType(reactionType)
                .build();
    }

    public ReactionResponseDTO toResponse(Reaction reaction) {
        return ReactionResponseDTO.builder()
                .userId(reaction.getUser().getUserId())
                .postId(reaction.getPost().getPostId())
                .reactionType(reaction.getReactionType())
                .build();
    }
}
