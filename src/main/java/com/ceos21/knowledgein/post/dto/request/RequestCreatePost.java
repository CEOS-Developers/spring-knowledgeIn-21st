package com.ceos21.knowledgein.post.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record RequestCreatePost(
        @NotNull
        String title,
        @NotNull
        String content,
        @NotNull
        boolean nicknamePublic,
        List<MultipartFile> images,
        List<String> hashTags
) {
}
