package com.ceos21.ceos21BE.web.question.entity;


import com.ceos21.ceos21BE.domain.BaseEntity;
import com.ceos21.ceos21BE.web.questionhashtag.entity.QuestionHashtag;
import com.ceos21.ceos21BE.web.answer.entity.Answer;
import com.ceos21.ceos21BE.web.comment.entity.Comment;
import com.ceos21.ceos21BE.web.image.entity.Image;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Image> images;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QuestionHashtag> questionHashtags = new ArrayList<>();

    public static Question create(String title, String content, UserEntity user) {
        return Question.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }

    public void updateQuestion(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
