package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.comment.Comment;
import com.ceos21.knowledgeIn.domain.image.Image;
import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    private Boolean isAnonymous;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();


}
