### 네이버 지식인

**구현 기능**
1. 게시글 조회
2. 게시글에 사진과 함께 글, 해시태그 작성하기 
3. 게시글에 댓글 및 대댓글 기능
4. 게시글 댓글에 좋아요, 싫어요 기능
5. 게시글, 댓글, 좋아요 삭제 기능



#### 네이버 지식인 구조
1. 질문 (Post)
<img src="https://github.com/user-attachments/assets/7c03d81a-2dc6-4525-a813-80c91426f3e0" width="50%">

2. 답변 (Answer)
<img src="https://github.com/user-attachments/assets/a648fd62-186e-49f9-a412-3beb63e4be2b" width="50%">

4. 좋아요/싫어요 (Like_dislike) + 댓글 (Comment)
<img src="https://github.com/user-attachments/assets/251a48a4-0e74-4030-bde4-0b8d74e4e200" width="50%">



## Mission 1️⃣ 데이터 모델링 
(1) **ERD**![Image](https://github.com/user-attachments/assets/e1c66816-b435-4335-80f9-a36cbd603e03)**1. User**
- 한 명의 user은 여러개의 **Post, Aswer, like_dislike, comment**를 작성 가능 (User와 1:N 관계)

**2. Post**
- 하나의 Post에는 여러개의 **Comment, Answer, like_dislike, image** 작성 가능 (Post와 1:N 관계)
- Post와 **Hashtag**는 N:M 관계 -> 중간에 PostHash table 설정

**3. Answer**
- 하나의 Answer에는 여러개의 **comment, Image, like_dislike** 작성 가능 (Answer과 1:N 관계)

### (2) Entity 설계 

**1. LikeDislike**
```
@Enumerated(EnumType.STRING)
   private LikeStatus likestatus;

@Enumerated(EnumType.STRING)
    private TargetStatus targetstatus;
```
- 이 부분에서, LikeStatus는 Enum으로 관리하여 Like, Dislike 설정
- 좋아요/싫어요는 Post와 Answer에 달 수 있으므로 TargetStatus에서 Post, Answer로 관리

**2. Comment**
```
  @Enumerated(EnumType.STRING)
    private TargetStatus targetStatus;
```
- Comment 또한 Post와 Answer에 각각 작성 가능하므로 TargetStatus를 이용하여 하나의 테이블에서 관리

🌟 Comment는 갯수가 많지 않을 것 같고 코드 중복을 피하려고 이 방식을 사용했는데 Post_Comment와 Answer_Comment로 나누는게 나을까요? 의견 부탁드립니다 🌟



## Mission 2️⃣ Repository 단위 테스트 (Post Entity 사용)

**1. User 생성**
```
@BeforeEach
    public void setUp() {
        // 테스트에 사용할 사용자 데이터 생성
        user = User.builder()
                .nickname("dohyun")
                .email("dohyun@naver.com")
                .password("1234")
                .build();

        userRepository.save(user);
    }
```
<img src="https://github.com/user-attachments/assets/8b2227f0-43b7-4b36-ae46-accc9386423d" width="60%">


**2. 작성자를 기준으로 FindPost**
- 첫번째 Post 생성 
```
@Test
    public void testFindByWriter() {
        // given

        //첫번째 질문글 (사진 X)
        Post post1 = Post.builder()
                .title("Post 1")
                .content("hello")
                .writer(user)
                .build();
        postRepository.save(post1);

```
<img src="https://github.com/user-attachments/assets/dffeab57-e437-48eb-ace8-fc28a72cc7af" width="60%">


- 두번째 Post 생성 
```
 Image image = Image.builder()
                .imageUrl("image.jpg") // 이미지 URL 설정
                .post(null)  // 아직 Post와 연결되지 않음
                .build();

        //2번째 질문글 (사진 1장)
        Post post2 = Post.builder()
                .title("Post 2")
                .content("one picture")
                .images(Collections.singletonList(image))
                .writer(user)
                .build();
        image.setPost(post2);
        postRepository.save(post2);
```
<img src="https://github.com/user-attachments/assets/a45d367d-4a33-4cc7-9504-4db1a22590cb" width="60%">


- 세번째 Post 생성
```
 //3번쨰 질문글 (사진 2장)
        Post post3 = Post.builder()
                .title("Post 3")
                .content("two pictures")
                .images(Arrays.asList())
                .writer(user)
                .build();
        postRepository.save(post3);

        Image image1 = Image.builder()
                .imageUrl("image_url_1")
                .post(post3)
                .build();

        Image image2 = Image.builder()
                .imageUrl("image_url_2")
                .post(post3)
                .build();

        imageRepository.save(image1);
        imageRepository.save(image2);

```
<img src="https://github.com/user-attachments/assets/5e5e52df-bfa7-459e-a5b9-5ecf8d3dd19b" width="60%">

- Post DB
<img src="https://github.com/user-attachments/assets/fe378097-02c1-4153-979f-ea16c396b5f2" width="60%">

- Image DB
 <img src="https://github.com/user-attachments/assets/e5232e8e-39c2-48da-80d2-0445e0744e42" width="60%">

- 나머지 when/then
```
// when
        List<Post> posts = postRepository.findByWriter(user);
// then
        assertThat(posts).hasSize(3);
        assertThat(posts).extracting(Post::getTitle).containsExactly("Post 1", "Post 2","Post 3");
```



## Mission 3️⃣ JPA 관련 문제
### (1) 어떻게 data jpa는 interface만으로도 함수가 구현이 되는가?
```
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByWriter(User writer);
}

```
- Spring이 애플리케이션을 실행하면서 PostRepository의 프록시 객체를 생성

- 인터페이스만 정의하면 Spring이 동적으로 구현체를 만들어 주입
이 때, SimpleJpaRepository 클래스가 작동하며 메서드 이름을 분석해 쿼리 자동 생성

> findByWriter(User writer)
→ "SELECT p FROM Post p WHERE p.writer = ?"

- Spring이 내부적으로 EntityManager를 사용하여 쿼리를 실행하고 결과 반환



### (2)  왜 계속 생성되는 entity manager를 생성자 주입을 이용하는가?
- **EntityManager은 싱글톤 객체가 아니다 !!**
- 트랜잭션이 시작될 때 새로운 EntityManager 객체가 동적으로 생성되며, 트랜잭션이 끝날 때 EntityManager는 폐기됨.

> ❔ **그럼 왜 생성자 주입?**
- EntityManager는 **프록시 객체**로 주입되며, 실제 트랜잭션 범위에서만 EntityManager가 생성되고 관리된다.
- 프록시 객체는 애플리케이션에서 하나의 인스턴스로 관리되며(싱글톤), 필요한 시점에 실제 EntityManager를 동적으로 생성한다.



### (3)  Fetch Join과 Distinct
- **Fetch Join** 이란?
  
 : JPQL에서 성능 최적화를 위해 제공하는 기능
 
 : 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
 
 - **Fetch Join** 사용
 ```
"select t From Team t join fetch t.members where t.name = "팀A";
```

 : Name이 "팀A"인 Team을 조회하면서 해당 팀에 속한 members도  함께 즉시 로딩하여 가져오는 쿼리 (즉시 로딩)
  - 만약 "팀 A"에 **Member가 2명** 있다면?
    : **팀 A가 2번 중복** 됨
   
    
 - 이 때 !! **Distinct**를 사용하면
```
"select distinct t From Team t join fetch t.members where t.name = "팀A";
 ```
 : 중복되었던 "Team A"가 **한번** 만 나오게 된다.
 

 (참고 https://9hyuk9.tistory.com/77)

---
### WEEK 3. ERD 수정
![Image](https://github.com/user-attachments/assets/93f0e2b0-2b97-4426-90e2-4ce2fee3f4cb)
- 좋아요/싫어요는 답변 글에만 달 수 있도록 수정

### 구현 기능
<img src="https://github.com/user-attachments/assets/7954e2c9-b181-4b04-bf30-e042610746bd" width="60%">

- User은 로그인 기능이 아직 없어 임의로 추가했습니다.
  <img src="https://github.com/user-attachments/assets/703d8bc7-4e31-4a0f-a273-7eafaace8ffc" width="70%">

#### 1. 질문 작성
![Image](https://github.com/user-attachments/assets/328be23e-9793-4d8d-b9b9-dae4d0bc77b7)
✨ **여기서 이미지는!! AWS S3 버킷 사용**

<img src="https://github.com/user-attachments/assets/bad6c7b5-cb11-437d-90ef-48e405ef1a10" width="70%">

 - 버킷에 잘 들어갔지요~

#### 2. 내가 쓴 모든 질문글 조회
![Image](https://github.com/user-attachments/assets/9499ae8b-c7ab-40de-b747-7069b8adcc36)

#### 3. 내가 쓴 질문글 삭제
<img src="https://github.com/user-attachments/assets/c51a031a-ad44-409f-a7a6-1a74393c080a" width="70%">

- 삭제 성공~

✨ 삭제하려는 userId와 질문 작성자가 다르면?
![Image](https://github.com/user-attachments/assets/94a62a0a-175b-4577-9cd8-15dbfe3a01b5)
- 에러 발생!!

#### 4. 답변 작성
<img src="https://github.com/user-attachments/assets/51d8f2ab-e820-480a-8766-42f2787c317c" width="60%">

![Image](https://github.com/user-attachments/assets/9b9f714d-7601-438d-8ee8-a8a1d12785de)

✨ 질문 작성자가 답변을 달려 하면?
![Image](https://github.com/user-attachments/assets/7d7b8eac-d01e-4872-8952-f58698339081)
- 에러 발생 !!

#### 5. 질문과 답변 조회
![Image](https://github.com/user-attachments/assets/47a6e657-d499-4a7e-a35f-b204a2ebc45d)
- postId를 PathParameter로 입력하면 그 질문과 답변글들을 조회 가능

#### 6. 좋아요/싫어요 달기
![Image](https://github.com/user-attachments/assets/40f6552c-c7f5-44b2-bb19-26960f1a28a9)
✨ 좋아요/싫어요 연타 방지를 어떻게 할까... 생각하다가 

(1) 좋아요-> 좋아요/ (2) 좋아요-> 싫어요/ (3) 싫어요-> 싫어요/(4) 싫어요->좋아요

모두 에러 처리 나도록 했습니다.

(1) 의 경우

<img src="https://github.com/user-attachments/assets/ec241205-d2d6-4be4-b1d5-0a5f7c42d535" width="70%">

(2),(4)의 경우

<img src="https://github.com/user-attachments/assets/19aee750-7f83-4041-9fc5-8a6c8673d7c2" width="70%">

**결국, LIKE/DISLIKE가 있는 경우, 삭제한 후에만 새로 달 수 있습니다.**

#### 7. 좋아요/싫어요 삭제
<img src="https://github.com/user-attachments/assets/9c3f6c42-b3a9-4dcc-8591-57c919f30b3e" width="50%">

 ***

❔Hashtag를 이용한 질문글 찾기를 위해 HashtagController을 따로 둘지, PostController에 포함시킬지 고민중입니다. 어떻게 하셨나요❔

***
### 부가 구현 설명

**1. ErrorStatus + 성공 응답 처리**
 - exception과 ErrorStatus, SuccessStatus 등을 추가하였습니다. 
 - ErrorStatus에서는 에러 처리를 Custom하여 추가합니다.

**2. Swagger**
- SwaggerConfig를 이용한 Swagger 테스트 설정

**3. Converter**
- DTO <-> Entity 간 변환을 Converter에서 처리
- 서비스 로직의 간결성을 위해

**4. Service + ServiceImpl 사용**
- Service는 인터페이스 구현 + ServiceImpl은 비즈니스 로직 처리
- 확장성을 위해

**5. AWS S3 BUCKET 사용**
- 이미지 업로드를 위해 사용
- MultiPartFile 형식으로 이미지를 S3 버킷에 업로드 후, 이미지 URL을 반환하여 DB에 저장

---
### WEEK 4. 로그인/회원가입 추가 + 이 외 기능 구현

#### 1. 회원가입 + 로그인
**1) 로그인 정보를 받아오기 위한 CustomUserDetails**
``` java
public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
```
이후 **@AuthenticationPrincipal** 로 로그인 정보를 주입받았다.

**2) Spring Security**
 ```java
    @Bean
    public SecurityFilterChain myFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) //csrf 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a.requestMatchers("/user/create", "/user/login", "/user/logout", "/connect/**", "/v3/api-docs/**",
                        "/swagger-ui/**", "/swagger-ui.html","permit/**").permitAll().anyRequest().authenticated())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder makePassword() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
```
- 로그인/회원가입/스웨거 등은 인증 절차 없이 필터를 통과,
  로그인하지 않은 사용자가 볼 수 있는 화면 (질문+답변 조회) 등은 엔드포인트를 "**permit/**"으로 시작하게 하여 필터 통과
- 비밀번호 암호화를 위한 인코더 생성

**3) JwtAuthFilter**
 ```java
 UserDetails userDetails = new CustomUserDetails(userId, username, null, authorities);

    // Authentication 객체 설정
 Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
```

- JWT 안의 정보로 CustomUserDetails 객체를 만든다.
  이 때, 비밀번호는 이미 토큰으로 인증된 상태이므로 null 처리

- 만든 Authentication을 SecurityContextHolder에 심어 추후 @AuthenticationPrincipal을 통해 로그인 정보를 꺼냄.

#### 2. 로그인 + 비로그인 구분
<img src="https://velog.velcdn.com/images/dohyunii/post/40c0d955-447e-4d68-87d1-83deb398b807/image.png" width="60%" />
- **post를 예로 들면**

  **<내가 쓴 질문 조회/질문 작성/내가 쓴 질문 삭제>** 등의 api는 로그인 정보를 받아와야 하므로 **/post**로 시작함
  <**해시태그별 글 조회**>는 로그인하지 않은 사용자도 조회 가능하므로 **/permit**으로 시작해 필터 통과함

#### 3. 추가 구현 기능
**(1) 회원가입, 로그인**
- 회원가입 시 email, nickname, password 입력
<img src="https://velog.velcdn.com/images/dohyunii/post/39504893-04dd-4dc2-a376-26cd6ba8b9c0/image.png" width="60%" />
- 이후 로그인 시 토큰 반환
  
  ![](https://velog.velcdn.com/images/dohyunii/post/216e208c-c063-481e-9c64-c155a43494c0/image.png)

**(2) 해시태그별 글 조회**

 <img src="https://velog.velcdn.com/images/dohyunii/post/314325ad-831e-4615-8112-9831b5f53743/image.png" width="40%" /> 
 
 ![](https://velog.velcdn.com/images/dohyunii/post/45c1c7f5-5c5c-446b-9cce-278c02aab72b/image.png)

- **post 삭제 시, post와 hashtag의 관계는 끊고 hashtag는 남겨둠**
``` java
       //4. Post 삭제시 hashtag는 그대로 -> 해당 hashtag의 postId를 null로 설정
        List<PostHash> postHashtags = post.getPostHashtags();
        for (PostHash postHash : postHashtags) {
            postHash.setPost(null);
        }
```
![](https://velog.velcdn.com/images/dohyunii/post/9238ccca-1a0b-4081-a5d0-292bc395f77e/image.png)
: 삭제된 post이기 때문에 post_hash 테이블의 post_id가 null로 바뀌었다.

**(3) 댓글 관련**
- 댓글은 **POST, ANSWER**에 남길 수 있다. 이를 TargetStatus로 구분하였다.
<img src="https://velog.velcdn.com/images/dohyunii/post/71277bc1-08ba-4f2a-ac8e-af22e5dcbb49/image.png" width="50%" />
: TargetStatus에는 POST 또는 ANSWER과 그의 id를 넣으면 된다.


❶ **Post**에 댓글 남김

<img src="https://velog.velcdn.com/images/dohyunii/post/17fb05b4-0d1d-48b4-a8de-8192884fd689/image.png" width="50%" />

❷ **Answer**에 댓글 남김

<img src="https://velog.velcdn.com/images/dohyunii/post/4086b03d-2f08-4aef-a0a1-7c77201a9c88/image.png" width="50%" />

![](https://velog.velcdn.com/images/dohyunii/post/17752005-f2e9-47bb-9d7c-eab40883095e/image.png)

- **Post 삭제 시** 댓글과 답변이 모두 삭제되도록, **Answer만 삭제시** 댓글은 그대로 남도록 했다.
```
        // answer삭제시 comment는 그대로 둠
        List<Comment> comments = commentRepository.findAllByAnswer(answer);
        for (Comment comment : comments) {
            comment.setAnswer(null);
        }
```
![](https://velog.velcdn.com/images/dohyunii/post/05232126-f1fe-4c91-b15e-4811bb3a636c/image.png)
: answer 삭제 후 위와 달리 comment_id 5의 answer_id가 null로 바뀌었다.

🤔이렇게 하면 나중에 어디에 달렸던 댓글인지 알 수 없지 않나 ..??

**-> soft delete**로 변경

- Answer 엔티티에 추가

 ``` java
 @Where(clause = "is_deleted = false")
 // @Where을 두어 isdeleted=false인 것만 조회하도록 함
 
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
 ```
 
 - Answer을 실제로 삭제하는 대신 is_deleted를 true로 설정하여 관계는 그대로 둔다.
   - answer삭제시 answer_id 5의 is_deleted 가 1로 변경
   ![](https://velog.velcdn.com/images/dohyunii/post/5cfdcc23-dbcb-4902-828a-104cf0157b83/image.png)
   - comment 테이블을 보면, answer_id 5가 그대로 남아있다.
   ![](https://velog.velcdn.com/images/dohyunii/post/6095ebc9-993b-424b-ac14-cad21ca939b6/image.png)
   - 글 조회시, is_deleted=false인 답변만 조회된다.
![](https://velog.velcdn.com/images/dohyunii/post/5e43c3f1-3e8d-49dc-b28d-49fe9c722ef4/image.png)


