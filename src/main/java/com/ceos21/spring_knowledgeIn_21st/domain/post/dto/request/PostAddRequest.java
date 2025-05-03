package com.ceos21.spring_knowledgeIn_21st.domain.post.dto.request;

import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PostAddRequest (
    @NotBlank
    String title,

    @NotBlank
    String content,

    List<String> hashtags,
    List<String> imageUrls
){
    public Post toEntity(User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
