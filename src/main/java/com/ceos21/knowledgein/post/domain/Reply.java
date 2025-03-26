package com.ceos21.knowledgein.post.domain;

import com.ceos21.knowledgein.global.domain.BaseTimeEntity;
import com.ceos21.knowledgein.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "reply")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Reply extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ColumnDefault("false")
    private boolean accepted;
    @Column(length = 2048)
    private String content;

    // 대댓글
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = LAZY)
    private Reply parent;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private UserEntity user;

    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private Post post;

    public static Reply createWithNoParent(String content, UserEntity user, Post post) {
        return Reply.builder()
                .accepted(false)
                .content(content)
                .user(user)
                .post(post)
                .build();
    }

    @Builder(access = PRIVATE)
    private Reply(boolean accepted, String content, Reply parent, UserEntity user, Post post) {
        this.accepted = accepted;
        this.content = content;
        this.parent = parent;
        this.user = user;
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
        if (!post.getReplies().contains(this)) {
            post.getReplies().add(this);
        }
    }

}
