package com.ceos21.knowledgein.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "reply")
public class Reply extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ColumnDefault("false")
    private boolean accepted;
    @Column(length = 2048)
    private String content;

    // 대댓글
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = LAZY)
    private Reply parent;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private UserEntity user;

    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private Post post;


}
