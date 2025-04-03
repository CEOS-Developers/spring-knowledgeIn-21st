package com.ceos21.ceos21BE.web.reaction.entity;

import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.TargetType;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    private TargetType targetType; // 'question' or 'answer'
    private Long targetId;
    private ReactionType reactionType; // 'like' or 'dislike'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
