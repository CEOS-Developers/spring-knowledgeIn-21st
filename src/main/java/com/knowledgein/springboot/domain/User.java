package com.knowledgein.springboot.domain;

import com.knowledgein.springboot.domain.common.BaseEntity;
import com.knowledgein.springboot.domain.enums.PostType;
import com.knowledgein.springboot.domain.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String email;

    @Column(nullable = false, length = 10)
    private String nickName;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'USER'")
    private RoleType roleType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Reaction> reactionList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Post> postList = new ArrayList<>();

    public void addReaction(Reaction commentReaction) {
        reactionList.add(commentReaction);
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public void addPost(Post post) {
        postList.add(post);
    }

    public void updatePassword(String password){
        this.password = password;
    }
}
