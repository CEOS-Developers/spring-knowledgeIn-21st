package com.ceos21.knowledgeIn.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequestDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberJoinDTO{
        private String name;
        private String email;
        private String password;
        private String nickname;
        private String phone;
    }
}
