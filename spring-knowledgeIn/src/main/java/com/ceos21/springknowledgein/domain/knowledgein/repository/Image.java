package com.ceos21.springknowledgein.domain.knowledgein.repository;

import jakarta.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne //image => post
    @JoinColumn(name = "post_id")
    private Post post;

    public Image() {}

    public Image(String imageUrl, Post post) {
        this.imageUrl = imageUrl;
        this.post = post;
    }
    /*
    public void setPost(Post post) {
        this.post = post;
    }
    */

}
