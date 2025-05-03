package com.ceos21.spring_knowledgeIn_21st.domain.post.domain;

import com.ceos21.spring_knowledgeIn_21st.domain.answer.domain.Answer;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.AnswerComment;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.PostComment;
import com.ceos21.spring_knowledgeIn_21st.global.common.BaseEntity;
import com.ceos21.spring_knowledgeIn_21st.domain.postHashtag.domain.PostHashtag;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.BaseComment;
import com.ceos21.spring_knowledgeIn_21st.domain.image.domain.Image;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Builder
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.postComments = new ArrayList<>();
        this.images = new ArrayList<>();
        this.postHashtags = new ArrayList<>();
    }

    public void addImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();  // 안전하게 빈 리스트로 초기화
        }
        this.images.add(image);
    }
    public void addPostHashtag(PostHashtag postHashtag) {
        this.postHashtags.add(postHashtag);
        postHashtag.setPost(this);
    }
    public void update(String title, String content) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setPost(this);
    }

    public void addPostComment(PostComment comment) {
        this.postComments.add(comment);
        comment.setPost(this);
    }
}
