package com.ceos21.spring_boot.Controller;

import com.ceos21.spring_boot.DTO.PostRequestDTO;
import com.ceos21.spring_boot.DTO.PostResponseDTO;
import com.ceos21.spring_boot.Domain.User;
import com.ceos21.spring_boot.Repository.UserRepository;
import com.ceos21.spring_boot.Service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "게시글 API", description = "게시글 CRUD를 위한 API")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    @Autowired
    public PostController(PostService postService, UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }


    // 게시글 생성
    @Operation(summary = "게시글 생성", description = "게시글을 작성합니다.")
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestBody PostRequestDTO postCreateDTO,
            @RequestHeader("User-Id") String userId,
            @RequestHeader("User-Name") String userName,
            @RequestHeader("User-Email") String userEmail) {

        // 헤더 정보로 User 객체 생성
        User user = new User(userId, "password", userName, userEmail,
                LocalDate.of(2000, 1, 1), "010-0000-0000");
        userRepository.save(user);

        PostResponseDTO postResponseDTO = postService.createPost(postCreateDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }

    // 특정 게시글 조회
    @Operation(summary = "특정 게시글 조회", description = "게시글 ID로 특정 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id) {
        PostResponseDTO postResponseDTO = postService.getPost(id);
        return ResponseEntity.ok(postResponseDTO);
    }

    @Operation(summary = "전체 게시글 조회", description = "모든 게시글을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 게시글 수정
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, @RequestBody PostRequestDTO postUpdateDTO,
                                                      @RequestHeader("User-Id") String userId,
                                                      @RequestHeader("User-Name") String userName,
                                                      @RequestHeader("User-Email") String userEmail) {

        // 헤더 정보로 User 객체 생성
        User user = new User(userId, "password", userName, userEmail,
                LocalDate.of(2000, 1, 1), "010-0000-0000");
        userRepository.save(user);

        PostResponseDTO postResponseDTO = postService.updatePost(id, postUpdateDTO, user);
        return ResponseEntity.ok(postResponseDTO);
    }

    // 게시글 삭제
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id,
                                             @RequestHeader("User-Id") String userId,
                                             @RequestHeader("User-Name") String userName,
                                             @RequestHeader("User-Email") String userEmail) {

        // 헤더 정보로 User 객체 생성
        User user = new User(userId, "password", userName, userEmail,
                LocalDate.of(2000, 1, 1), "010-0000-0000");
        userRepository.save(user);

        String message = postService.deletePost(id, user);
        return ResponseEntity.ok(message);
    }
}
