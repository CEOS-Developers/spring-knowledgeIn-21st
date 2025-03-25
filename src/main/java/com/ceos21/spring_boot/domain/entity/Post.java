package com.ceos21.spring_boot.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    @Column(length=30)
    private String title;

    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Answer> answers;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostHash> postHashtags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="writer_id")
    private User writer;
}
