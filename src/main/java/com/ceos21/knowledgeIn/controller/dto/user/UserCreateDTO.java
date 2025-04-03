package com.ceos21.knowledgeIn.controller.dto.user;

import com.ceos21.knowledgeIn.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateDTO {

    private String name;

    private String nickname;

}
