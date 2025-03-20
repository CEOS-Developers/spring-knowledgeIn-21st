package com.ceos21.knowledgeIn.domain.user;

import com.ceos21.knowledgeIn.domain.comment.Comment;
import com.ceos21.knowledgeIn.domain.hate.Hates;
import com.ceos21.knowledgeIn.domain.like.Likes;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.global.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class User extends BaseEntity {

    private String name;

    private String email;

    private String password;

    private String username;

    private String nickname;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Likes> likes;

    @OneToMany(mappedBy = "user")
    private List<Hates> hates;
}
