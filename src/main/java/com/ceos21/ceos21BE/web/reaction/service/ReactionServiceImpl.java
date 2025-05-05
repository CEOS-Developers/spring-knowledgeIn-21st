package com.ceos21.ceos21BE.web.reaction.service;

import com.ceos21.ceos21BE.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.entity.PostType;
import com.ceos21.ceos21BE.web.post.repository.PostRepository;
import com.ceos21.ceos21BE.web.reaction.converter.ReactionConverter;
import com.ceos21.ceos21BE.web.reaction.dto.request.ReactionRequest;
import com.ceos21.ceos21BE.web.reaction.dto.response.ReactionResponse;
import com.ceos21.ceos21BE.web.reaction.entity.Reaction;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.reaction.handler.ReactionHandler;
import com.ceos21.ceos21BE.web.reaction.repository.ReactionRepository;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReactionConverter reactionConverter;


    @Override
    public ReactionResponse reactToPost(ReactionRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ReactionHandler(ErrorStatus._USER_NOT_FOUND));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ReactionHandler(ErrorStatus._POST_NOT_FOUND));

        if(post.getPostType() == PostType.QUESTION) {
            throw new ReactionHandler(ErrorStatus._REACTION_NOT_VALID);
        }

        Reaction reaction = reactionRepository.findByUserAndPost(user, post)
                .orElseGet(() -> createNewReaction(user, post));

        updateReaction(reaction, request.getReactionType());
        reactionRepository.save(reaction);

        return reactionConverter.toResponse(reaction);

    }

    private Reaction createNewReaction(UserEntity user, Post post) {
        return reactionConverter.toEntity(ReactionType.NONE, user, post);
    }

    private void updateReaction(Reaction reaction, ReactionType newReactionType) {
        boolean isSameReaction = reaction.getReactionType() == newReactionType;
        if (isSameReaction) {
            reaction.setReactionType(ReactionType.NONE);
        } else {
            reaction.setReactionType(newReactionType);
        }
    }
}
