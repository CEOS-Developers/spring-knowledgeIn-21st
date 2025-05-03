package com.ceos21.spring_knowledgeIn_21st.domain.answer.domain;

import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.AnswerComment;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.BaseComment;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import com.ceos21.spring_knowledgeIn_21st.global.common.BaseEntity;
import com.ceos21.spring_knowledgeIn_21st.domain.reaction.domain.Reaction;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Answer extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotBlank
    private String content;

    @NotNull
    @JoinColumn(name = "like_count")
    private Integer likeCount = 0;

    @NotNull
    @JoinColumn(name = "dislike_count")
    private Integer dislikeCount = 0;

    @NotNull
    @JoinColumn(name = "comment_count")
    private Integer commentCount = 0;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<AnswerComment> answerComments = new ArrayList<>();

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<Reaction> reactions = new ArrayList<>();

    @Builder
    public Answer(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.likeCount = 0;
        this.dislikeCount = 0;
        this.commentCount = 0;
    }

    public void update(String content){
        this.content = content;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void addAnswerComment(AnswerComment comment) {
        this.answerComments.add(comment);
        comment.setAnswer(this);
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void increaseDislikeCount() {
        this.dislikeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void decreaseDislikeCount() {
        this.dislikeCount--;
    }
}
