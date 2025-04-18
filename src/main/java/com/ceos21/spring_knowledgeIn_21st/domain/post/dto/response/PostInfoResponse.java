package com.ceos21.spring_knowledgeIn_21st.domain.post.dto.response;

import com.ceos21.spring_knowledgeIn_21st.domain.image.domain.Image;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostInfoResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private List<String> hashtags;
    private List<String> imageUrls;

    public PostInfoResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.userId = post.getUser().getId();
        this.hashtags = post.getPostHashtags().stream()
                .map(postHashtag -> postHashtag.getHashtag().getContent())
                .collect(Collectors.toList());
        this.imageUrls = post.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
    }
}
