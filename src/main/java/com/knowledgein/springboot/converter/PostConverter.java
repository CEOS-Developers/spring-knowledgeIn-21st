package com.knowledgein.springboot.converter;

import com.knowledgein.springboot.domain.Post;
import com.knowledgein.springboot.domain.User;
import com.knowledgein.springboot.web.dto.postDTO.PostRequestDTO;
import com.knowledgein.springboot.web.dto.postDTO.PostResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {
    public static PostResponseDTO.resultDto toResultDto(Post post) {
        return PostResponseDTO.resultDto.builder()
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static Post toPost(PostRequestDTO.CreateDto request, User user, Post question) {
        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .postType(request.getPostType())
                .user(user)
                .questionPost(question)
                .build();
    }

    public static PostResponseDTO.PreviewDto toPreviewDto(Post post) {
        return PostResponseDTO.PreviewDto.builder()
                .postId(post.getId())
                .userId(post.getUser().getId())
                .questionId(post.getQuestionPost().getId())
                .postType(post.getPostType())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .build();
    }


    public static PostResponseDTO.PreviewListDto toPreviewListDto(Page<Post> postPage) {
        List<PostResponseDTO.PreviewDto> postPreviewItems = postPage.stream()
                .map(item -> toPreviewDto(item))
                .collect(Collectors.toList());

        return PostResponseDTO.PreviewListDto.builder()
                .previewList(postPreviewItems)
                .listSize(postPage.getContent().size())
                .totalPage(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .isFirst(postPage.isFirst())
                .isLast(postPage.isLast())
                .build();
    }


}
