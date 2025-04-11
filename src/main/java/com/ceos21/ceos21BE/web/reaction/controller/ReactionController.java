package com.ceos21.ceos21BE.web.reaction.controller;

import com.ceos21.ceos21BE.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.web.reaction.dto.request.ReactionRequest;
import com.ceos21.ceos21BE.web.reaction.dto.response.ReactionResponse;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.reaction.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reaction")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PutMapping("/post/{postId}")
    public ApiResponse<ReactionResponse> reactToPost(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestParam ReactionType reactionType) {
        ReactionRequest request = ReactionRequest.builder()
                .postId(postId)
                .userId(userId)
                .reactionType(reactionType)
                .build();
        return ApiResponse.onSuccess(reactionService.reactToPost(request));
    }

}
