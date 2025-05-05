package com.ceos21.knowledgeIn.domain.member.dto;

import com.ceos21.knowledgeIn.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class MemberResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfoDTO {
        private Long id;
        private String name;
        private String nickname;
        private String email;
        private String phone;

    }

    public static MemberInfoDTO from(Member member){

        return MemberInfoDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .phone(member.getPhone())
                .build();

    }
}
