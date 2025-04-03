package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.common.BaseTimeEntity;
import com.ceos21.knowledgeIn.domain.user.User;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Comment extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id")
    private Post post;

/*
    @Builder(access = AccessLevel.PRIVATE)
    private Comment(User user, Post post, String content) {
        //!! null체크가 누락돼있음 개선필요
        this.user = user;
        this.post = post;
        this.content = content;
    }
*/

    public static Comment create(User user, Post post, String content) {
        if (user == null || post == null || content == null) throw new IllegalArgumentException("인자에 Null 값이 있습니다");

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
        post.addComment(comment);

        return comment;
    }

}
