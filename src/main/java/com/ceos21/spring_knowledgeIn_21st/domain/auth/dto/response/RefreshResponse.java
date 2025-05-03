package com.ceos21.spring_knowledgeIn_21st.domain.auth.dto.response;

public class RefreshResponse {
    private final String accessToken;

    public RefreshResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
