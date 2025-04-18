package com.ceos21.spring_knowledgeIn_21st.domain.answer.domain;

import com.ceos21.spring_knowledgeIn_21st.domain.baseEntity.domain.BaseEntity;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.Comment;
import com.ceos21.spring_knowledgeIn_21st.domain.reaction.domain.Reaction;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Answer extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    private String comment;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<Reaction> reactions = new ArrayList<>();
}
