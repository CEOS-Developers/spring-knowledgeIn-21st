package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.post.ImageCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.ImageResponseDTO;
import com.ceos21.knowledgeIn.domain.post.Image;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.exception.CustomJisikInException;
import com.ceos21.knowledgeIn.exception.ErrorCode;
import com.ceos21.knowledgeIn.fileManage.FileStorageService;
import com.ceos21.knowledgeIn.repository.ImageRepository;
import com.ceos21.knowledgeIn.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;

    //create
    @Transactional
    public Long createImage(ImageCreateDTO dto){
        Post post = postRepository.findById(dto.getPostId()).orElseThrow();

        String fileUrl = null;
        try {
            fileUrl = fileStorageService.storeFile(dto.getFile());
        } catch (IOException e) {
            throw new CustomJisikInException(e, ErrorCode.CAN_NOT_SAVE_IMAGE);
        }

        Image entity = Image.create(post, fileUrl);
        return imageRepository.save(entity).getId();
    }

    //read
    public ImageResponseDTO findImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow();

        return ImageResponseDTO.from(image);
    }

    public List<ImageResponseDTO> findAllImages() {
        return imageRepository.findAll()
                .stream()
                .map(image -> ImageResponseDTO.from(image))
                .collect(Collectors.toList());
    }

    //update
    //delete
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    public List<ImageResponseDTO> findAllImagesByPostId(Long postId) {
        List<Image> images = imageRepository.findAllByPostId(postId);

        return images.stream()
                .map(image -> ImageResponseDTO.from(image))
                .collect(Collectors.toList());
    }

    @Transactional
    public ImageResponseDTO modifyImage(Long imageId, MultipartFile file) {
        Image findImage = imageRepository.findById(imageId).orElseThrow();
        String olfFileUrl = findImage.getImageUrl();
        String newFileUrl = null;

        //기존 파일 삭제
        try {
            fileStorageService.deleteFile(olfFileUrl);
        } catch (IOException e) {
            throw new CustomJisikInException(e, ErrorCode.CAN_NOT_DELETE_IMAGE);
        }
        //새 파일 저장
        try {
            newFileUrl = fileStorageService.storeFile(file);
        } catch (IOException e) {
            throw new CustomJisikInException(e, ErrorCode.CAN_NOT_SAVE_IMAGE);
        }

        //파일 경로 수정
        findImage.updateUrl(newFileUrl);

        return ImageResponseDTO.from(findImage);
    }
}
