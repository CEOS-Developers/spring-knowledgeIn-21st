package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.comment.Comment;
import com.ceos21.knowledgeIn.domain.common.PostType;
import com.ceos21.knowledgeIn.domain.image.Image;
import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Post extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Post parent;

    private String title;

    private String content;

    private PostType postType;

    private Boolean isAnonymous;

    @Builder.Default
    private Integer likeCnt = 0;

    @Builder.Default
    private Integer hateCnt = 0;

    @Builder.Default
    private Integer commentCnt = 0;


    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();


}
