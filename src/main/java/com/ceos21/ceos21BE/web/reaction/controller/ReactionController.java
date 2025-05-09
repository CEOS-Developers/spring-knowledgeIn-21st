package com.ceos21.ceos21BE.web.reaction.controller;

import com.ceos21.ceos21BE.customDetail.CustomDetails;
import com.ceos21.ceos21BE.global.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.global.apiPayload.code.status.SuccessStatus;
import com.ceos21.ceos21BE.web.reaction.dto.response.ReactionResponseDTO;
import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.reaction.service.ReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    // 좋아요 싫어요 등록 및 삭제 요청
    // 리액션이 존재하는 경우 1. 타입이 다르면 업데이트 2. 타입이 같으면 삭제
    // 리액션이 없는 경우 새로 생성
    @PostMapping("/posts/{postId}/reactions")
    public ApiResponse<?> createReaction(
            @PathVariable Long postId,
            @RequestBody @Valid ReactionType type,
            @AuthenticationPrincipal CustomDetails user
            ) {
        ReactionResponseDTO response =  reactionService.handleReaction(postId, user.getUser().getUserId(), type);

        if(response == null) {
            return ApiResponse.of(SuccessStatus._DELETED, null);
        } else {
            return ApiResponse.onSuccess(response);
        }
    }

}
