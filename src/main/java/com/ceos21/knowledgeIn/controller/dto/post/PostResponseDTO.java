package com.ceos21.knowledgeIn.controller.dto.post;

import com.ceos21.knowledgeIn.domain.post.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

//hashtag아이디 넣어야함 개선사항!
@AllArgsConstructor
@Builder
@Data
public class PostResponseDTO {
    private Long id;

    private String title;

    private String content;

    private PostType postType;

    private Long userId;

    private Long parentPostId;

    public static PostResponseDTO from(Post post) {
        Optional<PostResponseDTO> dto = Optional.empty();

        if (post.getPostType().equals(PostType.QUESTION)) {
            dto = Optional.of(PostResponseDTO.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postType(PostType.QUESTION)
                    .userId(post.getUser().getId())
                    .build());
        } else if(post.getPostType().equals(PostType.ANSWER)) {
            dto = Optional.of(PostResponseDTO.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postType(PostType.ANSWER)
                    .userId(post.getUser().getId())
                    .parentPostId(post.getParentPost().getId())
                    .build());
        }

        return dto.orElseThrow();
    }
}
