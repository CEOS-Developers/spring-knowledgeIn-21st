package com.ceos21.knowledgein.post.service;

import com.ceos21.knowledgein.post.domain.HashTag;
import com.ceos21.knowledgein.post.domain.Image;
import com.ceos21.knowledgein.post.domain.Post;
import com.ceos21.knowledgein.post.domain.Reply;
import com.ceos21.knowledgein.post.dto.ReplyDto;
import com.ceos21.knowledgein.post.dto.request.RequestCreateReply;
import com.ceos21.knowledgein.post.repository.ReplyRepository;
import com.ceos21.knowledgein.user.domain.UserEntity;
import com.ceos21.knowledgein.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public ReplyDto createReply(Long postId, RequestCreateReply request, Long userId) {

        Post post = postService.findPostOrThrow(postId);
        List<Image> images = postService.storeImage(request.images());
        List<HashTag> hashTags = postService.createHashTagEntity(request.hashTags());
        UserEntity user = userService.findUserByIdReturnEntity(userId);

        Reply reply = Reply.createWithNoParent(
                request.content(),
                user,
                post,
                images
        );

        replyRepository.save(reply);
        return ReplyDto.from(reply);
    }
}
