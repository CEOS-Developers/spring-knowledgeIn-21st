package com.ceos21.spring_knowledgeIn_21st.domain.hashtag.domain;

import com.ceos21.spring_knowledgeIn_21st.domain.postHashtag.domain.PostHashtag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Hashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @NotBlank
    private String content;

    @NotNull
    @Column(name = "post_count")
    private Integer postCount;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @Builder
    public Hashtag(String content) {
        this.content = content;
        this.postCount = 0;
        this.postHashtags = new ArrayList<>();
    }

    public void addPostHashtag(PostHashtag postHashtag) {
        this.postHashtags.add(postHashtag);
        postHashtag.setHashtag(this);
    }

    public void increasePostCount() {
        this.postCount++;
    }

    public void decreasePostCount() {
        this.postCount--;
    }

    public boolean isUnused() {
        return this.postCount <= 0;
    }
}