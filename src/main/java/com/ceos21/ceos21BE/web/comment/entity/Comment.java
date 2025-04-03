package com.ceos21.ceos21BE.web.comment.entity;

import com.ceos21.ceos21BE.domain.BaseEntity;
import com.ceos21.ceos21BE.web.question.entity.Question;
import com.ceos21.ceos21BE.web.answer.entity.Answer;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import jakarta.persistence.*;

@Entity
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;
}
