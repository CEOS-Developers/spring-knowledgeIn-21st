package com.ceos21.knowledgeIn.domain.member;

import com.ceos21.knowledgeIn.domain.member.dto.MemberRequestDTO;
import com.ceos21.knowledgeIn.domain.member.dto.MemberResponseDTO;
import com.ceos21.knowledgeIn.domain.member.service.MemberService;
import com.ceos21.knowledgeIn.global.exceptionHandler.ApiResponse;
import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import com.ceos21.knowledgeIn.global.exceptionHandler.Status;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name="회원")
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;


    @PostMapping("/join")
    public ApiResponse<MemberResponseDTO.MemberInfoDTO> join(@RequestBody MemberRequestDTO.MemberJoinDTO requestDTO) {

        boolean isMember = memberRepository.existsByEmail(requestDTO.getEmail());
        if(isMember) {
            throw new GeneralException(Status.MEMBER_ALREADY_EXISTS);
        }
        Member member = memberService.memberJoin(requestDTO);

        MemberResponseDTO.MemberInfoDTO body = MemberResponseDTO.from(member);

        return ApiResponse.onSuccess(body);

    }
}
