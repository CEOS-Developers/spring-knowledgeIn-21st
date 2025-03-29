package com.ceos21.knowledgein.post.dto;

import com.ceos21.knowledgein.post.domain.Post;

import java.util.List;

public record PostDto(
    String title,
    String content,
    Long viewCount,
    boolean nicknamePublic,
    List<ImageDto> images,
    List<HashTagDto> hashTags,
    List<ReplyDto> replies
) {

    public static List<PostDto> from(List<Post> posts) {
        return posts.stream()
            .map(PostDto::from)
            .toList();
    }

    public static PostDto from(Post post) {
        return new PostDto(
            post.getTitle(),
            post.getContent(),
            post.getViewCount(),
            post.isNicknamePublic(),
            ImageDto.from(post.getImages()
                                  .stream()
                                  .filter(image -> image.getReply() == null)
                                  .toList()
            ),
            HashTagDto.from(post.getHashTags()),
            ReplyDto.from(post.getReplies())
        );
    }
}
