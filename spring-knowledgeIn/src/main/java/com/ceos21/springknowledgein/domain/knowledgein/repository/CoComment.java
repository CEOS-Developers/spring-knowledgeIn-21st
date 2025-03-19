package com.ceos21.springknowledgein.domain.knowledgein.repository;

import com.ceos21.springknowledgein.domain.user.repository.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CoComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn
    private Member member;

    public CoComment() {}

    public CoComment(String content) {
        this.content = content;
    }
}
