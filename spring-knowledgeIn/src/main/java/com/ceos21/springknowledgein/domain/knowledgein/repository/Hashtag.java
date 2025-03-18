package com.ceos21.springknowledgein.domain.knowledgein.repository;

import jakarta.persistence.*;

@Entity
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagName;

    public Hashtag() {}

    public Hashtag(String tagName) {
        this.tagName = tagName;
    }
}
