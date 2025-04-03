package com.ceos21.knowledgeIn.domain.post;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class PostHashTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_hashtag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id")
    private Post post;

    public static PostHashTag create(HashTag hashTag, Post post) {
        if (hashTag == null || post == null) throw new IllegalArgumentException("인자에 Null 값이 있습니다");

        PostHashTag postHashTag = PostHashTag.builder()
                .hashTag(hashTag)
                .post(post)
                .build();

        post.addPostHashTag(postHashTag);

        return postHashTag;
    }

}
