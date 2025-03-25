package com.ceos21.springknowledgein.domain.knowledgein.repository;

import jakarta.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;


    public Image() {}

    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
