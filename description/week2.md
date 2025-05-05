# spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project

## 1️⃣2주차 미션 - 네이버 지식인의 DB를 모델링해봐요!

---

### ERD 설명

![image.png](attachment:c2e897e0-7e9f-44a3-af94-f721233c72d4:image.png)

**Member 테이블**

- 회원 정보를 관리하는 테이블, 각 회원의 기본 정보와 생성 시점을 저장합니다.

**Post 테이블**

- 각 게시글의 내용을 저장
- 작성자의 `member_id`를 참조합니다.
- **질문** : 여기서 해시 태그를 1~3으로 지정했는데, 어떻게 하는게 효율적일까요? 다들 어떻게 구성하셨나요? 일단 저는 사용자가 많이 없을 거라 생각해서 이렇게 작성했는데…만약에 나중에 해시 태그로 조회한다면 빼는 게 맞겠죠?

**Comment 테이블**

- 게시글에 대한 댓글을 저장하며, `post_id` 로 어느 게시글에 댓글이 달렸는지 추적합니다.
- `parent_id` 는 대댓글을 지원하는 기능을 위한 컬럼입니다.

**Like_Comment 테이블**

- 댓글에 대한 좋아요/싫어요 를 저장하는 테이블입니다.
- **질문** : 저는 `enum`으로 만들 예정입니다! 속성으로 like, dislike, nothing으로 처리 후 기본이 nothing 이 될 예정인데, 다른 분들은 어떻게 구성하셨나요?
    - 조금 더 생각해보니.. 이렇게 하면 모든 사람들이 게시글이나 댓글에 대한 연관이 있어서 DB가 커지겠네요.. 이건 수정합니다..

**Like_Post 테이블**

- 게시글에 대한 좋아요/싫어요 를 저장하는 테이블입니다.
- 이것도 위와 같은 질문이 있습니다! 그리고 추가로 이렇게 테이블이 빼는게 더 괜찮겠죠??
- *Like Comment와 Like_Post 테이블을 합쳤습니다! (추후에 수정 가능할 수 있어서 erd는 저렇게 만들었어요!)*

### 관계 설명

- **Member → Post**
    - 1 : N 관계. 하나의 회원이 여러 게시글을 작성할 수 있습니다.
- **Post → Comment**
    - 1 : N 관계. 하나의 게시글에 여러 댓글이 달릴 수 있습니다.
- **Member → Comment**
    - 1 : N 관계. 하나의 회원이 여러 댓글을 작성할 수 있습니다.
- **Comment → Like_Comment & Like_Post**
    - 1 : N 관계. 하나의 댓글에 여러 좋아요/싫어요가 달릴 수 있습니다.
- **Member → Like_Comment or Like_Post**
    - 1 : N 관계. 하나의 회원이 여러 게시글, 댓글에 대해 좋아요/싫어요를 누를 수 있습니다.
- **Comment → Comment (Parent-Child 관계)**
    - `parent_id` 를 통해 댓글에 대한 대댓글 기능을 구현할 예정입니다.

### 구현 전 설정

- **응답 통일 설정**
    - ReasonDTO && 성공 및 실패 응답 통일
        - 일관성 있는 응답 형식 && 유지 보수에 용이

    ```java
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReasonDTO {
        private String message;
        private String code;
        private Boolean isSuccess;
        private HttpStatus httpStatus;
    }
    ```

    ```
    // 성공 응답
    		_OK(HttpStatus.OK, "COMMON200", "성공입니다.");
    
    // 실패 응답
    // 그냥 이전에 쓰던 거 가져왔습니다...
        _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000", "서버 에러, 관리자에게 문의 바랍니다."),
        _MAILSENDER_ERROR(HttpStatus.BAD_REQUEST,"COMMON5001","인증 이메일 전송에 실패하였습니다."),
        _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON4000","잘못된 요청입니다."),
        _TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "Exception test 입니다."),
        _DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "COMMON4002", "중복된 이메일입니다."),
        _BAD_PASSWORD(HttpStatus.BAD_REQUEST, "COMMON4003", "잘못된 패스워드입니다."),
        _NON_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "COMMON4004", "존재하지 않는 이메일입니다."),
        _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON4010","인증이 필요합니다."),
        _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON4030", "금지된 요청입니다."),
        _FORBIDDEN_PASSWORD(HttpStatus.FORBIDDEN, "COMMON4031","불가능한 패스워드입니다. 패스워드는 영어,숫자 8-13글자만 가능합니다.");
    ```


~~참고 : 저는 꽤 많은 라이브러리를 사용했는데, spring-boot-validation, spring-boot-starter-web,Jackson, 등을 사용했습니다. 참고로 버전 때문에 오류 좀 생겨서 ㅎㅎ 잘 알아보고 하는 게 좋을 것 같습니다!~~

### API 명세서

1. 게시글 조회
2. 게시글에 제목, 본문, 사진, 해시태그 작성
3. 게시글 댓글 및 대댓글 작성
4. 게시글 좋아요 또는 싫어요
5. 댓글에 좋아요 또는 싫어요
6. 게시글 좋아요 또는 싫어요 삭제
7. 게시글 좋아요 또는 싫어요 삭제

### Entity 생성

저는 왜 인지 모르겠는데 꼭 BaseEntity를 만드는 버릇이 있더라고요

```java
@MappedSuperclass
@EntityListeners(AuditingConfiguration.class)
@Getter
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

- `@MappedSuperclass`
    - JPA 어노테이션, 상속받는 엔티티가 부모 클래스의 필드들을 상속하도록 하는 역할
    - 이 어노테이션을 붙인 클래스는 **엔티티로 매핑되지 않으며**, **오직 상속을 위한 베이스 클래스로만** 사용됩니다.
    - 따라서 **실제 테이블을 생성하지 않고**, 이 클래스를 상속 받아 공통 필드를 사용할 수 있게 됩니다.
- `@EntityListeners(AuditingConfiguration.class)`
    - 엔티티의 이벤트 리스너를 지정하는 어노테이션입니다. 이 어노테이션을 통해 특정 엔티티가 저장, 수정, 삭제 등의 이벤트를 처리하는 리스너를 연결할 수 있습니다.
    - `@CreatedDate` 와 `@LastModifiedDate` 와 같은 **자동 생성 및 수정 시간**을 처리
- `@CreatedDate`
    - Spring Data JPA Auditing 에서 제공
    - 해당 필드가 엔티티 **생성** 시점에 **자동으로 현재 시간**을 설정 해 줍니다.
    - 즉, 저장할 때 자동으로 생성 시간이 기록됩니다.
- `@LastModifiedDate`
    - Spring Data JPA Auditing 에서 제공
    - 해당 필드가 엔티티 **수정** 시점에 **자동으로 현재 시간을 설정** 해 줍니다.
    - 즉, 수정될 때 자동으로 현재 날짜와 시간이 설정됩니다.
- 왜 abstract로 했을까요~?
    - 앞에서 @MappedSuperclass 어노테이션이 붙은 클래스는 실제 테이블에 매핑되지 않고, 오직 상속을 위한 클래스라고 했죠!
    - 그렇기에 BaseEntity를 직접 인스턴스화 하여 사용할 필요가 없기에 이 클래스를 직접 객체로 생성할 수 없도록 하는 것 입니다.
    - `BaseEntitiy base = new BaseEntity();` 와 같은 인스턴스는 로직 상 아예 불가능 한 것이지요!
    - 또한 상속을 통해서 공통 필드를 재사용 가능하답니다 🙂

## 2️⃣ Repository 단위 테스트를 진행해요

---

### TEST 구현 목록

1️⃣ 게시글 저장

```
// save method 가 호출되어 결과가 반환되는지 확인
assertThat(savedPost1.getTitle()).isEqualTo("제목1");
assertThat(savedPost2.getTitle()).isEqualTo("제목2");
```

2️⃣ 게시글 삭제

```
assertThrows(IllegalArgumentException.class, () -> {
            postRepository.findById(savedPost1.getPostId()).orElseThrow(() 
            -> new IllegalArgumentException("Post not found"));
        });
```

++게시글 저장 실패

*하단의 트러블 슈팅에 정리했습니다.*

![image.png](attachment:eeb0b827-ed80-4647-9e4e-14120c48783b:image.png)

## 3️⃣ (옵션) JPA 관련 문제 해결

---

<aside>
💡

1.  **어떻게 data jpa 는 interface 만으로도 함수가 구현이 되는가?**
</aside>

- **Query Method Derivation 과 동적 쿼리 생성**
    - Spring Data JPA 는 `JpaRepository`  와 `CrudRepository`  와 같은 인터페이스를 제공
    - 따라서 상속받은 인터페이스에 따라 메소드 이름을 기반으로 쿼리를 자동 생성함

```java
// SELECT * From member WHERE login_id = ?
Member findByLoginId(String loginId);

// SELECT * FROM member WHERE name = ? AND email = ?
List<Member> findByNameAndEmail(String name, String email);

// SELECT * FROM member WHERE name = ? ORDER BY email ASC
List<Member> findByNameOrderByEmailAsc(String name);
```

<aside>
💡

1. **data jpa를 찾다보면 SimpleJpaRepository 에서 entity manager를 생성자 주입을 통해서 주입 받는다. 근데 싱글톤 객체는 한 번만 할당을 받는데, 한 번 연결 때 마다 생성이 되는 entity manager를 생성자 주입을 통해서 받는 것은 수상하지 않는가? 어떻게 되는 것일까? 한 번 알아보자**
</aside>

- 사전 지식 ) `SimpleJpaRepository` 는 `JpaRepository`의 기본 구현체. EntityManager 를 통해 데이터베이스 작업을 수행. `EntityManager` 는 **영속성 컨텍스트**를 관리하며, **트랜젝션 단위**로 데이터를 다룬다.
    - EntityManager 주입 방식 알아보기
        - SimpleJpaRepository 는 @PersistenceContext를 사용하여 EntityManager 를 주입받는다.
        - @PersistenceContext 는 Spring DI 에서 관리되는 EntityManager 를 자동으로 주입하는 방법이다.
        - **트랜젝션 범위 내에서 EntityManager을 관리하므로, 매번 새로운 EntityManager 인스턴스를 생성하는 것이 아니라, 트랜젝션 단위로 하나의 EntityManager 를 사용!**
    - EntityManager의 스코프
        - **매번 새로운 객체 생성 x → 트랜잭션 단위로 관리한다.**
        - @Transactional 을 사용해 싱글톤으로 관리한다.
        - 각 트랜잭션 마다 새로 생성되고 관리되지만, 빈의 생애 주기는 싱글톤이다.
    - 동작 방법 알아보기
        1. @PersistenceContext 는 **Persistence Context(컨테이너 관리 영속성 컨텍스트)**를 **트탠잭션 범위** 내에서 관리한다. 따라서 EntityManager 는 스프링 빈으로 관리되지만, 트랜잭션을 시작하고 종료할 때마다, 트랜잭션에 맞는 EntityManager 가 생성되어 해당 트랜잭션에만 연관된다.
        2. 트랜젝션이 시작되면, 하나의 EntityManager 인스턴스가 트랜잭션 내에서 사용된다. **트랜젝션이 끝나면 EntityManager는 자동으로 flush 되고, transaction 이 commit/rollback**된다. 트랜잭션 단위가 끝날 때마다, **해당 EntityManager는 빈풀에 반환되며, 다음 트랜잭션에서 새로 할당될 때는 새 EntityManager가 사용된다.**
        3. **EntityManagerFactory 는 싱글톤**으로 애플리케이션 전체에서 하나만 존재한다. EntityManager 는 EntityManagerFactory에서 **필요한 트랜잭션 범위에 맞게 생성**되며, 이를 생성자 주입을 통해 SimpleJpaRepository에 주입한다. 따라서 SimpleJpaRepository는 **트랜잭션을 처리할 때마다 해당 EntityManager을 사용해 작업을 처리하므로, 하나의 트랙잭션에 대해서는 하나의 EntityManager 만 존재하고, 매번 트랜잭션 단위로 다른 EntityManager가 사용된다.**

  **[정리]**

    - `EntityManagerFactory` 는 애플리케이션 전역에서 **하나만** 존재. EntityManager 생성 역할
    - `@PersistenceContext` 를 통해 Spring 이 관리하는 EntityManager를 트랜잭션 범위 내에서 주입
    - **트랜잭션 시작** 시 새로운 EntityManager 가 트랜잭션에 맞게 생성
    - **트랜잭션 종료** 시 EntityManager 가 Flush 되고 Commit 또는 Rollback
    - SimpleJpaRepository는 트랜잭션마다 주입된 EntityManager를 사용해 DB 작업을 처리한다.

<aside>
💡

1. **fetch join 할 때 distinct를 안하면 생길 수 있는 문제**
</aside>

- **중복 데이터 생성 가능**
    - fetch join 이란?
        - 연관된 데이터를 가져오기 위한 테이블 조인 방식
        - SQL 에서 이야기 하는 조인의 종류는 아니다! JPQL에서 성능을 최적화하기 위해 제공하는 조인의 종류이다. (객체 지향 쿼리)
        - 일반 조인 시 N+1 문제가 생기기 때문에 사용한다.

    ```
    @Query("SELECT p FROM Post p JOIN FETCH p.comments WHERE p.author =: author"
    ```

- 위에서 Post 가 여러 개의 Comment를 가진 경우, fetch join으로 Post 와 Comment를 조인하면, Post 엔티티가 여러 번 중복되어 반환된다.
    - 예를 들어 Post가 3개의 Comment를가졌다면, Post는 3번 중복

<aside>
💡

**fetch join 을 할 때 생기는 에러가 생기는 3가지 에러 메시지의 원인과 해결 방안**

</aside>

1. `HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!`
    - **원인** : firstResult 와 masResult 를 함께 fetch join 시 생김
        - fetch join 은 연관된 엔티티들을 한번에 가져오기 때문에, 페이징 적용 시 문제가 생긴다.
        - **쿼리 결과를 전부 메모리에 적재한 뒤 Pagination 작업을 애플리케이션 레벨에서 하기 때문에 위험하다는 의미.**
    - **해결 방안 :** 페이징을 fetch join과 함께 사용하지 말자
        1. 페이징을 먼저 처리 → 나중에 연관된 데이터를 별도로 로딩하는 방식 사용
        2. `hibernate.default_batch_fetch_size` , `@BatchSize` 로 최적화 도 가능하다.
        - 대량의 데이터를 처리하는 것을 목적으로 하는 스프링 배치 환경에서는 메모리에서 모든 데이터를 처리하는 방식은 무조건 지양할 것

   ++같이 보면 좋은 블로그

   https://javabom.tistory.com/104

2. `query specified join fetching, but the owner of the fetched association was not present in the select list`
    - **원인** : fetch 하려는 연관된 entity 가 select 목록에 포함되지 않은 경우.
    - **해결 방안** : 해당 연관된 엔티티가 **select목록에 반드시 포함**되도록 명시적으로 포함
        - **또는 그냥 fetch join 을 제거한다**…도 있다고 하는데 N+1 문제가 발생할 수 있다.
        - 그렇기에 2주차 스터디에서 배운 Batch Size 조정 등을 사용할 수 있을 것 같습니다.
3. `org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags`
    - 원인 : **여러 개의 List 또는 Bag 컬렉션을 동시에 fetch join** 으로 로딩 시 발생.
        - Join을 통해 연관 관계가 있는 여러 개의 데이터를 한꺼번에 가져오면 카테시안 곱이 발생일 수 있다.
        - **Bag collection ??**
            - Hibernate 에서 사용하는 용어로 순서가 없고, 키가 없으며, 중복을 허용한다.
            - Java Collection에는 Bag 가 구현되어 있지 않아, List 를 사용한다.
            - 가방 안에 들어 있는 물건들을 내가 꺼낼 때 순서 보장 없이 물건이 꺼내지는 모습과 유사
    - 해결 방안 : hibernate.default_batch_fetch_size 설정
        - 여러 쿼리를 사용해서 해결 (나는 쿼리 만들기 싫은뎅)
        - List 를 Set 으로 바꿔서 사용한다.
            - Set ???
                - Java Collection Framework 에서 제공하는 인터페이스
                - 순서를 보장하지 않으며, **중복을 허용하지 않는** 컬렉션
                - `HashSet`  : 순서가 보장되지 않으며, 중복된 값을 허용 x
                - `LinkedHashSet`  : 입력 순서가 유지, 중복 x
                - `TreeSet`  : 자연 순서나 지정된 비교자에 따라 순서 정렬됨
                - 검색, 삽입, 삭제가 평균적으로 O(1) 시간 복잡도를 가진다.
                - 음.. 그렇다면 중복이 없어야 하는 경우 List 대신 Set 을 사용하는 것도 방법일 수 있겠네요.

---

### +추가 : 트러블 슈팅 정리

트러블 슈팅 정리

- `ComtextLoads() FAILED`
  `database-platform: org.hibernate.dialect.H2Dialect`
    - DB 연결 실패 → H2 서버를 켜 놓지 않거나, DB 가 생성되지 않았음

- `org.mockito.exceptions.misusing.UnnecessaryStubbingException`
    - **원인** : Mockito 테스트에서 불필요하게 설정된 stub 코드가 있을 때 발생
        - stub란 ?? 객체의 메서드를 호출했을 때 원하는 값을 리턴하기 위해 사용
    - **Strict stubbing** 이 뭐지?
        - Mockito 에서 테스트의 품질을 높이기 위한 규칙
        - **테스트가 정의한 동작 외에 불필요한 stub 설정을 허용하지 않습니다!**
        - LENIENT, WARN, STRICT_STUBS 라는 3단계의 Strictness 를 가지고 있다.
            - **LENIENT** : 모든 stub 설정을 허용하고, 필요하지 않은 stub도 무시한다.
            - **WARN** : 불필요한 stub가 있으면 경고를 출력하고, 예외를 발생시키지 않는다.
            - **STRICT_STUBS** : 불필요한 stub가 있으면 예외를 발생시켜 테스트를 실패하도록 만든다.
        - SUT  에 불필요한 stub 코드가 작성되어 있으면, ‘UnnecessaryStubbingException’ 이 발생
    - 해결 방법
        1. 불필요한 stub 제거
        2. `@BeforeEach` 에 **제한적으로 Lenient 모드 사용** ← 를 사용했다.
            - 특정 테스트에서만 lenient 모드를 적용하고 싶다면, @BeforeEach 에서 Mockito.lenient() 메서드를 사용해 특정 mock 객체에 대해 lenient 모드를 설정 가능하다. 따라서 **불필요한 stub 가 있어도 예외를 발생시키지 않고 무시하도록 설정한**다.

```java
@BeforeEach
    public void setUp() {
        Mockito.lenient().when(myClass.someMethod()).thenReturn("some value");
    }
```

- **게시글 저장 실패 test에서 IllegalArgumentException을 예상했는데 안나옴…!!!!**
    - **현재 상태** : Repository test를 진행하려고 한다. 이 때 Post 의 title이 null일 경우 당연히 `IllegalArgumentException` 이 발생한다고 생각했다. 참고로 title에는 `@NotBlank` 가 걸려있다.
        - 그런데 여기서 찾은 것은??? 아니 @NotBlank나 NotNull은 JPA에서 직접적으로 유효성 검사를 하지 않는다고?
        - 그럼 어디서 유효성 검사를 하는건데??? → 서비스까지 가서 이 유효성 검사를 한다고 이상한데?
        - 엥??? JPA save() 메서드에서 자동으로 적용되지가 않는다네요?? 뭐죠?
        - 그대로 저장되고 예외는 @Valid를 통해서 검증이 되어야 한다네요! 헐 진짜 몰랐음.. 어이없어

  **[정리]**

  ### JPA 유효성 검사 (Validation)

    - JPA 는 객체가 @Entity로 매핑된 후 DB와의 연동만 담당한다
    - 즉, save() 메서드는 단순히 객체를 저장할 뿐 유효성 검사는 하지 않는다.
    - @NotBlank나 @NotNull, @NotEmpty 와 같은 어노테이션은 JPA에서 자동으로 처리되지 않습니다.
    - 그럼 어디에서 적용되나??
        - Spring Validation 라이브러리에서 처리된다.
        - 즉, JPA 에서는 검증하지 않는다.
        - 유효성 검사는 보통 Service, Controller layer에서 @Valid 어노테이션과 함께 수행된다.

  ### Spring 에서 유효성 검사 적용 방법

    - @Valid는 객체가 controller/service 로 전달되서나 서비스로 전달되기 전 해당 객체를 검증한다.
    - 이 때 유효성 검사가 실패하면, `MethodArgumentNotValidException`이 발생한다.
    - 또는 `ConstraintViolationException`을 예외로 던진다.
    - 추가적으로 @Transactional 을 이용해 잘못되면 rollback 실행해야 한다.
    - 또는 DB 제약 조건을 걸어서 데이터 검증이 가능하다.

```java
@Test
    public void 게시글저장_실패test() throws Exception {
        // given: 필요한 조건 설정
        Post invalidPost = Post.builder()
                .title("")  // 제목이 빈 문자열일 때 예외 발생을 예상
                .content("내용")
                .hashtag("해시태그")
                .build();

        // when: 예외가 발생할 것으로 예상
        assertThrows(IllegalArgumentException.class, () -> {
            postRepository.save(invalidPost);  // 예외가 발생해야 함
        });

    }
```

[참고 자료]

https://xxeol.tistory.com/54

https://medium.com/sjk5766/spring-boot-notnull-notempty-notblank-validation%EA%B3%BC-%ED%85%8C%EC%8A%A4%ED%8A%B8-c3410e82689c