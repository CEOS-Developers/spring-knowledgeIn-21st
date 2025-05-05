package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.post.HashTagCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.HashTagResponseDTO;
import com.ceos21.knowledgeIn.controller.dto.post.PostCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.PostHashTagResponseDTO;
import com.ceos21.knowledgeIn.domain.post.PostType;
import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class HashTagServiceTest {
    @Autowired
    com.ceos21.knowledgeIn.service.PostService
            postService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    com.ceos21.knowledgeIn.service.HashTagService hashTagService;
    @Autowired
    EntityManager em;

    User user;
    PostCreateDTO dto;
    String hashtag1 = "#giveme";
    String hashtag2 = "#vacation";

    @BeforeEach
    void init() {
        user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();
        userRepository.save(user);

        dto = PostCreateDTO.builder()
                .title("title")
                .content("content")
                .userId(user.getId())
                .postType(PostType.QUESTION)
                .hashTagContents(List.of(hashtag1, hashtag2))
                .build();
    }
    @Test
    void createHashTag() {
        //given
        Long postId = postService.createPost(dto);

        //when
        HashTagCreateDTO dto1 = HashTagCreateDTO.builder()
                .contents(dto.getHashTagContents())
                .postId(postId)
                .build();
        List<Long> hashTagIds = hashTagService.createOrFindHashTags(dto1.getContents(),postId);

        //then
        hashTagIds.forEach(id->log.info("id: "+id+"\n"));
        assertEquals(hashTagIds.size(), 2);
    }

    @Test
    void createPostHashTagTest() {
        //given
        Long postId = postService.createPost(dto);

        //when
        HashTagCreateDTO dto1 = HashTagCreateDTO.builder()
                .contents(dto.getHashTagContents())
                .postId(postId)
                .build();
        List<Long> hashTagIds = hashTagService.createOrFindHashTags(dto1.getContents(),postId);

        //then
        List<PostHashTagResponseDTO> postHashTags = hashTagService.findPostHashTag(postId);
        assertEquals(postHashTags.size(), 2);
    }
    @Test
    void findHashTag() {
        //given
        Long postId = postService.createPost(dto);
        HashTagCreateDTO dto1 = HashTagCreateDTO.builder()
                .contents(dto.getHashTagContents())
                .postId(postId)
                .build();
        List<Long> hashTagIds = hashTagService.createOrFindHashTags(dto1.getContents(),postId);

        //when
        HashTagResponseDTO hashTagDTO = hashTagService.findHashTag(hashTagIds.get(1));

        //then
        assertEquals(hashTagDTO.getContent(), hashtag2);
    }

    @Test
    void findAllHashTags() {
        //given
        Long postId = postService.createPost(dto);
        HashTagCreateDTO dto = HashTagCreateDTO.builder()
                .contents(this.dto.getHashTagContents())
                .postId(postId)
                .build();
        List<Long> hashTagIds = hashTagService.createOrFindHashTags(dto.getContents(), postId);
        //when
        List<HashTagResponseDTO> hashTagDTOs = hashTagService.findAllHashTags();

        //then
        assertEquals(hashTagDTOs.size(),2);
    }

    @Test
    void deleteHashTag() {
        //given
        Long postId = postService.createPost(dto);
        HashTagCreateDTO dto = HashTagCreateDTO.builder()
                .contents(this.dto.getHashTagContents())
                .postId(postId)
                .build();
        List<Long> hashTagIds = hashTagService.createOrFindHashTags(dto.getContents(),postId);

        //when
        Long deleteHashTagId = hashTagIds.get(0);
        hashTagService.deletePostHashTagByHashTagId(deleteHashTagId); //HashTag삭제를 위해선 PostHashTag삭제가 우선
        em.flush();
        em.clear(); //테스트 코드이기에 HashTagService의 트랜잭션이 종료된건 맞지만, 영속성 컨텍스트는 비워지지 않음. => 수동 비우기 필수!!!
        hashTagService.deleteHashTag(deleteHashTagId);

        //then
        int size = hashTagService.findAllHashTags().size();
        assertEquals(size,1);
    }

}