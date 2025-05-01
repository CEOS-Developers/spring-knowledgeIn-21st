package com.ceos21.ceos21BE.jwt.dto;

public record JwtDto (
        String accessToken,
        String refreshToken
) {
}
