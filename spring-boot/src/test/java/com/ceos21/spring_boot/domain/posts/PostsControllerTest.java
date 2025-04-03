// package com.ceos21.spring_boot.domain.posts;

// import com.ceos21.spring_boot.domain.posts.dto.PostsRequest;
// import com.ceos21.spring_boot.domain.posts.dto.PostsResponse;
// import com.ceos21.spring_boot.domain.users.Users;
// import com.ceos21.spring_boot.domain.users.UsersService;
// import com.ceos21.spring_boot.exception.GlobalExceptionHandler;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;

// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.context.annotation.Import;

// import java.time.LocalDateTime;
// import java.util.List;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(PostsController.class)
// @Import(GlobalExceptionHandler.class)
// class PostsControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private PostsService postsService;

//     @MockBean
//     private UsersService usersService;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @Test
//     @DisplayName("POST /api/posts - 게시글 생성 (Builder 사용)")
//     void testCreatePost() throws Exception {
//         PostsRequest request = PostsRequest.builder()
//                 .userId(1L)
//                 .texts("테스트 내용")
//                 .tags("태그")
//                 .pictures("pic.jpg")
//                 .comment("댓글")
//                 .build();

//         Users user = Users.builder()
//                 .id(1L)
//                 .name("홍길동")
//                 .email("test@test.com")
//                 .build();

//         var postEntity = request.toEntity(user);
//         var response = PostsResponse.from(postEntity);

//         when(usersService.getUserById(1L)).thenReturn(user);
//         when(postsService.createPost(any())).thenReturn(postEntity);

//         mockMvc.perform(post("/api/posts")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.texts").value("테스트 내용"));
//     }

//     @Test
//     @DisplayName("GET /api/posts - 전체 조회")
//     void testGetAllPosts() throws Exception {
//         var post = Posts.builder()
//                 .id(1L)
//                 .texts("내용")
//                 .tags("태그")
//                 .pictures("이미지")
//                 .comment("댓글")
//                 .createdAt(LocalDateTime.now())
//                 .updatedAt(LocalDateTime.now())
//                 .build();

//         when(postsService.getAllPosts()).thenReturn(List.of(post)); // List<Posts> 반환


//         mockMvc.perform(get("/api/posts"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].texts").value("내용"))
//                 .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
//     }

//     @Test
//     @DisplayName("GET /api/posts/{id} - 개인 조회")
//     void testGetPostById() throws Exception {
//         var post = Posts.builder()
//                 .id(1L)
//                 .texts("내용입니다")
//                 .tags("태그")
//                 .pictures("사진")
//                 .comment("댓글")
//                 .build();

//         when(postsService.getPostById(1L)).thenReturn(post);

//         mockMvc.perform(get("/api/posts/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(1));
//     }

//     @Test
//     @DisplayName("DELETE /api/posts/{id} - 게시글 삭제")
//     void testDeletePost() throws Exception {
//         doNothing().when(postsService).deletePost(1L);

//         mockMvc.perform(delete("/api/posts/1"))
//                 .andExpect(status().isOk());
//     }
// }


// package com.ceos21.spring_boot.domain.posts;

// import com.ceos21.spring_boot.domain.posts.PostsService;
// import com.ceos21.spring_boot.domain.posts.dto.PostsRequest;
// import com.ceos21.spring_boot.domain.posts.dto.PostsResponse;
// import com.ceos21.spring_boot.domain.users.Users;
// import com.ceos21.spring_boot.domain.users.UsersService;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.web.client.RestTemplate;

// import static org.junit.jupiter.api.Assertions.*;

// import java.time.LocalDateTime;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
// @ActiveProfiles("test") // 테스트로 db 연결
// class PostsControllerTest {

//     @Autowired
//     private RestTemplate restTemplate;

//     @Autowired
//     private UsersService usersService;

//     @Test
//     @DisplayName("POST /api/posts - 게시글 생성")
//     void testCreatePost() {
//         PostsRequest request = PostsRequest.builder()
//                 .userId(1L)
//                 .texts("테스트 내용")
//                 .tags("태그")
//                 .pictures("pic.jpg")
//                 .comment("댓글")
//                 .build();

//         // 실제 db에서 사용할 user 객체
//         Users user = usersService.getUserById(1L);
//         if (user == null) {
//             user = Users.builder()
//                     .id(1L)
//                     .name("홍길동")
//                     .email("test@test.com")
//                     .build();
//             usersService.createUser(user); // 테스트 사용자 추가
//         }

//         // 실제 HTTP POST 요청 보내기
//         HttpEntity<PostsRequest> entity = new HttpEntity<>(request);
//         PostsResponse response = restTemplate.exchange("/api/posts", HttpMethod.POST, entity, PostsResponse.class).getBody();

//         // 응답 확인
//         assertNotNull(response);
//         assertEquals("테스트 내용", response.getTexts());
//     }

//     @Test
//     @DisplayName("GET /api/posts - 전체 조회")
//     void testGetAllPosts() {
//         var post = Posts.builder()
//                 .id(1L)
//                 .texts("내용")
//                 .tags("태그")
//                 .pictures("이미지")
//                 .comment("댓글")
//                 .createdAt(LocalDateTime.now())
//                 .updatedAt(LocalDateTime.now())
//                 .build();

//         PostsService.createPost(post); // DB에 실제 게시글 생성

//         // 실제 HTTP GET 요청 보내기
//         var response = restTemplate.exchange("/api/posts", HttpMethod.GET, null, PostsResponse[].class).getBody();

//         // 응답 확인
//         assertNotNull(response);
//         assertEquals("내용", response[0].getTexts()); // 첫 번째 게시글의 텍스트 확인
//     }

//     @Test
//     @DisplayName("GET /api/posts/{id} - 개인 조회")
//     void testGetPostById() {
//         var post = Posts.builder()
//                 .id(1L)
//                 .texts("내용입니다")
//                 .tags("태그")
//                 .pictures("사진")
//                 .comment("댓글")
//                 .build();

//         postsService.createPost(post);

//         var response = restTemplate.exchange("/api/posts/1", HttpMethod.GET, null, PostsResponse.class).getBody();

//         assertNotNull(response);
//         assertEquals(1L, response.getId());
//         assertEquals("내용입니다", response.getTexts());
//     }

//     @Test
//     @DisplayName("DELETE /api/posts/{id} - 게시글 삭제")
//     void testDeletePost() {
//         var post = Posts.builder()
//                 .id(1L)
//                 .texts("삭제할 게시글")
//                 .tags("태그")
//                 .pictures("사진")
//                 .comment("댓글")
//                 .build();

//         postsService.createPost(post);

//         restTemplate.delete("/api/posts/1");


//         var response = restTemplate.exchange("/api/posts/1", HttpMethod.GET, null, PostsResponse.class);
//         assertEquals(404, response.getStatusCodeValue());
//     }
// }




package com.ceos21.spring_boot.domain.posts;

import com.ceos21.spring_boot.domain.posts.dto.PostsRequest;
import com.ceos21.spring_boot.domain.posts.dto.PostsResponse;
import com.ceos21.spring_boot.domain.users.Users;
import com.ceos21.spring_boot.domain.users.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
// @ActiveProfiles("test")
@SpringBootTest
class PostsControllerTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testServiceBeanExists() {

        assertNotNull(context.getBean(PostsService.class), "PostsService 빈이 등록되지 않았습니다.");
        assertNotNull(context.getBean(UsersService.class), "UsersService 빈이 등록되지 않았습니다.");
    }



    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UsersService usersService;

    @Test
    @DisplayName("POST /api/posts - 게시글 생성")
    void testCreatePost() {
        PostsRequest request = PostsRequest.builder()
                .userId(1L)
                .texts("테스트 내용")
                .tags("태그")
                .pictures("pic.jpg")
                .comment("댓글")
                .build();

        Users user = usersService.getUserById(1L);
        if (user == null) {
            user = Users.builder()
                    .id(1L)
                    .name("홍길동")
                    .email("test@test.com")
                    .build();
            usersService.createUser(user);
        }

        HttpEntity<PostsRequest> entity = new HttpEntity<>(request);
        PostsResponse response = restTemplate.exchange("/api/posts", HttpMethod.POST, entity, PostsResponse.class).getBody();

        assertNotNull(response);
        assertEquals("테스트 내용", response.getTexts());
    }

    @Test
    @DisplayName("GET /api/posts - 전체 조회")
    void testGetAllPosts() {
        var post = Posts.builder()
                .id(1L)
                .texts("내용")
                .tags("태그")
                .pictures("이미지")
                .comment("댓글")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        HttpEntity<Posts> entity = new HttpEntity<>(post);
        restTemplate.exchange("/api/posts", HttpMethod.POST, entity, PostsResponse.class);


        var response = restTemplate.exchange("/api/posts", HttpMethod.GET, null, PostsResponse[].class).getBody();

        assertNotNull(response);
        assertEquals("내용", response[0].getTexts());
    }

    @Test
    @DisplayName("GET /api/posts/{id} - 단건 조회")
    void testGetPostById() {
        var post = Posts.builder()
                .id(1L)
                .texts("내용입니다")
                .tags("태그")
                .pictures("사진")
                .comment("댓글")
                .build();

        HttpEntity<Posts> entity = new HttpEntity<>(post);
        restTemplate.exchange("/api/posts", HttpMethod.POST, entity, PostsResponse.class);


        var response = restTemplate.exchange("/api/posts/1", HttpMethod.GET, null, PostsResponse.class).getBody();

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("내용입니다", response.getTexts());
    }

    @Test
    @DisplayName("DELETE /api/posts/{id} - 게시글 삭제")
    void testDeletePost() {
        var post = Posts.builder()
                .id(1L)
                .texts("삭제할 게시글")
                .tags("태그")
                .pictures("사진")
                .comment("댓글")
                .build();


        HttpEntity<Posts> entity = new HttpEntity<>(post);
        restTemplate.exchange("/api/posts", HttpMethod.POST, entity, PostsResponse.class);

        restTemplate.delete("/api/posts/1");

        var response = restTemplate.exchange("/api/posts/1", HttpMethod.GET, null, PostsResponse.class);
        assertEquals(404, response.getStatusCodeValue());
    }
}
