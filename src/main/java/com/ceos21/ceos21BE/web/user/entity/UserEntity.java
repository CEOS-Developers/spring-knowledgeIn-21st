package com.ceos21.ceos21BE.web.user.entity;

import com.ceos21.ceos21BE.domain.*;
import com.ceos21.ceos21BE.web.answer.entity.Answer;
import com.ceos21.ceos21BE.web.comment.entity.Comment;
import com.ceos21.ceos21BE.web.question.entity.Question;
import com.ceos21.ceos21BE.web.reaction.entity.Reaction;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String email;
    private String password;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Question> questions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Answer> answers;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reaction> reactions;
}
