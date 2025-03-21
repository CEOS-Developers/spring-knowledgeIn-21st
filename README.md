# spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project

## 네이버 지식인 DB 모델링

### 요구 구현 기능

1. 게시글 조회
2. 게시글에 사진과 함께 글, 해시태그 작성하기
3. 게시글에 댓글 및 대댓글 기능
4. 게시글 댓글에 좋아요, 싫어요 기능
5. 게시글, 댓글, 좋아요 삭제 기능

### ERD Modeling

![knowledgein-erd](./readme-src/knowledgein-erd.png)

### 엔티티 관계

- User 1 — N Post
- User 1 — N Comment
- User 1 — N Reaction (리액션 삭제 기능이 있으려면 유저와 연결되어 있어야 할듯)
- Post 1 — N Image
- Post 1 — N PostHashtag
- Post 1 — N Comment
- Post 1 — N Reaction
- Hashtag 1 — N PostHashtag
- Comment 1 — N Reaction

### 엔티티 설명

- common > BaseEntity
    - createdAt, updatedAt 필드를 User, Post, Comment, Reaction에 적용

    ```jsx
    @MappedSuperclass
    @EntityListeners(AuditingEntityListener.class)
    @Getter
    public abstract class BaseEntity {
        @CreatedDate
        @Column(columnDefinition = "DATETIME")
        private LocalDateTime createdAt;
    
        @LastModifiedDate
        @Column(columnDefinition = "DATETIME")
        private LocalDateTime updatedAt;
    }
    ```

    - `@MappedSuperclass`
        - 객체의 입장에서 공통 매핑 정보가 필요할 때 사용
        - 공통 매핑 정보를 포함한 부모 클래스를 선언하고 속성만 상속 받아서 사용하고 싶을 때 사용함

          ![mapped-super-class](./readme-src/mapped-superclass.png)

        - Reference: [MappedSuperClass](https://ict-nroo.tistory.com/129)
    - `@EntityListeners(AuditingEntityListner.class)`
        - SpringBootApplication에 `@EnableJpaAuditing` 어노테이션을 붙여줘야 함
        - Auditing 기능을 사용할 수 있도록 함 → 엔티티가 생성되고, 변경되는 그 시점을 감지하여 생성시각, 수정시각, 생성한 사람, 수정한 사람을 기록할 수 있음
- User
    - 유저의 정보
    - 이후 로그인 기능 추가할때 password 및 보안 관련 필드 추가 예정
- Post
    - 게시물 정보
- Image
    - 게시물의 사진 URL 저장
- mapping > PostHashtag
    - 게시물과 해시태그의 매핑 엔티티
    - Post와 Hashtag가 N:M 관계인데 DB 모델링에서 지양해야하므로 사이에 mapping entity를 둬서 1:N, 1:M 관계로 매핑함
- Hashtag
    - 게시물에 붙일 해시태그 정보
    - 해시태그의 성격에 따라서 Enum, Varchar 타입으로 할지 정해야 할듯 (정형화된 태그면 Enum으로)
- Comment
    - 댓글 정보
    - 대댓글 기능
        - 부모 댓글에 자식 댓글들이 달리는 구조 → 1 : N

        ```jsx
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_comment_id")
        private Comment parentComment; // Default로 nullable
        
        @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
        private List<Comment> childrenCommentList = new ArrayList<>();
        ```

        - `parentComment`: 이 값이 `null`이면 부모 댓글임
        - `childrenCommentList`: 부모 댓글에 대한 대댓글들
        - Reference1: [대댓글 reference 1](https://velog.io/@ssm2053/Spring-%EB%8C%93%EA%B8%80%EC%9D%98-%EB%8B%B5%EA%B8%80-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84-%EB%8C%80%EB%8C%93%EA%B8%80-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84)
        - Reference2: [대댓글 reference 2](https://velog.io/@hhss2259/%EB%8C%80%EB%8C%93%EA%B8%80-%EB%8C%93%EA%B8%80%EC%9D%98-%EB%8C%93%EA%B8%80-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)
- Reaction
    - 댓글/게시물에 반응
    - enums > ContentType, enums > ReactionType

    ```jsx
    public enum ContentType {
        POST, COMMENT, NONE
    }
    
    public enum ReactionType {
        LIKE, UNLIKE, NONE
    }
    ```

    - `@DynamicInsert`, `@DynamicUpdate`
        - Hibernate(Spring JPA 구현체)는 엔티티를 데이터베이스에 등록하거나 데이터베이스에 있는 엔티티를 수정할 때 엔티티의 모든 필드를 업데이트 하는 방식으로 이루어짐
        - 굳이 모든 필드 수정이 필요없다면 실제 등록되거나 수정되는 칼럼에 대한 쿼리에 대해서만 insert 및 update를 해주도록 위의 어노테이션 사용
        - 칼럼에 디폴트 값을 설정하고 싶을 때 사용 가능

            ```jsx
            @Enumerated(EnumType.ORDINAL)
            @Column(columnDefinition = "VARCHAR(10) DEFAULT 'NONE'")
            private ContentType contentType;
            
            @Enumerated(EnumType.ORDINAL)
            @Column(columnDefinition = "VARCHAR(10) DEFAULT 'NONE'")
            private ReactionType reactionType;
            ```

        - Refrence: [DynamicInsert, DynamicUpdate](https://velog.io/@choidongkuen/JPA-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D-DynamicInsert-DynamicUpdate-%EC%97%90-%EB%8C%80%ED%95%B4-%EC%95%8C%EC%95%84%EB%B4%85%EC%8B%9C%EB%8B%A4)

### DB 생성 - 화긴

![DB 생성](./readme-src/db-creation.png)

---

## Repository 단위 테스트

### Post Repository Test

```jsx
@SpringBootTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    //@Transactional
    //@Rollback(false)
    void storePostTest() {
        // Post 저장 테스트

        // given - 유저 생성
        // ~~~(생략)
        
        // when - 포스트 생성
        // ~~~(생략)
        
        // then - 저장된 게시물 조회
        List<Post> postList = postRepository.findAll();

        Assertions.assertEquals(3, postList.size());

        postList.forEach(post -> {
            System.out.println("Post: " + post.getTitle());
            System.out.println("Writer: " + post.getUser().getNickname());
        });

    }
}

```

![post-repo-test-log.png](./readme-src/post-repo-test-log.png)

![Screenshot 2025-03-22 at 02.24.21.png](./readme-src/post-repo-test.png)

### `@SpringBootTest` vs `@DataJpaTest`

- `@SpringBootTest`
    - 스프링의 전체 애플리케이션 컨텍스트를 로드
    - `application.yml` 파일의 DB 설정으로 세팅해주며 mySQL도 지원 → so 이거 사용
    - 자동 rollback 지원 안하지만 `@SpringBootTest` + `@Transactional` 로 rollback 사용 가능
- `@DataJpaTest`
    - JPA 관련 컴포넌트(@Entity, Repository)만 로드하여 주로 DB 접근 레이어(Repository)만 테스트함
    - `application.yml` 파일의 DB 설정을 무시하고 h2 DB로 실행 → mySQL 지원 x ㅠㅠ
    - 자동으로 rollback 됨 (내부에 rollback 로직 포함) → rollback 안하려면 `@rollback(false)` 설정