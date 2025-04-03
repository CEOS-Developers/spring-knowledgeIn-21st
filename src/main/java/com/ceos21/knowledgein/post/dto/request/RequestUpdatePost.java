package com.ceos21.knowledgein.post.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record RequestUpdatePost(
        String title,
        String content,
        List<MultipartFile> images,
        List<String> hashTags
) {
}
