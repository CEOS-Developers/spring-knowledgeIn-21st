package com.ceos21.knowledgeIn.controller.dto.post;

import com.ceos21.knowledgeIn.domain.post.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class PostCreateDTO {
    private String title;

    private String content;

    private PostType postType;

    private Long userId;

    private Long parentPostId;

    private List<String> hashTagContents;

}
