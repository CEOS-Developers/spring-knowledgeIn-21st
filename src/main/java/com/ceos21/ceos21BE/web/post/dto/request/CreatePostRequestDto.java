package com.ceos21.ceos21BE.web.post.dto.request;

import com.ceos21.ceos21BE.web.post.entity.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreatePostRequestDto {
    // 질문일 경우만 가능
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private PostType postType; // Question or Answer

    private List<String> hashtags;

    private List<String> imageUrls;

    // 답변일 경우만 필요
    private Long parentId;
}
