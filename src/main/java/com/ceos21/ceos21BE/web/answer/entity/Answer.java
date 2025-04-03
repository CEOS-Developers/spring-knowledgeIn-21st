package com.ceos21.ceos21BE.web.answer.entity;

import com.ceos21.ceos21BE.domain.BaseEntity;
import com.ceos21.ceos21BE.web.comment.entity.Comment;
import com.ceos21.ceos21BE.web.image.entity.Image;
import com.ceos21.ceos21BE.web.question.entity.Question;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY)
    private List<Image> images;
}
