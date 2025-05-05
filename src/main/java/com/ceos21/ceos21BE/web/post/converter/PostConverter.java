package com.ceos21.ceos21BE.web.post.converter;

import com.ceos21.ceos21BE.web.image.entity.Image;
import com.ceos21.ceos21BE.web.post.dto.request.CreatePostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.UpdatePostRequest;
import com.ceos21.ceos21BE.web.post.dto.response.PostResponse;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.entity.PostType;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PostConverter {

    public Post toPostEntity(CreatePostRequest request, UserEntity user, Post parent) {

        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .postType(request.getPostType())
                .parent(parent)
                .build(); // parent, hashtags, images는 서비스에서 처리
    }


    public PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
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
}
