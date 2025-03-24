package com.knowledgein.springboot.domain;

import com.knowledgein.springboot.domain.common.BaseEntity;
import com.knowledgein.springboot.domain.enums.PostType;
import com.knowledgein.springboot.domain.enums.ReactionType;
import com.knowledgein.springboot.domain.mapping.PostHashtag;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String title;

    @Column(nullable = false, length = 100)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'QUESTION'")
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostHashtag> postHashtagList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Post questionPost; // Default로 nullable

    @OneToMany(mappedBy = "questionPost", cascade = CascadeType.ALL)
    private List<Post> answerPostList = new ArrayList<>();

    public void addAnswerPost(Post post) {
        this.answerPostList.add(post);
    }

    public void addImage(Image image) {
        this.imageList.add(image);
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public void addPostHashtag(PostHashtag postHashtag) {
        this.postHashtagList.add(postHashtag);
    }
}
