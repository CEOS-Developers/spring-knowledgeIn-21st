package com.ceos21.knowledgein.post.domain;

import com.ceos21.knowledgein.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "image")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Image extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String storageUrl;
    private String uploadFileName;

    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private Post post;

    @JoinColumn(name = "reply_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = LAZY)
    private Reply reply;

    public static Image createWithNoPost(String storageUrl, String uploadFileName) {
        return Image.builder()
                .storageUrl(storageUrl)
                .uploadFileName(uploadFileName)
                .build();
    }

    public void setPost(Post post) {
        this.post = post;
        if (!post.getImages().contains(this)) {
            post.getImages().add(this);
        }
    }

    public void setReply(Reply reply) {
        this.reply = reply;
        if (!reply.getImages().contains(this)) {
            reply.getImages().add(this);
        }
    }

    @Builder(access = PRIVATE)
    private Image(String storageUrl, String uploadFileName, Post post) {
        this.storageUrl = storageUrl;
        this.uploadFileName = uploadFileName;
        this.post = post;
    }

}
