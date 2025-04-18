package com.ceos21.spring_knowledgeIn_21st.domain.image.application;

import com.ceos21.spring_knowledgeIn_21st.domain.image.dao.ImageRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.image.domain.Image;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public void saveImageUrls(Post post, List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        imageUrls.forEach(url -> {
            Image image = Image.builder()
                    .post(post)
                    .imageUrl(url)
                    .build();
            post.addImage(image);
            imageRepository.save(image);
        });
    }
}
