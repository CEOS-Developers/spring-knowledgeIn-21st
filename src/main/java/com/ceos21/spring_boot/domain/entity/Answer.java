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
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="answer_id")
    private Long id;

    private String content;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="writer_id")
    private User writer;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

}