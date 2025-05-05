package com.ceos21.spring_boot.Service.post;

import com.ceos21.spring_boot.DTO.answer.AnswerDTO;
import com.ceos21.spring_boot.DTO.post.PostRequestDTO;
import com.ceos21.spring_boot.DTO.post.PostResponseDTO;
import com.ceos21.spring_boot.DTO.post.PostResponseWithAnswerDTO;
import com.ceos21.spring_boot.Domain.post.Hashtag;
import com.ceos21.spring_boot.Domain.post.Image;
import com.ceos21.spring_boot.Domain.post.Post;
import com.ceos21.spring_boot.Domain.user.User;
import com.ceos21.spring_boot.Repository.post.HashtagRepository;
import com.ceos21.spring_boot.Repository.post.ImageRepository;
import com.ceos21.spring_boot.Repository.post.PostRepository;
import com.ceos21.spring_boot.Service.answer.AnswerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final ImageRepository imageRepository;
    private final AnswerService answerService;

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

        return PostResponseDTO.from(
                post,
                post.getImages().stream().map(Image::getPostImageUrl).toList(),
                post.getPostHashtags().stream().map(ph -> ph.getHashtag().getName()).toList()
        );
    }

    // 특정 게시글 조회 (답변 포함)
    @Transactional(readOnly = true)
    public PostResponseWithAnswerDTO getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found"));

        List<String> imageUrls = post.getImages().stream()
                .map(Image::getPostImageUrl)
                .toList();

        List<String> hashtags = post.getPostHashtags().stream()
                .map(ph -> ph.getHashtag().getName())
                .toList();

        List<AnswerDTO> answers = answerService.getAnswersByPostId(id);

        return PostResponseWithAnswerDTO.from(post, imageUrls, hashtags, answers);
    }

    // 전체 게시글 조회 - 로그인한 사용자가 모두 볼 수 있음
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post ->
                PostResponseDTO.from(
                        post,
                        post.getImages().stream().map(Image::getPostImageUrl).toList(),
                        post.getPostHashtags().stream().map(ph -> ph.getHashtag().getName()).toList()
                )
        ).collect(Collectors.toList());
    }

    // 자신이 쓴 전체 게시글 조회
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getMyPosts(User user) {
        List<Post> posts = postRepository.findAllByUserId(user.getId());

        return posts.stream().map(post ->
                PostResponseDTO.from(
                        post,
                        post.getImages().stream().map(Image::getPostImageUrl).toList(),
                        post.getPostHashtags().stream().map(h -> h.getHashtag().getName()).toList()
                )
        ).collect(Collectors.toList());
    }

    // 게시글 수정
    @Transactional
    public PostResponseDTO updatePost(Long postId, PostRequestDTO postUpdateDTO, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        // 작성자 본인인지 확인
        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("게시글 수정 권한이 없습니다.");
        }

        // 도메인 메서드를 통한 의도 있는 변경
        post.updatePost(postUpdateDTO.getTitle(), postUpdateDTO.getContent());

        // 기존 해시태그 제거
        post.getPostHashtags().clear();

        // 새로운 해시태그 추가
        postUpdateDTO.getHashtags().forEach(tag -> {
            Hashtag hashtag = hashtagRepository.findByName(tag)
                    .orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag(tag);
                        hashtagRepository.save(newHashtag);
                        return newHashtag;
                    });
            post.addHashtag(hashtag); // 관계 설정 메서드
        });

        // 기존 이미지 제거
        post.getImages().clear();

        // 새로운 이미지 저장 및 연결
        postUpdateDTO.getImageUrls().forEach(url -> {
            Image image = new Image(url, post);
            imageRepository.save(image);
            post.getImages().add(image);
        });

        // 반환 DTO 구성
        return PostResponseDTO.from(
                post,
                post.getImages().stream().map(Image::getPostImageUrl).toList(),
                post.getPostHashtags().stream().map(ph -> ph.getHashtag().getName()).toList()
        );
    }

    // 게시글 삭제
    @Transactional
    public String deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found"));

        // 작성자 본인인지 확인
        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("게시글 수정 권한이 없습니다.");
        }

        postRepository.delete(post);
        return "게시글이 삭제되었습니다.";
    }
}