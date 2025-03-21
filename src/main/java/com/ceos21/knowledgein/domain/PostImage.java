package com.ceos21.knowledgein.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "post_image")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class PostImage {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String storageUrl;
    private String uploadFileName;

    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private Post post;

    @Builder
    public PostImage(String storageUrl, String uploadFileName, Post post) {
        this.storageUrl = storageUrl;
        this.uploadFileName = uploadFileName;
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
        if (!post.getPostImages().contains(this)) {
            post.getPostImages().add(this);
        }
    }
}
