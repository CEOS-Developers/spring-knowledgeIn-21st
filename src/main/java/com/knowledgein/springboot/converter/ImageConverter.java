package com.knowledgein.springboot.converter;

import com.knowledgein.springboot.domain.Image;

public class ImageConverter {
    public static Image toImage(String imageUrl) {
        return Image.builder()
                .imageUrl(imageUrl)
                .build();
    }
}
