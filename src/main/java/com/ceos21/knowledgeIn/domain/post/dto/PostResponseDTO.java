package com.ceos21.knowledgeIn.domain.post.dto;


import com.ceos21.knowledgeIn.domain.common.PostType;
import com.ceos21.knowledgeIn.domain.hashTag.HashTagDTO;
import com.ceos21.knowledgeIn.domain.image.Image;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.global.exceptionHandler.ErrorStatus;
import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


public class PostResponseDTO {

    public static PostResponseDTO from(Post post) {

        List<HashTagDTO> hashTagDTOs = post.getHashTags().stream().map(tag->(HashTagDTO.builder()
                .tagId(tag.getHashTag().getId()).tagName(tag.getHashTag().getName())).build()).toList();

        List<String> imgUrls = post.getImages().stream().map(Image::getImageUrl).toList();

        String writerName = post.getIsAnonymous() ? "비공개" : post.getMember().getNickname();

        if(post.getPostType().equals(PostType.QUESTION)){
            return QuestionResponseDTO.builder()
                    .postId(post.getId())
                    .writerName(writerName)
                    .createdAt(post.getCreatedAt())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .hashTags(hashTagDTOs)
                    .images(imgUrls)
                    .commentCount(post.getCommentCnt())
                    .answerCount(post.getAnswerCnt())
                    .build();
        }else if(post.getPostType().equals(PostType.ANSWER)){
            return  AnswerResponseDTO.builder()
                    .postId(post.getId())
                    .writerId(post.getMember().getId())
                    .writerName(post.getMember().getNickname())
                    .createdAt(post.getCreatedAt())
                    .content(post.getContent())
                    .hashTags(hashTagDTOs)
                    .images(imgUrls)
                    .likeCount(post.getLikeCnt())
                    .dislikeCount(post.getDisLikeCnt())
                    .commentCount(post.getCommentCnt())
                    .build();
        }else{
            throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
