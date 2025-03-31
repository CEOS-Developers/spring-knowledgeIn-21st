package com.ceos21.springknowledgein.domain.knowledgein.repository;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PostHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    public PostHashtag() {}

    public PostHashtag(Post post, Hashtag hashtag) {
        this.post = post;
        this.hashtag = hashtag;
    }

    public void setPost(Post post) {
        this.post = post;
        post.getPostHashtags().add(this);

    }

    public void setHashtag(Hashtag hashtag) {
        this.hashtag = hashtag;
        post.getPostHashtags().add(this);
    }

}
