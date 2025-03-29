package com.ceos21.spring_boot.Service;

import com.ceos21.spring_boot.DTO.AnswerDTO;
import com.ceos21.spring_boot.DTO.PostRequestDTO;
import com.ceos21.spring_boot.DTO.PostResponseDTO;
import com.ceos21.spring_boot.Domain.Hashtag;
import com.ceos21.spring_boot.Domain.Image;
import com.ceos21.spring_boot.Domain.Post;
import com.ceos21.spring_boot.Domain.User;
import com.ceos21.spring_boot.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final ImageRepository imageRepository;

    // 게시글 생성
    @Transactional
    public PostResponseDTO createPost(PostRequestDTO postCreateDTO, User user) {
        Post post = new Post(postCreateDTO.getTitle(), postCreateDTO.getContent(), user);
        postRepository.save(post);

        // 이미지 저장
        postCreateDTO.getImageUrls().forEach(url -> {
            Image image = new Image(url, post);
            imageRepository.save(image);
            post.getImages().add(image);
        });

        // 해시태그 처리
        postCreateDTO.getHashtags().forEach(tag -> {
            Hashtag hashtag = hashtagRepository.findByName(tag).orElseGet(() -> {
                Hashtag newHashtag = new Hashtag(tag);
                hashtagRepository.save(newHashtag);
                return newHashtag;
            });

            post.addHashtag(hashtag);
        });

        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getUsername())
                .createdAt(post.getCreatedAt().toString())
                .imageUrls(post.getImages().stream().map(Image::getPostImageUrl).collect(Collectors.toList()))
                .hashtags(post.getPostHashtags().stream().map(postHashtag -> postHashtag.getHashtag().getName()).collect(Collectors.toList()))
                .build();
    }

    // 특정 게시글 조회
    @Transactional
    public PostResponseDTO getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found"));

        List<AnswerDTO> answerDTOs = post.getAnswers().stream()
                .map(answer -> AnswerDTO.builder()
                        .id(answer.getId())
                        .content(answer.getContent())
                        .author(answer.getUser().getUsername())
                        .createdAt(answer.getCreatedAt().toString())
                        .imageUrls(answer.getImages().stream().map(Image::getPostImageUrl).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getUsername())
                .createdAt(post.getCreatedAt().toString())
                .imageUrls(post.getImages().stream().map(Image::getPostImageUrl).collect(Collectors.toList()))
                .hashtags(post.getPostHashtags().stream().map(postHashtag -> postHashtag.getHashtag().getName()).collect(Collectors.toList()))
                .answers(answerDTOs)
                .build();
    }

    // 전체 게시글 조회
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post ->
                PostResponseDTO.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getUser().getUsername())
                        .createdAt(post.getCreatedAt().toString())
                        .imageUrls(post.getImages().stream().map(Image::getPostImageUrl).collect(Collectors.toList()))
                        .hashtags(post.getPostHashtags().stream().map(postHashtag -> postHashtag.getHashtag().getName()).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());
    }

    // 게시글 수정
    @Transactional
    public PostResponseDTO updatePost(Long postId, PostRequestDTO postUpdateDTO, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Post updatedPost = Post.builder()
                .title(postUpdateDTO.getTitle())
                .content(postUpdateDTO.getContent())
                .user(post.getUser())
                .build();

        // 기존 게시글의 해시태그를 모두 제거
        updatedPost.getPostHashtags().clear();

        // 새 해시태그 처리
        postUpdateDTO.getHashtags().forEach(tag -> {
            Hashtag hashtag = hashtagRepository.findByName(tag).orElseGet(() -> {
                Hashtag newHashtag = new Hashtag(tag);
                hashtagRepository.save(newHashtag);
                return newHashtag;
            });
            updatedPost.addHashtag(hashtag);
        });

        // 이미지 처리
        postUpdateDTO.getImageUrls().forEach(url -> imageRepository.save(new Image(url, updatedPost)));

        // 게시글 업데이트 후 저장
        postRepository.save(updatedPost);

        // PostResponseDTO로 반환
        return PostResponseDTO.builder()
                .id(post.getId())
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .author(updatedPost.getUser().getUsername())
                .createdAt(updatedPost.getCreatedAt().toString())
                .imageUrls(updatedPost.getImages().stream().map(Image::getPostImageUrl).collect(Collectors.toList()))
                .hashtags(updatedPost.getPostHashtags().stream().map(postHashtag -> postHashtag.getHashtag().getName()).collect(Collectors.toList()))
                .build();
    }


    // 게시글 삭제
    @Transactional
    public String deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found"));

        postRepository.delete(post);
        return "게시글이 삭제되었습니다.";
    }
}
