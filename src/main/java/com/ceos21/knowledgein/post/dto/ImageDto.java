package com.ceos21.knowledgein.post.dto;

import com.ceos21.knowledgein.post.domain.Image;

import java.util.List;

public record ImageDto(
        String storageUrl,
        String uploadFileName
) {

    public static List<ImageDto> from(List<Image> images) {
        return images.stream()
                .map(ImageDto::from)
                .toList();
    }

    public static ImageDto from(Image image) {
        return new ImageDto(
                image.getStorageUrl(),
                image.getUploadFileName()
        );
    }
}
