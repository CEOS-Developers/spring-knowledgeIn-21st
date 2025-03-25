package com.ceos21.spring_boot.domain.entity;

import com.ceos21.spring_boot.domain.enums.LikeStatus;
import com.ceos21.spring_boot.domain.enums.TargetStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeDislike {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
            @Column(name="like_dislike_id")
            private Long id;

        @Enumerated(EnumType.STRING)
        private LikeStatus likestatus;

            @Enumerated(EnumType.STRING)
            private TargetStatus targetstatus;

            @ManyToOne(fetch= FetchType.LAZY)
            @JoinColumn(name="comment_id")
            private Comment comment;

            @ManyToOne(fetch= FetchType.LAZY)
            @JoinColumn(name="user_id")
            private User user;

            @ManyToOne(fetch= FetchType.LAZY)
            @JoinColumn(name="post_id")
            private Post post;

            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "answer_id", nullable = true)
            private Answer answer;
}
