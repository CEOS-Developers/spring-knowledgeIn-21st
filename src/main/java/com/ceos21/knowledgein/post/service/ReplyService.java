package com.ceos21.knowledgein.post.service;

import com.ceos21.knowledgein.post.domain.HashTag;
import com.ceos21.knowledgein.post.domain.Image;
import com.ceos21.knowledgein.post.domain.Post;
import com.ceos21.knowledgein.post.domain.Reply;
import com.ceos21.knowledgein.post.dto.ReplyDto;
import com.ceos21.knowledgein.post.dto.request.RequestCreateReply;
import com.ceos21.knowledgein.post.dto.request.RequestCreateReplyChildren;
import com.ceos21.knowledgein.post.exception.PostException;
import com.ceos21.knowledgein.post.repository.ReplyRepository;
import com.ceos21.knowledgein.user.domain.UserEntity;
import com.ceos21.knowledgein.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ceos21.knowledgein.post.exception.PostErrorCode.REPLY_NOT_FOUND;

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

        Reply reply = createNewReplyEntity(request, user, post, images);
        replyRepository.save(reply);

        return ReplyDto.from(reply);
    }

    @Transactional
    public ReplyDto createReplyChildren(Long postId, Long replyId, RequestCreateReplyChildren requestCreateReply, Long userId) {

        Post post = postService.findPostOrThrow(postId);
        UserEntity user = userService.findUserByIdReturnEntity(userId);
        Reply parent = findReplyOrThrow(replyId);

        Reply reply = createReplyEntityWithParent(requestCreateReply, user, post, parent);
        replyRepository.save(reply);

        return ReplyDto.from(reply);
    }


    private Reply createReplyEntityWithParent(RequestCreateReplyChildren requestCreateReply, UserEntity user, Post post, Reply parent) {
        return Reply.createWithParent(
                requestCreateReply.content(),
                user,
                post,
                parent
        );
    }

    private Reply findReplyOrThrow(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new PostException(REPLY_NOT_FOUND));
    }

    private Reply createNewReplyEntity(RequestCreateReply request, UserEntity user, Post post, List<Image> images) {
        return Reply.createWithNoParent(
                request.content(),
                user,
                post,
                images
        );
    }


}
