package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.post.ImageCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.ImageResponseDTO;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.post.PostRepository;
import com.ceos21.knowledgeIn.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class ImageServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ImageService imageService;
    @Autowired
    EntityManager em;

    User user;
    Post post;
    String imageContent = "file content is here";

    MockMultipartFile mockFile= new MockMultipartFile(
            "name",
            "olaf.jpeg",
            MediaType.IMAGE_JPEG_VALUE,
            imageContent.getBytes()
    );


    @BeforeEach
    void init() {
        user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();
        userRepository.save(user);

        post = Post.createQuestion("title", "content", user);
        postRepository.save(post);
    }

    @Test
    void createImage() {
        //given
        ImageCreateDTO imageDTO = ImageCreateDTO.builder()
                .postId(post.getId())
                .file(mockFile)
                .build();

        //when
        Long id = imageService.createImage(imageDTO);
        //then
        assertThat(id).isNotNull();
    }

    @Test
    void findImage() {
        //given
        ImageCreateDTO imageDTO = ImageCreateDTO.builder()
                .postId(post.getId())
                .file(mockFile)
                .build();
        Long id = imageService.createImage(imageDTO);

        //when
        em.flush();
        em.clear();
        ImageResponseDTO image = imageService.findImage(id);

        //then
        assertThat(image.getId()).isNotNull();
        assertThat(image.getImageUrl()).isNotNull();
    }

    @Test
    void findAllImages() {
        //given
        ImageCreateDTO imageDTO = ImageCreateDTO.builder()
                .postId(post.getId())
                .file(mockFile)
                .build();
        Long id = imageService.createImage(imageDTO);

        String imageUrl2 = "/임의경로";
        ImageCreateDTO imageDTO2 = ImageCreateDTO.builder()
                .postId(post.getId())
                .file(mockFile)
                .build();
        Long id2 = imageService.createImage(imageDTO2);

        //when
        em.flush();
        em.clear();

        List<ImageResponseDTO> allImages = imageService.findAllImages();

        //then
        assertEquals(allImages.size(), 2);


        List<Long> ids = allImages.stream()
                .map(image -> image.getId())
                .collect(Collectors.toList());
        assertThat(ids.size()).isEqualTo(2);
        assertThat(ids).containsExactlyInAnyOrder(id, id2);
    }

    @Test
    void deleteImage() {
        //given
        String imageUrl = "/임의경로";
        ImageCreateDTO imageDTO = ImageCreateDTO.builder()
                .postId(post.getId())
                .file(mockFile)
                .build();
        Long id = imageService.createImage(imageDTO);

        String imageUrl2 = "/임의경로";
        ImageCreateDTO imageDTO2 = ImageCreateDTO.builder()
                .postId(post.getId())
                .file(mockFile)
                .build();
        Long id2 = imageService.createImage(imageDTO2);

        //when
        em.flush();
        em.clear();

        imageService.deleteImage(id);

        //then
        List<ImageResponseDTO> allImages = imageService.findAllImages();

        assertEquals(allImages.size(), 1);
        assertEquals(allImages.get(0).getId(), id2);
    }
}