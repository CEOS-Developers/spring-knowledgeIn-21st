package com.ceos21.ceos21BE.web.post.entity;


import com.ceos21.ceos21BE.global.BaseEntity;
import com.ceos21.ceos21BE.web.comment.entity.Comment;
import com.ceos21.ceos21BE.web.reaction.entity.Reaction;
import com.ceos21.ceos21BE.web.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    // 제목은 오직 QUESTION type 일 경우만 가능하다.
    @Column(length = 50, nullable = false)
    private String title;

    // 대량의 텍스트 값을 저장할 때 사용하는 애노테이션
    // 스프링이 추론하여 어떤 타입으로 저장할 지 자동으로 판단한다.
    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private PostType postType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 답변글이면 질문글을 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Post parent;

    @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> answers = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Reaction> reactions = new ArrayList<>();

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
