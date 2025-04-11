package com.ceos21.ceos21BE.web.post.service.questionservice;

import com.ceos21.ceos21BE.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.web.hashtag.entity.Hashtag;
import com.ceos21.ceos21BE.web.hashtag.repository.HashtagRepository;
import com.ceos21.ceos21BE.web.image.entity.Image;
import com.ceos21.ceos21BE.web.post.converter.PostConverter;
import com.ceos21.ceos21BE.web.post.dto.request.CreatePostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.DeletePostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.UpdatePostRequest;
import com.ceos21.ceos21BE.web.post.dto.response.PostResponse;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.entity.PostType;
import com.ceos21.ceos21BE.web.post.handler.PostHandler;
import com.ceos21.ceos21BE.web.post.repository.PostRepository;
import com.ceos21.ceos21BE.web.posthashtag.entity.PostHashtag;
import com.ceos21.ceos21BE.web.posthashtag.repository.PostHashtagRepository;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final UserRepository userRepository;
    private final PostConverter converter;

    @Override
    @Transactional
    public PostResponse createPost(CreatePostRequest request, Long userId) {
        //validateCreatePostRequest(request);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new PostHandler(ErrorStatus._USER_NOT_FOUND));


        Post parent = null;
        if (request.getPostType() == PostType.ANSWER) {
            parent = postRepository.findById(request.getParentId())
                    .orElseThrow(() -> new PostHandler(ErrorStatus._QUESTION_NOT_FOUND));
        }

        Post post = converter.toPostEntity(request, user, parent);
        addHashtagsToPost(request.getHashtags(), post);
        addImagesToPost(request.getImageUrls(), post);

        postRepository.save(post);
        return converter.toPostResponse(post);
    }

    @Override
    @Transactional
    public PostResponse updatePost(UpdatePostRequest request, Long requestUserId, Long postId) {
        //validateUpdatePostRequest(request);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus._QUESTION_NOT_FOUND));

        // 만약 권한이 다르다면 제한함
        if (!post.getUser().getUserId().equals(requestUserId)) {
            throw new PostHandler(ErrorStatus._FORBIDDEN);
        }

        // 추가!! 답변이 달려있으면 수정 불가입니다.(질문글)
        if(post.getPostType() == PostType.QUESTION) {
            boolean hasAnswer = postRepository.existsByParent(post);
            if(hasAnswer) {
                throw new PostHandler(ErrorStatus._QUESTION_CANNOT_UPDATE_WITH_ANSWER);
            }
        }

        if (request.getTitle() == null || request.getContent() == null) {
            throw new IllegalArgumentException("제목과 본문은 필수입니다.");
        }

        post.updatePost(request.getTitle(), request.getContent());
        post.getImages().clear();
        post.getPostHashtags().clear();

        addImagesToPost(request.getImageUrls(), post);
        addHashtagsToPost(request.getHashtags(), post);

        postRepository.save(post);
        return converter.toPostResponse(post);

    }

    @Override
    @Transactional
    public void deletePost(DeletePostRequest request) {

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostHandler(ErrorStatus._POST_NOT_FOUND));

        if(!post.getUser().getUserId().equals(request.getUserId())) {
            throw new PostHandler(ErrorStatus._FORBIDDEN);
        }

        postRepository.delete(post);

    }

    @Override
    public List<PostResponse> getPostsByHashtag(String hashtagName) {
        Hashtag hashtag = hashtagRepository.findByName(hashtagName)
                .orElseThrow(() -> new PostHandler(ErrorStatus._HASHTAG_NOT_FOUND));

        List<PostHashtag> mappings = postHashtagRepository.findAllByHashtag(hashtag);

        return mappings.stream()
                .map(PostHashtag::getPost)
                .map(converter::toPostResponse)
                .toList();
    }

    @Override
    public List<PostResponse> getPostsByUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new PostHandler(ErrorStatus._USER_NOT_FOUND));

        return postRepository.findByUser(user).stream()
                .map(converter::toPostResponse)
                .toList();
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(converter::toPostResponse)
                .toList();
    }


    private void addHashtagsToPost(List<String> hashtags, Post post) {
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

    private void addImagesToPost(List<String> imageUrls, Post post) {
        for (String url : imageUrls) {
            if (url == null || url.isBlank()) continue;

            Image image = Image.builder()
                    .url(url)
                    .post(post)
                    .build();

            post.getImages().add(image);
        }
    }
}
