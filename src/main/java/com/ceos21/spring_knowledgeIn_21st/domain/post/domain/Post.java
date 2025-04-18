package com.ceos21.spring_knowledgeIn_21st.domain.post.domain;

import com.ceos21.spring_knowledgeIn_21st.domain.baseEntity.domain.BaseEntity;
import com.ceos21.spring_knowledgeIn_21st.domain.postHashtag.domain.PostHashtag;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.Comment;
import com.ceos21.spring_knowledgeIn_21st.domain.image.domain.Image;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

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

    public void addImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();  // 안전하게 빈 리스트로 초기화
        }
        this.images.add(image);
    }
    public void addPostHashtag(PostHashtag postHashtag) {
        if (this.postHashtags == null) {
            this.postHashtags = new ArrayList<>();  // 안전하게 빈 리스트로 초기화
        }
        this.postHashtags.add(postHashtag);
    }
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
