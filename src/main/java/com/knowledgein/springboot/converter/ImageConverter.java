package com.knowledgein.springboot.converter;

import com.knowledgein.springboot.domain.Image;
import com.knowledgein.springboot.domain.Post;

public class ImageConverter {
    public static Image toImage(Post post, String imageUrl) {
        return Image.builder()
                .post(post)
                .imageUrl(imageUrl)
                .build();
    }
}
