package com.ceos21.knowledgeIn.domain.post.dto;

import com.ceos21.knowledgeIn.domain.hashTag.HashTagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDTO extends PostResponseDTO {
    private Long postId;
    private Long writerId;
    private String writerName;
    private LocalDateTime createdAt;
    private String content;
    private List<HashTagDTO> hashTags;
    private List<String> images;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
}