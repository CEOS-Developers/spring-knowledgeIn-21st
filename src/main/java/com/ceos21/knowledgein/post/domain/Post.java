package com.ceos21.knowledgein.post.domain;

import com.ceos21.knowledgein.global.domain.BaseTimeEntityWithDeletion;
import com.ceos21.knowledgein.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Post extends BaseTimeEntityWithDeletion {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 2048)
    private String content;
    private Long viewCount;
    private boolean nicknamePublic;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = LAZY)
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = {PERSIST, REMOVE})
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = PERSIST)
    private List<HashTag> hashTags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Reply> replies = new ArrayList<>();

    public static Post of(String title,
                          String content,
                          boolean nicknamePublic,
                          List<HashTag> hashTags,
                          List<Image> images,
                          UserEntity user) {
        return Post.builder()
                .title(title)
                .content(content)
                .nicknamePublic(nicknamePublic)
                .hashTags(hashTags)
                .images(images)
                .user(user)
                .build();
    }

    public void addReply(Reply reply) {
        replies.add(reply);
        if (reply.getPost() != this) {
            reply.setPost(this);
        }
    }

    public void addPostImage(Image image) {
        images.add(image);
        if (image.getPost() != this) {
            image.setPost(this);
        }
    }

    public void addHashTag(HashTag hashTag) {
        hashTags.add(hashTag);
        if (hashTag.getPost() != this) {
            hashTag.setPost(this);
        }
    }

    @Builder(access = PRIVATE)
    private Post(String title, String content, boolean nicknamePublic, List<HashTag> hashTags, List<Image> images, UserEntity user) {
        this.title = title;
        this.content = content;
        this.nicknamePublic = nicknamePublic;
        this.images = images;
        this.hashTags = hashTags;
        this.user = user;

        this.viewCount = 0L;
        if (images != null) {
            images.forEach(postImage -> postImage.setPost(this));
        }
        if (hashTags != null) {
            hashTags.forEach(hashTag -> hashTag.setPost(this));
        }
    }

    public Post update(String title, String content, List<HashTag> hashTags, List<Image> images) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.hashTags = hashTags;

        return this;
    }
}
