package com.ceos21.knowledgeIn.controller.post;

import com.ceos21.knowledgeIn.controller.dto.post.ImageCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.ImageResponseDTO;
import com.ceos21.knowledgeIn.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class ImageController {
    private final ImageService imageService;

    @PostMapping(value = "/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = "application/json"
    )
    public Long createImage(@RequestParam("postId") Long postId, @RequestParam("file") MultipartFile file) {
        return imageService.createImage(ImageCreateDTO.builder()
                .postId(postId)
                .file(file)
                .build());
    }

    @GetMapping("/images/post/{postId}")
    public List<ImageResponseDTO> findAllImagesByPost(@PathVariable Long postId) {
        return imageService.findAllImagesByPostId(postId);
    }

    @PutMapping(value = "/images/{imageId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageResponseDTO modifyImage(@PathVariable Long imageId, @RequestParam("file") MultipartFile file) {
        return imageService.modifyImage(imageId, file);
    }

    @DeleteMapping("/images/{imageId}")
    public void deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
    }
}
