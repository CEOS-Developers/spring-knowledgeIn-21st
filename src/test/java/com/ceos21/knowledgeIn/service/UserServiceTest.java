package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.user.UserCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.user.UserResponseDTO;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    EntityManager em;

    @Test
    void createUser() {
        //given
        UserCreateDTO dto = new UserCreateDTO("최근호", "olaf");
        //when
        Long userId = userService.createUser(dto);

        //then
        assertThat(userId).isNotNull();
    }

    @Test
    void findUser() {
        //given
        UserCreateDTO dto = new UserCreateDTO("최근호", "olaf");
        Long userId = userService.createUser(dto);

        //when
        UserResponseDTO findUserDTO = userService.findUser(userId);

        //then
        assertEquals(findUserDTO.getNickname(),dto.getNickname());
    }

    @Test
    void findAllUsers() {
        //given
        UserCreateDTO dto1 = new UserCreateDTO("최근호1", "olaf1");
        UserCreateDTO dto2 = new UserCreateDTO("최근호2", "olaf2");
        UserCreateDTO dto3 = new UserCreateDTO("최근호3", "olaf3");

        Long userId1 = userService.createUser(dto1);
        Long userId2 = userService.createUser(dto2);
        Long userId3 = userService.createUser(dto3);

        //when
        List<UserResponseDTO> allUsers = userService.findAllUsers();

        //then
        assertEquals(allUsers.size(),3);
    }

    @Test
    void deleteUser() {
        //given
        UserCreateDTO dto = new UserCreateDTO("최근호", "olaf");
        Long userId = userService.createUser(dto);

        //when
        userService.deleteUser(userId);
        List<UserResponseDTO> allUsers = userService.findAllUsers();

        //then
        assertThat(allUsers).isEmpty();
    }
}