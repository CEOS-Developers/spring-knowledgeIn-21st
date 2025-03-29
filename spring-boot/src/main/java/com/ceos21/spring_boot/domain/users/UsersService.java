package com.ceos21.spring_boot.domain.users;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users getUserById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 유저가 없습니다 : " + id));
    }

    public void deleteUser(Long id) {
        Users user = getUserById(id);
        usersRepository.delete(user);
    }
}
