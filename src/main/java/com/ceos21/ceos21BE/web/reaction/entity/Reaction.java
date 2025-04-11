package com.ceos21.ceos21BE.web.reaction.entity;

import com.ceos21.ceos21BE.domain.BaseEntity;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
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

    //private TargetType targetType;
    // 오직 answer 에 대해서 만 좋아요 가능

    @Setter
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType = ReactionType.NONE; // 'like' or 'dislike'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // 단, 저장/조회 시 post.postType == ANSWER 인 경우에만 reaction 허용
    @ManyToOne
    private Post post;

}
