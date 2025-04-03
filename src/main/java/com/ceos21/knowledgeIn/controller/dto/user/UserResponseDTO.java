package com.ceos21.knowledgeIn.controller.dto.user;

import com.ceos21.knowledgeIn.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@AllArgsConstructor
@Builder
@Data
public class UserResponseDTO {
    private Long id;

    private String name;

    private String nickname;

    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .build();
    }
}
