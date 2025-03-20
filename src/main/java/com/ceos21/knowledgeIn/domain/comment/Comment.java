package com.ceos21.knowledgeIn.domain.comment;

import com.ceos21.knowledgeIn.domain.hate.Hates;
import com.ceos21.knowledgeIn.domain.like.Likes;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    private String content;

    @OneToMany(mappedBy = "comment")
    private List<Likes> likes;

    @OneToMany(mappedBy = "comment")
    private List<Hates> hates;


}
