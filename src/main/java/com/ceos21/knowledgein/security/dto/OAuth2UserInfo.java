package com.ceos21.knowledgein.security.dto;

import com.ceos21.knowledgein.security.exception.SecurityImplementException;
import com.ceos21.knowledgein.user.domain.UserEntity;
import lombok.Builder;

import java.util.Map;

import static com.ceos21.knowledgein.security.exception.SecurityImplementErrorCode.ILLEGAL_REGISTRATION_ID;

@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String imageUrl
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {

        if (registrationId.equalsIgnoreCase("google")) {
            return ofGoogle(attributes);
        }


        throw new SecurityImplementException(ILLEGAL_REGISTRATION_ID);
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .imageUrl((String) attributes.get("picture"))
                .build();
    }


    public UserEntity toEntity() {
        return UserEntity.of(
                email,
                name,
                email,
                null
        );
    }

}
