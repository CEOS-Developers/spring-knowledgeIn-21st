package com.ceos21.knowledgeIn.controller.dto.post;

import com.ceos21.knowledgeIn.domain.post.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ImageResponseDTO {
    private Long id;

    private String imageUrl;

    private Long postId;

    public static ImageResponseDTO from(Image image) {
        return ImageResponseDTO.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .postId(image.getPost().getId())
                .build();
    }
}
