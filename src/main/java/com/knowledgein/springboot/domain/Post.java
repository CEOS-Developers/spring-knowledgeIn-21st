package com.knowledgein.springboot.domain;

import com.knowledgein.springboot.domain.common.BaseEntity;
import com.knowledgein.springboot.domain.mapping.PostHashtag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 100)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostHashtag> postHashtagList = new ArrayList<>();

    public void addImage(Image image) {
        this.imageList.add(image);
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public void addPostHashtag(PostHashtag postHashtag) {
        this.postHashtagList.add(postHashtag);
    }

}
