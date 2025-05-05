package com.ceos21.knowledgeIn.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
