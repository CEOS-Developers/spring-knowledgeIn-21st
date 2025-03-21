package com.knowledgein.springboot.domain;

import com.knowledgein.springboot.domain.common.BaseEntity;
import com.knowledgein.springboot.domain.enums.ContentType;
import com.knowledgein.springboot.domain.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder
@DynamicInsert // Enum의 Default value 제대로 처리하기 위함
@DynamicUpdate // Enum의 Default value 제대로 처리하기 위함
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'NONE'")
    private ContentType contentType;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'NONE'")
    private ReactionType reactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
