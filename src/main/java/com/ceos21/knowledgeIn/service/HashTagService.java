package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.post.HashTagCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.HashTagResponseDTO;
import com.ceos21.knowledgeIn.controller.dto.post.PostHashTagResponseDTO;
import com.ceos21.knowledgeIn.domain.post.HashTag;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.post.PostHashTag;
import com.ceos21.knowledgeIn.repository.HashTagRepository;
import com.ceos21.knowledgeIn.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class HashTagService {
    private final HashTagRepository hashTagRepository;

    private final PostRepository postRepository;

    //create
    /**
     * HashTag엔티티 & PostHashTag엔티티 저장
     * */
    @Transactional
    public List<Long> createOrFindHashTags(List<String> contents,Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow();
        findPost.getHashTags().clear(); //기존 PostHashTag 리셋

        List<Long> hashTagIds = new ArrayList<>();

        List<HashTag> hashTags = contents.stream()
                .map(content -> HashTag.builder()
                        .content(content)
                        .build()
                )
                .collect(Collectors.toList());
        //HashTag 엔티티 없으면 save 있으면 find
        hashTags.forEach(hashTag -> {
            Optional<HashTag> optFindHashTag = hashTagRepository.findByContent(hashTag.getContent());
            if (optFindHashTag.isPresent()) {
                hashTagIds.add(optFindHashTag.get().getId());
            } else {
                hashTagRepository.save(hashTag);
                hashTagIds.add(hashTag.getId());
            }
                }
        );

        //PostHashTag 엔티티 저장
        hashTagIds.forEach(hashTagId -> {
            this.createPostHashTag(hashTagId, findPost.getId());
        });

        postRepository.save(findPost);
        return hashTagIds;
    }

    private void createPostHashTag(Long hashTagId, Long postId) {
        HashTag hashTag = hashTagRepository.findById(hashTagId).orElseThrow();
        Post findPost = postRepository.findById(postId).orElseThrow();
        PostHashTag.create(hashTag, findPost);
    }

    //read
    public HashTagResponseDTO findHashTag(Long id) {
        HashTag hashTag = hashTagRepository.findById(id).orElseThrow();

        return HashTagResponseDTO.from(hashTag);
    }

    public List<HashTagResponseDTO> findAllHashTags() {
        return hashTagRepository.findAll()
                .stream()
                .map(hashTag -> HashTagResponseDTO.from(hashTag))
                .collect(Collectors.toList());
    }

    public List<PostHashTagResponseDTO> findPostHashTag(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow();
        return findPost.getHashTags().stream()
                .map(postHashTag -> PostHashTagResponseDTO.from(postHashTag))
                .collect(Collectors.toList());
    }
    //update -> 필요없음. only create OR delete
    //delete
    @Transactional
    public void deleteHashTag(Long id) {
        hashTagRepository.deleteById(id);
    }

    @Transactional
    public void deletePostHashTagByHashTagId(Long hashTagId) {
        //PostHashTag 삭제
        hashTagRepository.deletePostHashTagByHashTagId(hashTagId);
    }
}
