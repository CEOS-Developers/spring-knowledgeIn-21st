package com.ceos21.ceos21BE.web.post.service;

import com.ceos21.ceos21BE.global.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.global.apiPayload.exception.GeneralException;
import com.ceos21.ceos21BE.web.post.converter.HashtagConverter;
import com.ceos21.ceos21BE.web.post.dto.response.HashtagPostResponseDTO;
import com.ceos21.ceos21BE.web.post.dto.response.PostResponseDto;
import com.ceos21.ceos21BE.web.post.entity.Hashtag;
import com.ceos21.ceos21BE.web.post.repository.HashtagRepository;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.entity.PostHashtag;
import com.ceos21.ceos21BE.web.post.repository.PostHashtagRepository;
import com.ceos21.ceos21BE.web.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final HashtagConverter hashtagConverter;

    public List<String> getAllHashtags() {
        return hashtagRepository.findAll().stream()
                .map(Hashtag::getName)
                .toList();
    }

    public List<HashtagPostResponseDTO> getPostsByHashtag(String name) {
        Hashtag hashtag = validateHashtag(name);

        List<PostHashtag> postHashtags = postHashtagRepository.findAllByHashtag(hashtag);

        return postHashtags.stream()
                .map(PostHashtag::getPost)
                .map(hashtagConverter::toPostDto)
                .collect(Collectors.toList());
    }

    protected Hashtag validateHashtag(String name) {
        return hashtagRepository.findByName(name)
                .orElseThrow(() -> new GeneralException(ErrorStatus._HASHTAG_NOT_FOUND));
    }

    @Transactional
    public void addHashtagsToPost(List<String> hashtags, Post post) {
        for (String tagName : hashtags) {
            if (tagName == null || tagName.isBlank()) continue;

            Hashtag hashtag = hashtagRepository.findByName(tagName)
                    .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(tagName).build()));

            PostHashtag postHashtag = PostHashtag.builder()
                    .post(post)
                    .hashtag(hashtag)
                    .build();

            post.getPostHashtags().add(postHashtag);
        }
    }
}
