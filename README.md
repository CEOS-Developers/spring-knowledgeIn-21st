# 2주차

# spring-knowledgeIn-21st

**CEOS Back-End 21st Naver Knowledge-In Clone Coding Project**

---

## 1. 서비스 설명

- 질문자는 질문 게시글을 등록한다.
- 해당 질문에 대해 다수의 사용자는 답변글을 작성 가능하다.
- 이때 **답변글에만 좋아요/싫어요** 표시가 가능하다.
- 질문글과 답변글 모두 **댓글을 추가할 수 있다**.  
  (이때 **삭제는 가능하지만 수정은 불가능**)

---

## 2. ERD 구조도

### 2-1. ERD 이미지

![ERD](./erd_picture.JPG)

### 2-2. ERD 설명

- **User - Post** : 1:N 관계 (한 명의 사용자는 여러 개의 게시글을 작성할 수 있음)
- **Post - PostThumb** : 1:N 관계 (한 게시글은 여러 개의 '좋아요'를 받을 수 있으며, '좋아요'는 사용자마다 한 번씩만 가능)
- **User - PostThumb** : 1:N 관계 (한 명의 사용자는 여러 게시글에 '좋아요'를 할 수 있음)
- **Post - Comment** : 1:N 관계 (한 게시글은 여러 개의 댓글을 가질 수 있음)
- **User - Comment** : 1:N 관계 (한 명의 사용자는 여러 개의 댓글을 작성할 수 있음)
- **Post - Image** : 1:N 관계 (한 게시글에 여러 개의 이미지를 첨부할 수 있음)
- **Post - PostHashTag** : 1:N 관계 (한 게시글은 여러 개의 해시태그를 가질 수 있음)
- **PostHashTag - HashTag** : N:1 관계 (여러 게시글이 같은 해시태그를 사용할 수 있음)
- **Post - Post (Self 참조)** : 1:N 관계 (질문글/답변글 관계 형성)

---

## 3. 설계하며 들었던 고민 지점

혹시 아래 내용들에 관한 리뷰어님들의 좋은 방법이 있다면 꼭 듣고싶습니다..!

### 질문글과 답변글 설계

- **Post 단일 엔티티 VS Question, Answer 두 개의 엔티티**
  - (결정) **Post 단일 엔티티**
  - **장점** : 구조의 단순함, 엔티티 추가 없이도 새로운 유형의 게시글 생성 가능
  - **단점** : 제약조건의 관리 비용 (PostType이 Answer인 경우 `parent_id`가 not null이어야 함)

### 해시태그 설계

- **HashTag 엔티티 VS List<String> 값 타입**
  - (결정) **HashTag 엔티티**
  - **장점** : 기능 확장성 (해시태그 기반 조회와 같은 추가 기능)
  - **단점** : 테이블 추가 비용

### PostThumb 레포지토리 설계

- **레포지토리 없이 Post에게 위임 VS PostThumbRepository 생성**
  - (결정) **Post에게 위임**
  - **장점** : 영속성 관리의 일관성 증가
  - **단점** : Post에 의존하게 됨 (PostThumb를 조회하기 위해서는 Post가 필요)

### 생성자 설계

- **Public 생성자 VS Private 생성자 & Static 팩토리 메서드**
  - (결정) **Static 팩토리 메서드**
  - **장점** : 연관관계 편의 메서드가 존재할 때, 객체를 생성했음에도 편의 메서드를 호출하지 않아 일관되지 않은 객체 관계를 가질 위험을 제거했다.
  - **단점** : 복잡성

---

## 4. 개선 필요점

- **좋아요는 유저당 한 번만 가능하도록 개선 필요**
- **답변글에만 좋아요/싫어요 가능하도록 개선 필요**


# 3주차
# 개발 중 주요 설계 및 구현 질문 정리

---

## Q. List 타입 멤버변수를 DTO로 어떻게 제공할 것인가?

- **Post 조회 시 Comment도 같이 DTO로 전달**  
  → 연관 데이터를 함께 응답하는 방식은 클라이언트의 편의성은 높지만, 조회 범위가 커질수록 응답 성능에 영향을 줄 수 있음. 페이징이 필요한 경우 추가 고민 필요.

- **Post 상세 조회 시 별도의 API 요청을 사용 (DTO 분리)**  
  → 관심사를 분리하여 성능을 최적화할 수 있으며, 선택적으로 데이터를 요청하도록 설계할 수 있음. 하지만 클라이언트에서 여러 요청을 병렬적으로 처리해야 하는 부담이 있음.

---

## Q. DTO에 외부 엔티티 식별자 넣을 것인가?

예: `postId`를 얻기 위해서 `Image`의 `post` 멤버변수를 지연로딩 시켜버림.

!!!

public class ImageResponseDTO {
private Long id;
private String imageUrl;
private Long postId;

    public static ImageResponseDTO from(Image image) {
        return ImageResponseDTO.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .postId(image.getPost().getId()) // ← 이 지연로딩이 불필요한 쿼리 발생의 원인이 될 수 있음
                .build();
    }
}

!!!

- 만약 `postId`가 꼭 필요하지 않다면, `Image` 생성 시점에 `postId`를 함께 저장해두고 바로 꺼내 쓰는 방법도 고려 가능.
- 또는 projection이나 join fetch로 성능 문제를 방지할 수 있음.

---

## Q. PostHashTag를 저장은 어느 서비스가 담당할 것인가?

- PostService vs HashTagService  
  → Post 생성 및 수정 과정에서 해시태그는 보조 정보로 활용되므로, **PostService**가 주도하고, HashTagService는 내부 헬퍼로 활용하는 것이 구조상 더 명확할 수 있음.  
  → 단, 해시태그만 관리하는 별도 기능이 있다면 분리 고려.

---

## Q. JPA가 쿼리를 날리는 시점은 언제인가?

### 1. 일반적인 경우 (em.flush() 없는 경우)
- 커밋 되기 직전

### 2. flush()가 명시된 경우
- 쿼리는 커밋/롤백과 무관하게 미리 나감 → 따라서 쿼리가 나갔어도 롤백 가능

> DELETE 쿼리가 나갔으니, JPA가 당연히 메모리에서도 삭제했을 것?  
> → **그렇지 않다!**

- JPA는 flush() 이후에도 엔티티를 “removed” 상태로 유지하여, 트랜잭션 내에서 필요 시점에 계속 접근 가능하며, 예외적으로 롤백도 지원하게끔 함.
- JPA는 DB 동작과 메모리 상태를 분리하여 관리함.

---

## Q. 쿼리 발생과 영속성 컨텍스트의 관계

**DELETE 쿼리가 발생하면 영속성 컨텍스트에서 더 이상 관리를 안 하는가?**  
→ ❌ 아니다.

- 여전히 `removed` 상태로 관리됨
- 트랜잭션 커밋/롤백 시점까지 유지됨
- 삭제된 엔티티도 트랜잭션 내에서는 상태 추적 대상이며, 조회 시 오류 없이 확인할 수 있음

![사례](./test_persistence_context.JPG)


---

## Q. 게시글 생성을 원자적으로 하나의 트랜잭션으로 관리해야 하는가?

Post는 OK, HashTag도 OK. 하지만 PostHashTag에서 오류 발생 시:  
→ **모두 롤백?** 또는 **그냥 두기?**

- 트랜잭션의 원자성을 유지하는 것이 일반적이며, PostHashTag에 실패했다면 전체를 롤백하는 것이 일관성을 위해 권장됨
- 단, Post만 저장하고 나머지는 보정 프로세스를 따로 둘 수도 있음

---

## Q. 게시글 수정에서 모든 데이터를 하나의 API에서 처리할 것인가?

- 게시글 필수 데이터, 이미지, 해시태그를 하나의 API에서 처리
- vs 각각 엔티티별로 API 생성

**⇒ 이미지 엔티티는 별도 API에서 처리**  
→ 이미지 ID를 받아옴  
→ 본문, 해시태그, 이미지 ID 리스트를 `PostModifyDTO`에서 한꺼번에 수정

- 이 방식은 이미지 등록을 별도로 처리하여 저장 부담을 줄이고, 실제 게시글 수정 시에는 연결만 관리하는 전략
- 특히 이미지 업로드와 게시글 수정의 책임을 분리할 수 있음

---

## Q. 이미지 파일 받아오기

- **Multipart 데이터**
  - 일반적으로 이미지 업로드는 Multipart로 처리하는 것이 표준적임

- **byte 데이터 (Base64 포함)**
  - Base64/byte 방식은 네트워크 비용 증가 및 클라이언트 처리 부담이 큼
  - 단, 내부 서비스 간 통신이나 WebSocket 기반 처리에는 byte 배열도 고려 가능

---

## 🛠 문제 해결

사용자 요청 body에 필드를 아예 넣지 않은 경우 → 기본값인 `new ArrayList<>()`를 사용하고 싶음  
**하지만 자꾸 null이 들어옴**

**이유**: `@AllArgsConstructor`만 있었기 때문!

!!!

@NoArgsConstructor // ✅ List에 디폴트 값을 넣으려면 필수!!

!!!

- 또 다른 해결책으로는 필드 선언 시 `new ArrayList<>()`로 초기화하거나, `@Builder.Default`를 함께 사용하는 방법도 고려 가능
- 빌더 패턴과 생성자 패턴을 함께 쓸 때는 예상치 못한 `null` 문제에 주의해야 함

# 4주차
## JWT 토큰 기반 로그인

### 0. 동작 flow: 자체 로그인 & 토큰

1. **사용자 회원가입** → 회원 생성 & DB 저장
2. **사용자 로그인**
  - ID/PW 검증 (`SecurityConfig`의 `UsernamePasswordAuthenticationFilter`가 처리)
  - 응답으로 AccessToken&RefreshToken 전달 → 클라이언트는 토큰 저장
3. **인증이 필요한 페이지 이동**
  - `JwtAuthenticationFilter`에서 토큰 검증  
    (요청 헤더의 Authorization 필드에서 토큰 추출)
- `SecurityConfig`가 인증객체가 필요한 URL인지 검증

---

### 1. 용어

- **시크릿 키**: 서버가 가지고 있는 key
- **토큰**: 사용자에게 발급되는 인증용 데이터
  - 구조: `[Header].[Payload].[Signature]`
    ```
    // 예시
    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEyMywiaWF0IjoxNjg1NjAwMDB9.sD25T7GLMfVJ-QKqkQOaKci3dfsk1X_U8koLQUmsK2Q
    ```
    - **Header**: 서명에 사용된 알고리즘 정보 (예: HS256, RS256)
      ```
      {
        "alg": "HS256",   // 서명 알고리즘
        "typ": "JWT"      // 토큰 타입
      }
      ```
    - **Payload**: 사용자 정보
    - **Signature**: 서버의 서명 (Header + Payload에 대해 Secret으로 서명함)

  - **서명(Signature)로 방어 가능한 것**
    - 토큰이 위조되지 않았음을 확인 가능

  - **서명으로 방어 불가 항목**
    | 방어 불가 항목           | 설명                                 |
    |-------------------------|--------------------------------------|
    | **토큰 탈취**           | 공격자가 유효한 토큰을 그대로 가져감 |
    | **재사용 공격 (Replay)**| 탈취한 토큰을 그대로 사용           |
    | **XSS, CSRF**           | 클라이언트 측 취약점 이용           |

- **Token과 Secret Key의 관계**
  - Secret을 사용해서 Token에 서명함 (암호화가 아님)

---

### 2. 필요 클래스 구조도
```angular2html
com.example.project
├── config
│ └── SecurityConfig.java # 스프링 시큐리티 설정
├── jwt
│ ├── JwtTokenProvider.java # JWT 생성 및 검증
│ ├── JwtAuthenticationFilter.java # JWT 인증 필터
│ └── JwtProperties.java # 설정 값 관리
├── user
│ ├── CustomUserDetails.java # 시큐리티 컨텍스트에 저장할 유저정보
│ └── CustomUserDetailsService.java# UserDetails를 구현
└── domain

```


---

### 3. Spring Security와 JwtFilter
```angular2html
요청 ─▶ ① Filter Chain (여기에 JwtAuthenticationFilter 포함)
└▶ ② AuthorizationManager / AccessDecision
├─ permitAll → 통과(SecurityContext에 Authentication 객체가 없어도 통과)
└─ authenticated → SecurityContext에 Authentication 있어야 통과
```

- Filter가 먼저 실행되어 인증에 성공하면 `Authentication` 객체를 SecurityContext에 저장
- 이후 SecurityConfig가 인증 객체를 사용해 접근 허용/거부 판단

> **Q. 같은 accessToken으로 여러 번 요청하면 SecurityContext에 인증 객체가 중복 저장되는가?**  
> **A. NO. 요청마다 SecurityContext는 초기화됨**

> **Q. 토큰 기반 인증에서는 매 요청마다 SecurityContext가 초기화된다면, 세션 인증에서는?**  
> **A. YES. 세션 인증은 SecurityContext가 HttpSession에 저장되어 세션 만료 전까지 유지됨**

| 항목                | 세션 기반 인증         | JWT 기반 인증 (토큰 기반)     |
|---------------------|-----------------------|------------------------------|
| 인증 정보 저장 위치 | 서버 `HttpSession`    | 클라이언트 `Authorization` 헤더 (JWT) |
| 상태 유지 방식      | 상태 유지 (Stateful)  | 무상태 (Stateless)           |
| SecurityContext 유지| 세션 만료 전까지 유지 | 요청마다 재생성              |
| 확장성              | 제한적 (서버 메모리)  | 높음 (서버 간 공유 불필요)   |

- **내부 구조**
    ```
    SecurityContextHolder
       └── SecurityContext
               └── Authentication (UsernamePasswordAuthenticationToken 등)
    ```

---

### 4. 로그아웃

| 방식                        | 설명                                                         | 장단점                              |
|-----------------------------|--------------------------------------------------------------|----------------------------------|
| **클라이언트 단 토큰 삭제** | 브라우저/앱에서 JWT를 제거                                   | 장:️ 구현 간단<br>단: 서버는 토큰 유효성 모름    |
| **블랙리스트 (별도 저장소)**| 로그아웃한 access 토큰을 Redis 등 DB에 저장하여 차단         | 장:️ 보안 강화<br>단: 저장소 필요, 성능 고려 필요 |
| **토큰 만료 짧게 + 리프레시**| refreshToken을 redis에서 삭제, accessToken은 짧게, refresh로 재발급 | 장:️ 일반적 방식<br>단: 구현 복잡도 증가       |

| a | b| c|
|---|--|--|

### 기타. 첨부자료

1. 로그인
![로그인](./uploads/login.png)
2. 토큰을 사용한 api
![토큰을 사용한 api](./uploads/api_with_token.png)
3. accessToken 재발행
![토큰 재발행](./uploads/reproduce_accessToken.png)
4. Redis안에 refreshToken 확인
![레디스 리프레시토큰 확인](./uploads/refreshToken_redis.png)
5. Redis안에 blacklist 확인
![레디스 블랙리스트 확인](./uploads/blacklist_redis.png)


