package com.ceos21.springknowledgein.domain.knowledgein.repository;

import com.ceos21.springknowledgein.domain.user.repository.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn
    private Member member;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name= "comment_id")
    private List<CoComment> coComments = new ArrayList<>();

    public Comment() {}

    public Comment(String content) {
        this.content = content;
    }

    public void addCoComment(CoComment coComment) {
        coComments.add(coComment);
    }

    public void removeCoComment(CoComment coComment) {
        coComments.remove(coComment);
    }



}
