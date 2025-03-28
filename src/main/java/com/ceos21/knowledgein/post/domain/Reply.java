package com.ceos21.knowledgein.post.domain;

import com.ceos21.knowledgein.global.domain.BaseTimeEntity;
import com.ceos21.knowledgein.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
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

    @OneToMany(mappedBy = "reply", cascade = {PERSIST, REMOVE})
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "parent", cascade = {REMOVE})
    private List<Reply> replyChildren = new ArrayList<>();

    public static Reply createWithNoParent(String content, UserEntity user, Post post, List<Image> images) {
        return Reply.builder()
                .accepted(false)
                .content(content)
                .user(user)
                .post(post)
                .images(images)
                .build();
    }

    @Builder(access = PRIVATE)
    private Reply(boolean accepted, String content, Reply parent, UserEntity user, Post post, List<Image> images) {
        this.accepted = accepted;
        this.content = content;
        this.parent = parent;
        this.user = user;
        this.post = post;
        this.images = images;

        if (images != null) {
            images.forEach(image -> image.setReply(this));
        }
    }

    public void setPost(Post post) {
        this.post = post;
        if (!post.getReplies().contains(this)) {
            post.getReplies().add(this);
        }
    }

    public void addReplyImage(Image image) {
        images.add(image);
        if (image.getReply() != this) {
            image.setReply(this);
        }
    }

}
