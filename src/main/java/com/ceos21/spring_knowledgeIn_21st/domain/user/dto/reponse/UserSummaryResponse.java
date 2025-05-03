package com.ceos21.spring_knowledgeIn_21st.domain.user.dto.reponse;

public record UserSummaryResponse(
        Long id,
        String name,
        String email
) {
}
