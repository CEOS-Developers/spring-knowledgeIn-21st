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
public class QuestionResponseDTO extends PostResponseDTO {
    private Long postId;
    private String writerName;
    private LocalDateTime createdAt;
    private String title;
    private String content;
    private List<HashTagDTO> hashTags;
    private List<String> images;
    private Integer commentCount;
    private Integer answerCount;
}
