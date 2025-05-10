package com.knowledgein.springboot.converter;

import com.knowledgein.springboot.domain.Post;
import com.knowledgein.springboot.domain.User;
import com.knowledgein.springboot.web.dto.postDTO.PostRequestDTO;
import com.knowledgein.springboot.web.dto.postDTO.PostResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {
    public static PostResponseDTO.ResultDto toResultDto(Post post) {
        return PostResponseDTO.ResultDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtagList(getHashtagList(post))
                .imageList(getImageList(post))
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
        Long questionId = (post.getQuestionPost() != null)
                ? post.getQuestionPost().getId()
                : null;

        return PostResponseDTO.PreviewDto.builder()
                .postId(post.getId())
                .userId(post.getUser().getId())
                .questionId(questionId)
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

    public static PostResponseDTO.UpdatedDto toUpdatedDto(Post post) {
        return PostResponseDTO.UpdatedDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hashtagList(getHashtagList(post))
                .imageList(getImageList(post))
                .build();
    }

    private static List<String> getHashtagList(Post post) {
        return post.getPostHashtagList().stream()
                .map(postHashtag -> postHashtag.getHashtag().getTag())
                .collect(Collectors.toList());
    }

    private static List<String> getImageList(Post post) {
        return post.getImageList().stream()
                .map(image -> image.getImageUrl())
                .collect(Collectors.toList());
    }

}
