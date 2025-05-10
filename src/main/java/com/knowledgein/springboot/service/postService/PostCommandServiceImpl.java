package com.knowledgein.springboot.service.postService;

import com.knowledgein.springboot.apiPayload.code.status.ErrorStatus;
import com.knowledgein.springboot.apiPayload.exception.GeneralException;
import com.knowledgein.springboot.converter.HashtagConverter;
import com.knowledgein.springboot.converter.ImageConverter;
import com.knowledgein.springboot.converter.PostConverter;
import com.knowledgein.springboot.converter.PostHashtagConverter;
import com.knowledgein.springboot.domain.Hashtag;
import com.knowledgein.springboot.domain.Image;
import com.knowledgein.springboot.domain.Post;
import com.knowledgein.springboot.domain.User;
import com.knowledgein.springboot.domain.enums.PostType;
import com.knowledgein.springboot.domain.mapping.PostHashtag;
import com.knowledgein.springboot.repository.HashtagRepository;
import com.knowledgein.springboot.repository.PostRepository;
import com.knowledgein.springboot.repository.UserRepository;
import com.knowledgein.springboot.web.dto.postDTO.PostRequestDTO;
import com.knowledgein.springboot.web.dto.postDTO.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    @Transactional
    public Post createPost(PostRequestDTO.CreateDto request, User user, List<String> imageUrls) {
        Post question = null;

        // postType == PostType.QUESTION -> questionId is null
        // postType == PostType.ANSWER -> questionId is not null & should exist
        if (request.getPostType() == PostType.QUESTION) {
            if (request.getQuestionId() != null) throw new GeneralException(ErrorStatus.QUESTION_SHOULD_NOT_EXIST);
        } else {
            // request.getPostType() == PostType.ANSWER
            if (request.getQuestionId() == null) throw new GeneralException(ErrorStatus.QUESTION_SHOULD_EXIST);
            question = postRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.PARENT_QUESTION_NOT_FOUND));
            if (question.getPostType() != PostType.QUESTION) throw new GeneralException(ErrorStatus.PARENT_IS_A_QUESTION);
        }

        Post newPost = PostConverter.toPost(request, user, question);

        if (request.getHashtagList() != null) {
            for (String tag : request.getHashtagList()) {
                Hashtag hashtag = hashtagRepository.findByTag(tag)
                        .orElseGet(() -> hashtagRepository.save(HashtagConverter.toHashtag(tag)));

                PostHashtag postHashtag = PostHashtagConverter.toPostHashtag(newPost, hashtag);
                newPost.addPostHashtag(postHashtag);
            }
        }

        if (imageUrls != null) {
            for (String imageUrl : imageUrls) {
                Image image = ImageConverter.toImage(newPost, imageUrl);
                newPost.addImage(image);
            }
        }

        if (request.getPostType() == PostType.ANSWER) {
            question.addAnswerPost(newPost);
        }

        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public List<PostResponseDTO.ResultDto> deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if (!Objects.equals(user, post.getUser())) throw new GeneralException(ErrorStatus.USER_NOT_AUTHORIZED);

        List<PostResponseDTO.ResultDto> deletedList = new ArrayList<>();

        if (post.getPostType() == PostType.QUESTION) {
            for (Post answer: post.getAnswerPostList()) {
                deletedList.add(PostConverter.toResultDto(answer));
                postRepository.delete(answer);
            }
        }

        deletedList.add(PostConverter.toResultDto(post));
        postRepository.delete(post);

        return deletedList;
    }

    @Override
    @Transactional
    public Post updatePost(Long postId, User user, PostRequestDTO.UpdateDto request, List<String> imageUrls) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if (!Objects.equals(user, post.getUser())) throw new GeneralException(ErrorStatus.USER_NOT_AUTHORIZED);

        // For orphan removal
        List<Hashtag> originalHashtags = post.getPostHashtagList().stream()
                .map(PostHashtag::getHashtag)
                .collect(Collectors.toList());

        // Possibly updated fields: title, content, hashtags, images
        if (request.getTitle() != null) { post.updateTitle(request.getTitle()); }
        if (request.getContent() != null) { post.updateContent(request.getContent()); }
        if (request.getHashtagList() != null) {
            post.clearPostHashtagList();
            for (String tag: request.getHashtagList()) {
                Hashtag hashtag = hashtagRepository.findByTag(tag)
                        .orElseGet(() -> hashtagRepository.save(HashtagConverter.toHashtag(tag)));

                PostHashtag postHashtag = PostHashtagConverter.toPostHashtag(post, hashtag);
                post.addPostHashtag(postHashtag);
            }

            // Orphan removal
            Set<String> updatedTagSet = new HashSet<>(Optional.ofNullable(request.getHashtagList()).orElse(List.of()));

            for (Hashtag oldTag : originalHashtags) {
                if (!updatedTagSet.contains(oldTag.getTag())) {
                    if (oldTag.getPostHashtagList().size() <= 1) {
                        hashtagRepository.delete(oldTag);
                    }
                }
            }
        }
        if (imageUrls != null) {
            if (post.getImageList() != null) post.clearImageList();

            for (String imageUrl: imageUrls) {
                Image image = ImageConverter.toImage(post, imageUrl);
                post.addImage(image);
            }
        }

        return post;
    }
}
