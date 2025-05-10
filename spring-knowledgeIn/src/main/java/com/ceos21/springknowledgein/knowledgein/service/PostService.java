package com.ceos21.springknowledgein.knowledgein.service;

import com.ceos21.springknowledgein.knowledgein.dto.PostCreateDto;
import com.ceos21.springknowledgein.knowledgein.dto.PostResponseDto;
import com.ceos21.springknowledgein.knowledgein.dto.PostUpdateDto;
import com.ceos21.springknowledgein.domain.knowledgein.repository.*;
import com.ceos21.springknowledgein.knowledgein.domain.Hashtag;
import com.ceos21.springknowledgein.knowledgein.repository.HashtagRepository;
import com.ceos21.springknowledgein.knowledgein.domain.Image;
import com.ceos21.springknowledgein.knowledgein.domain.Post;
import com.ceos21.springknowledgein.knowledgein.domain.PostHashtag;
import com.ceos21.springknowledgein.knowledgein.repository.PostRepository;
import com.ceos21.springknowledgein.user.repository.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    public Post createPost(PostCreateDto dto, Member member) {
        Post post = new Post(dto.getTitle(), dto.getContent(), member);

        if (dto.getImages() != null) {
            for (Image image : dto.getImages()) {
                post.addImage(new Image(image.getImageUrl()));//id는 안 넣음
            }
            /*
        for (타입 변수명 : 순회할 대상) {
        반복할 코드}
         */

        }

        if (dto.getHashtags() != null) {
            for (Hashtag tag : dto.getHashtags()) {
                Hashtag hashtag = hashtagRepository.findByTagName(tag.getTagName())
                        .orElseGet(() -> hashtagRepository.save(new Hashtag(tag.getTagName())));

                PostHashtag postHashtag = new PostHashtag(post, hashtag);
                post.addPostHashtag(postHashtag);
            }
        }

        return postRepository.save(post);
    }

    public Post updatePost(PostUpdateDto dto) {
        /*
        1. 기존 포스트 찾기(id)로 찾을 거임
        2. 수정
        3. 저장
         */
        Post post = postRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.update(dto.getTitle(), dto.getContent());

        post.getImages().clear();
        for (Image image: dto.getImages()) {
            post.addImage(new Image(image.getImageUrl()));
        }

        post.getPostHashtags().clear();
        for (Hashtag tag : dto.getHashtags()) {
            Hashtag hashtag = hashtagRepository.findByTagName(tag.getTagName())
                    .orElseGet(()-> hashtagRepository.save(new Hashtag(tag.getTagName())));

            PostHashtag postHashtag = new PostHashtag(post, hashtag);
            post.addPostHashtag(postHashtag);


        }

        return postRepository.save(post);

    }

    public Post responsePost(PostResponseDto dto) {
        /*
        1. 조회
        2. 리턴
         */
        Post post = postRepository.findById(dto.getId())
                .orElseThrow(()-> new RuntimeException("Post not found"));
        return post;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
