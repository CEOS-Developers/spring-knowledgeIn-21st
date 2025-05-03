package com.ceos21.knowledgein.post.controller;


import com.ceos21.knowledgein.post.dto.ReplyDto;
import com.ceos21.knowledgein.post.dto.request.RequestCreateReply;
import com.ceos21.knowledgein.post.dto.request.RequestCreateReplyChildren;
import com.ceos21.knowledgein.post.service.ReplyService;
import com.ceos21.knowledgein.security.dto.PrincipalUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/post/v1/{postId}/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<ReplyDto> createReply(@PathVariable Long postId,
                                                @ModelAttribute @Valid RequestCreateReply requestCreateReply,
                                                @AuthenticationPrincipal PrincipalUserDetails currentUser) {

        Long userId = currentUser.getUserEntity().getId();

        ReplyDto result = replyService.createReply(postId, requestCreateReply, userId);
        return ResponseEntity.status(CREATED).body(result);
    }

    @PostMapping("/{replyId}")
    public ResponseEntity<ReplyDto> createReplyChildren(@PathVariable Long postId,
                                                        @PathVariable Long replyId,
                                                        @RequestBody @Valid RequestCreateReplyChildren requestCreateReply,
                                                        @AuthenticationPrincipal PrincipalUserDetails currentUser) {

        Long userId = currentUser.getUserEntity().getId();

        ReplyDto result = replyService.createReplyChildren(postId, replyId, requestCreateReply, userId);
        return ResponseEntity.status(CREATED).body(result);
    }

    @PatchMapping("/{replyId}")
    public ResponseEntity<ReplyDto> acceptReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            @AuthenticationPrincipal PrincipalUserDetails currentUser) {

        Long userId = currentUser.getUserEntity().getId();

        ReplyDto result = replyService.acceptReply(postId, replyId, userId);
        return ResponseEntity.status(OK).body(result);
    }


}
