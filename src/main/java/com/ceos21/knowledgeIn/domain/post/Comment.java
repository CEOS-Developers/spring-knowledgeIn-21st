package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.user.User;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id")
    private Post post;

    @Builder(access = AccessLevel.PRIVATE)
    private Comment(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
    }

    public static Comment create(User user, Post post, String content) {
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
        post.addComment(comment);

        return comment;
    }

}
