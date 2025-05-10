package com.knowledgein.springboot.service.imageService;

import com.knowledgein.springboot.web.dto.imageDTO.ImageRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImageCommandService {
    String uploadFile(MultipartFile image, String extension);
}
