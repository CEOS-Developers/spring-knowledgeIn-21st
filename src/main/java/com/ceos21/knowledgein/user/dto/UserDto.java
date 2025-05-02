package com.ceos21.knowledgein.user.dto;

import com.ceos21.knowledgein.security.dto.PrincipalUserDetails;
import com.ceos21.knowledgein.user.domain.UserEntity;
import org.springframework.security.core.Authentication;

public record UserDto(
        String email,
        String name,
        String nickName
) {

    public static UserDto from(UserEntity user) {
        return new UserDto(
                user.getEmail(),
                user.getName(),
                user.getNickName()
        );
    }

    public static UserDto from(Authentication authentication) {
        PrincipalUserDetails principalUserDetails = (PrincipalUserDetails) authentication.getPrincipal();
        UserEntity user = principalUserDetails.getUserEntity();
        return new UserDto(
                user.getEmail(),
                user.getName(),
                user.getNickName()
        );
    }
}
