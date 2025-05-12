package com.ceos21.knowledgeIn.domain.user;

import com.ceos21.knowledgeIn.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@SpringBootTest
class UserTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void createUser() {
        //given
        User user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();

        //when
        Long id = userRepository.save(user).getId();

        //then
        Optional<User> findUser = userRepository.findById(id);
        Assertions.assertThat(findUser).isPresent();
    }
    @Test
    void deleteUser() {
        //given
        User user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();

        //when
        Long id = userRepository.save(user).getId();
        userRepository.deleteById(id);

        //then
        Optional<User> findUser = userRepository.findById(id);
        Assertions.assertThat(findUser).isEmpty();
    }


}