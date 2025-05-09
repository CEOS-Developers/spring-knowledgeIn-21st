package com.ceos21.ceos21BE.web.post.service;

import com.ceos21.ceos21BE.web.post.entity.Image;
import com.ceos21.ceos21BE.web.post.entity.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    public void addImagesToPost(List<String> imageUrls, Post post) {
        for (String url : imageUrls) {
            if (url == null || url.isBlank()) continue;

            Image image = Image.builder()
                    .url(url)
                    .post(post)
                    .build();

            post.getImages().add(image);
        }
    }
}
