package com.ceos21.ceos21BE.web.post.converter;

import com.ceos21.ceos21BE.web.post.entity.Image;
import com.ceos21.ceos21BE.web.post.dto.request.CreatePostRequestDto;
import com.ceos21.ceos21BE.web.post.dto.response.AnswerSummaryResponseDto;
import com.ceos21.ceos21BE.web.post.dto.response.PostResponseDto;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.entity.PostType;
import com.ceos21.ceos21BE.web.post.entity.PostHashtag;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PostConverter {

    public Post toPostEntity(CreatePostRequestDto request, User user, Post parent) {

        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .postType(request.getPostType())
                .parent(parent)
                .build(); // parent, hashtags, images는 서비스에서 처리
    }


    public PostResponseDto toPostResponse(Post post) {
        return PostResponseDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUser().getUserId())
                .username(post.getUser().getUsername())
                .hashtags(Optional.ofNullable(post.getPostHashtags())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(ph -> ph.getHashtag().getName())
                        .toList())
                .imageUrls(Optional.ofNullable(post.getImages())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(Image::getUrl).toList())
                .postType(post.getPostType())
                .parentId(post.getPostType() == PostType.ANSWER ? post.getParent().getPostId() : null)
                .build();
    }


    public AnswerSummaryResponseDto toAnswerSummaryDto(Post answer) {
        int likeCount = (int) answer.getReactions().stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.LIKE)
                .count();

        int dislikeCount = (int) answer.getReactions().stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.DISLIKE)
                .count();

        return AnswerSummaryResponseDto.builder()
                .postId(answer.getPostId())
                .content(answer.getContent())
                .username(answer.getUser().getUsername())
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .build();
    }

    private List<AnswerSummaryResponseDto> toAnswerSummaries(List<Post> answers) {
        if(answers == null || answers.isEmpty()) {
            return Collections.emptyList();
        }

        // 답변이 삭제된 경우를 제외하고 AnswerSummaryResponseDto로 변환
        return answers.stream()
                .filter(answer -> !answer.isDeleted())
                .map(this::toAnswerSummaryDto)
                .collect(Collectors.toList());
    }

    private List<String> toImageUrls(List<Image> images) {
        return images.stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
    }

    private List<String> toHashtagNames(List<PostHashtag> postHashtags) {
        return postHashtags.stream()
                .map(postHashtag -> postHashtag.getHashtag().getName())
                .collect(Collectors.toList());
    }
}
