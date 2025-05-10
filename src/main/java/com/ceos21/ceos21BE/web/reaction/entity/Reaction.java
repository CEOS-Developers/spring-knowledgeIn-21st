package com.ceos21.ceos21BE.web.reaction.entity;

import com.ceos21.ceos21BE.global.BaseEntity;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.user.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType; // 'like' or 'dislike'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 단, 저장/조회 시 post.postType == ANSWER 인 경우에만 reaction 허용
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public void updateReactionType(ReactionType newType) {
        this.reactionType = newType;
    }

}
