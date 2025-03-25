package com.ceos21.knowledgeIn.domain.member;

import com.ceos21.knowledgeIn.domain.comment.Comment;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.global.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member extends BaseEntity {

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String nickname;

    private String phone;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();
}
