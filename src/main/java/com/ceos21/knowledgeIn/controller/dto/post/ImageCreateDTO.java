package com.ceos21.knowledgeIn.controller.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Builder
@Data
public class ImageCreateDTO {
    private Long postId;

    private MultipartFile file;

}
