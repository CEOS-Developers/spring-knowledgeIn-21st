package com.ceos21.spring_boot.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post_hashtag")
@Getter
@NoArgsConstructor @AllArgsConstructor
public class PostHashtag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posthashtag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", nullable = false)
    private Hashtag hashtag;

    // 정적 팩토리 메서드
    // -> PostHashtag.create(post, hashtag)만 호출하면 알아서 처리됨
    // => PostHashtag을 생성할 때마다 Post와 Hashtag에 '개발자가 직접 추가' 할 필요 "없음"
    public static PostHashtag create(Post post, Hashtag hashtag) {
        PostHashtag postHashtag = new PostHashtag();
        postHashtag.post = post;
        postHashtag.hashtag = hashtag;

        post.getPostHashtags().add(postHashtag);
        hashtag.getPostHashtags().add(postHashtag);

        return postHashtag;
    }
}
