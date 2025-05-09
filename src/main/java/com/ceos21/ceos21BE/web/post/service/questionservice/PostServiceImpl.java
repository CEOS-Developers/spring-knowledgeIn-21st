package com.ceos21.ceos21BE.web.post.service.questionservice;

import com.ceos21.ceos21BE.global.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.web.post.service.HashtagService;
import com.ceos21.ceos21BE.web.post.service.ImageService;
import com.ceos21.ceos21BE.web.post.converter.PostConverter;
import com.ceos21.ceos21BE.web.post.dto.request.CreatePostRequestDto;
import com.ceos21.ceos21BE.web.post.dto.request.DeletePostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.UpdatePostRequestDto;
import com.ceos21.ceos21BE.web.post.dto.response.AnswerSummaryResponseDto;
import com.ceos21.ceos21BE.web.post.dto.response.PostResponseDto;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.entity.PostType;
import com.ceos21.ceos21BE.web.post.handler.PostHandler;
import com.ceos21.ceos21BE.web.post.repository.PostRepository;
import com.ceos21.ceos21BE.web.user.entity.User;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import com.ceos21.ceos21BE.web.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final HashtagService hashTagService;
    private final ImageService imageService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PostConverter converter;

    @Override
    @Transactional
    public PostResponseDto createPost(CreatePostRequestDto request, Long userId) {

        User user = userService.validateUser(userId);


        Post parent = null;
        if (request.getPostType() == PostType.ANSWER) {
            parent = validatePost(request.getParentId());
        }

        Post post = converter.toPostEntity(request, user, parent);

        hashTagService.addHashtagsToPost(request.getHashtags(), post);
        imageService.addImagesToPost(request.getImageUrls(), post);

        postRepository.save(post);
        return converter.toPostResponse(post);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(UpdatePostRequestDto request, Long requestUserId, Long postId) {

        Post post = validatePost(postId);

        validateOwner(post, requestUserId);

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

        hashTagService.addHashtagsToPost(request.getHashtags(), post);
        imageService.addImagesToPost(request.getImageUrls(), post);

        // postRepository.save(post); // JPA가 자동으로 변경 감지하여 업데이트합니다.
        return converter.toPostResponse(post);

    }

    @Override
    @Transactional
    public void deletePost(DeletePostRequest request, Long userId) {

        Post post = validatePost(request.getPostId());

        validateOwner(post, userId);

        postRepository.delete(post);

    }

    @Override
    public Page<PostResponseDto> getPostList(PostType type, Pageable pageable) {
        return postRepository.findAllByPostType(type, pageable)
                .map(converter::toPostResponse);
    }


    @Override
    public List<PostResponseDto> getPostsByUser(Long userId) {
        User user = userService.validateUser(userId);

        return postRepository.findByUser(user).stream()
                .map(converter::toPostResponse)
                .toList();
    }

    @Override
    public PostResponseDto getPost(Long postId) {
        Post post = validatePost(postId);
        return converter.toPostResponse(post);
    }

    @Override
    public List<AnswerSummaryResponseDto> getAnswersByQuestionId(Long questionId) {
        Post question = postRepository.findById(questionId)
                .orElseThrow(() -> new PostHandler(ErrorStatus._POST_NOT_FOUND));

        return question.getAnswers()
                .stream()
                .map(converter::toAnswerSummaryDto)
                .toList();
    }


    @Override
    public Post validatePost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus._POST_NOT_FOUND));
    }

    protected void validateOwner(Post post, Long userId) {
        if (!post.getUser().getUserId().equals(userId)) {
            throw new PostHandler(ErrorStatus._FORBIDDEN);
        }
    }
}
