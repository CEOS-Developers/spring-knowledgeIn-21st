package com.ceos21.spring_boot.domain.users;

import com.ceos21.spring_boot.domain.users.dto.UsersRequest;
import com.ceos21.spring_boot.domain.users.dto.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping
    public UsersResponse createUser(@RequestBody UsersRequest request) {
        return UsersResponse.from(usersService.createUser(request.toEntity()));
    }

    @GetMapping
    public List<UsersResponse> getAllUsers() {
        return usersService.getAllUsers()
                .stream()
                .map(UsersResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UsersResponse getUserById(@PathVariable Long id) {
        return UsersResponse.from(usersService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
    }
}
