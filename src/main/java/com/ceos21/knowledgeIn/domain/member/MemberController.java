package com.ceos21.knowledgeIn.domain.member;

import com.ceos21.knowledgeIn.domain.member.dto.MemberRequestDTO;
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


    @PostMapping("/join")
    public String join(@RequestBody MemberRequestDTO.MemberJoinDTO requestDTO) {

        return memberService.memberJoin(requestDTO);

    }
}
