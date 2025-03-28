package com.ceos21.knowledgein.post.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record RequestCreateReply(
        @NotNull
        String content,
        List<MultipartFile> images,
        List<String> hashTags
) {
}
