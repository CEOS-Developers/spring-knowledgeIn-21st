package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.auth.SignUpDTO;
import com.ceos21.knowledgeIn.controller.dto.user.UserResponseDTO;
import com.ceos21.knowledgeIn.service.user.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    EntityManager em;

    @Test
    void signUp() {
        //given
        SignUpDTO dto = SignUpDTO.builder()
                .name("gno")
                .nickname("gnonick")
                .username("asdf")
                .password("asdf")
                .build();
        //when
        Long userId = userService.signUp(dto);

        //then
        assertThat(userId).isNotNull();
    }

    @Test
    void findUser() {
        //given
        SignUpDTO dto = SignUpDTO.builder()
                .name("gno")
                .nickname("gnonick")
                .username("asdf")
                .password("asdf")
                .build();

        Long userId = userService.signUp(dto);

        //when
        UserResponseDTO findUserDTO = userService.findUser(userId);

        //then
        assertEquals(findUserDTO.getNickname(),dto.getNickname());
    }

    @Test
    void findAllUsers() {
        //given
        SignUpDTO dto1 = SignUpDTO.builder()
                .name("gno")
                .nickname("gnonick")
                .username("asdf")
                .password("asdf")
                .build();
        SignUpDTO dto2 = SignUpDTO.builder()
                .name("gno2")
                .nickname("gnonick")
                .username("asdf2")
                .password("asdf")
                .build();
        SignUpDTO dto3 = SignUpDTO.builder()
                .name("gno3")
                .nickname("gnonick")
                .username("asdf3")
                .password("asdf")
                .build();
        Long userId1 = userService.signUp(dto1);
        Long userId2 = userService.signUp(dto2);
        Long userId3 = userService.signUp(dto3);

        //when
        List<UserResponseDTO> allUsers = userService.findAllUsers();

        //then
        assertEquals(allUsers.size(),3);
    }

    @Test
    void deleteUser() {
        //given
        SignUpDTO dto = SignUpDTO.builder()
                .name("gno")
                .nickname("gnonick")
                .username("asdf")
                .password("asdf")
                .build();
        Long userId = userService.signUp(dto);

        //when
        userService.deleteUser(userId);
        List<UserResponseDTO> allUsers = userService.findAllUsers();

        //then
        assertThat(allUsers).isEmpty();
    }
}