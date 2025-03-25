package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.user.User;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class PostThumb {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postthumb_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;

    public static PostThumb create(Post post, User user) {
        PostThumb postThumb = PostThumb.builder()
                .post(post)
                .user(user)
                .build();

        post.addPostThumb(postThumb);

        return postThumb;
    }
}
