package com.ceos21.spring_boot.domain.users.dto;

import com.ceos21.spring_boot.domain.users.Users;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UsersResponse {

    private Long id;
    private String name;
    private String email;
    private String provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UsersResponse from(Users user) {
        UsersResponse response = new UsersResponse();
        response.id = user.getId();
        response.name = user.getName();
        response.email = user.getEmail();
        response.provider = user.getProvider();
        response.createdAt = user.getCreatedAt();
        response.updatedAt = user.getUpdatedAt();
        return response;
    }
}
