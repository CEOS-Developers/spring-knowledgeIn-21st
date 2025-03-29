package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.common.PostType;
import com.ceos21.knowledgeIn.domain.image.Image;
import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.post.dto.PostRequestDTO;
import com.ceos21.knowledgeIn.domain.postHashTag.PostHashTag;
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
    private Integer disLikeCnt = 0;

    @Builder.Default
    private Integer commentCnt = 0;

    @Builder.Default
    private Integer answerCnt = 0;


    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostHashTag> hashTags = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> post = new ArrayList<>();

    //편의 메서드
    public Post update(PostRequestDTO.PostUpdateRequestDTO requestDTO, List<PostHashTag> newHashTags, List<PostHashTag> remainingHashTags,
                       List<Image> newImages, List<Image> remainingImages) {

        //제목은 질문일 경우에만 변경 가능
        if(this.postType==PostType.QUESTION){
            if(requestDTO.getTitle()!=null){this.title = requestDTO.getTitle();}
        }

        if(requestDTO.getContent()!=null){this.content = requestDTO.getContent();}
        if(requestDTO.getIsAnonymous() != null){this.isAnonymous = requestDTO.getIsAnonymous();}
        //해시태그 삭제, 추가 반영
        if(!remainingHashTags.isEmpty()){this.hashTags.retainAll(remainingHashTags);}
        if(!newHashTags.isEmpty()){this.hashTags.addAll(newHashTags);}
        //이미지 삭제, 추가 반영
        if(!remainingImages.isEmpty()){this.images.retainAll(remainingImages);}
        if(!newImages.isEmpty()){this.images.addAll(newImages);}

        return this;

    }

    public Post setHashTags(List<PostHashTag> hashTags) {
        this.hashTags = hashTags;
        return this;
    }

    public void setAnswerCnt(Boolean isAdd) {
        if(isAdd){answerCnt++;}
        else {answerCnt--;}
    }
}
