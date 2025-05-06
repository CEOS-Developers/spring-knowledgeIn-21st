package com.ceos21.spring_boot.domain.users;


// import com.ceos21.spring_boot.domain.users.dto.UsersRequest;
// import com.ceos21.spring_boot.domain.users.dto.UsersResponse;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.*;

import com.ceos21.spring_boot.domain.users.dto.*;
import com.ceos21.spring_boot.config.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/users")
// @RequiredArgsConstructor
// public class UsersController {

//     private final UsersService usersService;

//     @PostMapping
//     public UsersResponse createUser(@RequestBody UsersRequest request) {
//         return UsersResponse.from(usersService.createUser(request.toEntity()));
//     }

//     @GetMapping
//     public List<UsersResponse> getAllUsers() {
//         return usersService.getAllUsers()
//                 .stream()
//                 .map(UsersResponse::from)
//                 .collect(Collectors.toList());
//     }

//     @GetMapping("/{id}")
//     public UsersResponse getUserById(@PathVariable Long id) {
//         return UsersResponse.from(usersService.getUserById(id));
//     }

//     @DeleteMapping("/{id}")
//     public void deleteUser(@PathVariable Long id) {
//         usersService.deleteUser(id);
//     }
// }




@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    // 회원가입 API
    @PostMapping
    public UsersResponse createUser(@RequestBody UsersRequest request) {
        return UsersResponse.from(usersService.createUser(request.toEntity()));
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);
        Long userId = Long.parseLong(authentication.getName());
        String token = tokenProvider.createAccessToken(userId, authentication);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    // 전체 유저 조회
    @GetMapping
    public List<UsersResponse> getAllUsers() {
        return usersService.getAllUsers()
                .stream()
                .map(UsersResponse::from)
                .collect(Collectors.toList());
    }

    // 단일 유저 조회
    @GetMapping("/{id}")
    public UsersResponse getUserById(@PathVariable Long id) {
        return UsersResponse.from(usersService.getUserById(id));
    }

    // 유저 삭제
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
    }
}

