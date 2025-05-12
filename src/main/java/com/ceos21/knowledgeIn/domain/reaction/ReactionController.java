package com.ceos21.knowledgeIn.domain.reaction;

import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.member.service.CustomUserDetailService;
import com.ceos21.knowledgeIn.domain.reaction.domain.Reaction;
import com.ceos21.knowledgeIn.domain.reaction.domain.ReactionType;
import com.ceos21.knowledgeIn.domain.reaction.dto.ReactionResponseDTO;
import com.ceos21.knowledgeIn.global.exceptionHandler.ApiResponse;
import com.ceos21.knowledgeIn.global.exceptionHandler.Status;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reaction")
@RequiredArgsConstructor
@Tag(name = "반응")
public class ReactionController {

    private final ReactionService reactionService;
    private final CustomUserDetailService customUserDetailService;

    @GetMapping("/reaction/{postId}")
    public ApiResponse<ReactionResponseDTO.reactionResultDTO> like(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails, @RequestParam ReactionType reactionType) {

        Member member = customUserDetailService.findMemberByUserDetails(userDetails);

        Reaction reaction = reactionService.like(member,postId,reactionType);
        ReactionResponseDTO.reactionResultDTO body = ReactionResponseDTO.reactionResultDTO.builder().reactionType(reaction.getReactionType()).build();

        return ApiResponse.onSuccess(body);
    }

}
