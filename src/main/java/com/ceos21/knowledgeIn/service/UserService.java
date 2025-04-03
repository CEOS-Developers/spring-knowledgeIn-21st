package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.user.UserCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.user.UserResponseDTO;
import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    //create
    @Transactional
    public Long createUser(UserCreateDTO dto) {
        User entity = User.builder()
                .name(dto.getName())
                .nickname(dto.getNickname())
                .build();

        return userRepository.save(entity).getId();
    }

    //read
    public UserResponseDTO findUser(Long id) {
        User findUser = userRepository.findById(id).orElseThrow(()->new RuntimeException("cannot find user by id"));

        return UserResponseDTO.from(findUser);
    }

    public List<UserResponseDTO> findAllUsers() {
        List<UserResponseDTO> dtos = userRepository.findAll()
                .stream()
                .map(user->UserResponseDTO.from(user))
                .collect(Collectors.toList());

        return dtos;
    }

    //update

    //delete
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
