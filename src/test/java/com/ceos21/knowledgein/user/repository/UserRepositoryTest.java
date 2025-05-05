package com.ceos21.knowledgein.user.repository;

import com.ceos21.knowledgein.user.domain.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 save 테스트")
    void saveUserTest() {
        // given
        UserEntity testUser = UserEntity.of("test@naver.com", "test-user", "test-nick", "1234");
        // when
        UserEntity savedUser = userRepository.save(testUser);
        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser).isEqualTo(testUser);
        assertThat(savedUser.getName()).isEqualTo(testUser.getName());
    }


}