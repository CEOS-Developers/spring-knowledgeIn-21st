package com.ceos21.knowledgein.repository;

import com.ceos21.knowledgein.domain.Role;
import com.ceos21.knowledgein.domain.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ceos21.knowledgein.domain.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 save 테스트")
    void saveUserTest() {
        // given
        UserEntity testUser = UserEntity.builder()
                .name("test-user")
                .nickName("test-nick")
                .passWord("1234")
                .role(USER)
                .build();
        // when
        UserEntity savedUser = userRepository.save(testUser);
        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser).isEqualTo(testUser);
        assertThat(savedUser.getName()).isEqualTo(testUser.getName());
    }


}