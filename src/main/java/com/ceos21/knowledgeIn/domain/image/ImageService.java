package com.ceos21.knowledgeIn.domain.image;

import com.ceos21.knowledgeIn.global.exceptionHandler.Status;
import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image createFile(MultipartFile img) {
        return Image.builder().imageUrl("실험").build();
    }

    public Image getImageByUrl(String imageUrl) {
        return imageRepository.findByImageUrl(imageUrl).orElseThrow(()->new GeneralException(Status.NOT_FOUND));
    }
}
