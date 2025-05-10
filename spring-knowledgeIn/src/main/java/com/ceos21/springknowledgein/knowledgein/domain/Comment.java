package com.ceos21.springknowledgein.knowledgein.domain;

import com.ceos21.springknowledgein.user.repository.Member;
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
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name= "comment_id")
    private List<CoComment> coComments = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Like> likes = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Hate> hates = new ArrayList<>();

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

    public void addLike(Like like) {
        likes.add(like);
    }

    public void removeLike(Like like) {
        likes.remove(like);
    }

    public void addHate(Hate hate) { hates.add(hate); }

    public void removeHate(Hate hate) { hates.remove(hate); }



}
