package com.knowledgein.springboot.service.imageService;

import org.springframework.web.multipart.MultipartFile;

public interface ImageCommandService {
    String uploadFile(MultipartFile image, String extension);
}
