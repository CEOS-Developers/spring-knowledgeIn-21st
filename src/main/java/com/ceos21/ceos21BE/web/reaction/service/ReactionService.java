package com.ceos21.ceos21BE.web.reaction.service;

import com.ceos21.ceos21BE.global.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.entity.PostType;
import com.ceos21.ceos21BE.web.post.repository.PostRepository;
import com.ceos21.ceos21BE.web.post.service.questionservice.PostService;
import com.ceos21.ceos21BE.web.reaction.converter.ReactionConverter;
import com.ceos21.ceos21BE.web.reaction.dto.request.ReactionRequestDTO;
import com.ceos21.ceos21BE.web.reaction.dto.response.ReactionResponseDTO;
import com.ceos21.ceos21BE.web.reaction.entity.Reaction;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.reaction.handler.ReactionHandler;
import com.ceos21.ceos21BE.web.reaction.repository.ReactionRepository;
import com.ceos21.ceos21BE.web.user.entity.User;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import com.ceos21.ceos21BE.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final UserService userService;
    private final PostService postService;
    private final ReactionConverter reactionConverter;

    // 기존 리액션 존재 시 수정(업데이트)
    // 없으면 새로 등록 로직
    @Transactional
    public ReactionResponseDTO handleReaction(Long postId, Long userId, ReactionType type) {
        User user = userService.validateUser(userId);

        Post post = postService.validatePost(postId);

        // 질문글에 리액션을 등록할 수 없음
        if(post.getPostType() != PostType.ANSWER) {
            throw new ReactionHandler(ErrorStatus._REACTION_NOT_VALID);
        }



        Optional<Reaction> OptionalReaction = reactionRepository.findByUserAndPost(user, post);

        Reaction reaction;
        if(OptionalReaction.isPresent()) {

            reaction = OptionalReaction.get();

            if(reaction.getReactionType() != type) {
                // 리액션 타입이 다를 경우 업데이트
                reaction.updateReactionType(type);

                return reactionConverter.toResponse(reaction);
            }
            // 리액션 타입이 같을 경우 삭제
            else {
                reactionRepository.delete(reaction);
                return null;
            }

        } else {
            // 기존 리액션이 존재하지 않는 경우
            Reaction newReaction = createNewReaction(type, user, post);
            reactionRepository.save(newReaction);

            return reactionConverter.toResponse(newReaction);
        }

    }

    private Reaction createNewReaction(ReactionType type, User user, Post post) {
        return reactionConverter.toEntity(type, user, post);
    }

}
