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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    @Transactional
    public Post createPost(PostRequestDTO.CreateDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

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

        if (request.getImageList() != null) {
            for (String imageUrl : request.getImageList()) {
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
    public List<PostResponseDTO.resultDto> deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        List<PostResponseDTO.resultDto> deletedList = new ArrayList<>();

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
}
