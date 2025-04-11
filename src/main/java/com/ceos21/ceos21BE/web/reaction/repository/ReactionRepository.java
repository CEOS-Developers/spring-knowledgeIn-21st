package com.ceos21.ceos21BE.web.reaction.repository;

import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.reaction.entity.Reaction;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    boolean existsByUserAndPost(UserEntity user, Post post);
    void deleteByReactionTypeAndCreatedAtBefore(ReactionType reactionType, LocalDateTime cutOff);
    Optional<Reaction> findByUserAndPost(UserEntity user, Post post);
}
