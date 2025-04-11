package com.ceos21.ceos21BE.web.post.dto.request;

import com.ceos21.ceos21BE.web.post.entity.PostType;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreatePostRequest {
    //private Long userId;
    private String title;
    private String content;
    private PostType postType;
    private List<String> hashtags;
    private List<String> imageUrls;
    private Long parentId;
}
