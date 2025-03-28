package com.ceos21.knowledgein.post.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "hashtag")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class HashTag {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tag;

    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public static HashTag createWithNoPost(String tag) {
        return HashTag.builder()
                .tag(tag)
                .build();
    }

    public static List<HashTag> of(List<String> tags) {
        return tags.stream()
                .map(HashTag::of)
                .collect(toList());
    }

    public static HashTag of(String tag) {
        return HashTag.builder()
                .tag(tag)
                .build();
    }

    @Builder(access = PRIVATE)
    private HashTag(String tag, Post post) {
        this.tag = tag;
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
        if (!post.getHashTags().contains(this)) {
            post.getHashTags().add(this);
        }
    }
}
