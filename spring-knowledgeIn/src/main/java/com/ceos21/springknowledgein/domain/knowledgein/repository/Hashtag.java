package com.ceos21.springknowledgein.domain.knowledgein.repository;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagName;

    @OneToMany
    private List<PostHashtag> postHashtags = new ArrayList<>();

    public Hashtag() {}

    public Hashtag(String tagName) {
        this.tagName = tagName;
    }
}
