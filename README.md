# spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project

헷갈렸던 개념 정리

Q. 참조하는 쪽이 관계의 주인인가??
A. X 관계의 주인은 FK를 가진 쪽으로 결정이 됨. 참조하는 쪽이라고 항상 FK를 가지는 것이 아니기에 항상 참조하는 쪽이 관계의 주인인 것은 아님.

Q. 참조하는 쪽에 FK가 생기나??
A. X 관계의 종류(1:1, 1:N, N:1, N:N)에 따라 달라짐.


테이블 관계 정리

OneToOne : mappedBy가 없는 쪽에 FK(mapedBy가 없다 => 관계의 주인이다 => FK를 가진다.) //OneToOne은 아직 헷갈리네요...
ManyToOne : 참조하는 쪽(Many)에 FK가 생김
OneToMany : 참조받는 쪽(Many)에 FK가 생김
ManyToMany : 중간 테이블 만들어서 ManyToOne 또는 OneToMany를 만듦

=>ManyToOne, OneToMany 둘 다 Many 쪽에서 FK가 생김 => 이렇게 해야 데이터베이스가 더 깔끔해질 거 같긴 함(아닌가...?)

ex.
#Post - Member

Post(참조하는 쪽) -> Member(참조받는 쪽) 단방향 @Many(Post)ToOne(Member) : 포스트가 멤버를 참조함. => Post를 조회하면 어떤 멤버가 쓴 지 알 수 있음. 하지만 멤버를 조회한다고 해서 그 멤버가 쓴 모든 Post를 조회할 순 없음. 

@Many(Post)ToOne(Member) 이기에 Many인 Post에 FK가 생기고 Post가 관계의 주인이 됨.

Post(참조하는 쪽) - Comment(참조받는 쪽) 단방향 @One(Post)ToMany(Comment) : 포스트가 코멘트를 참조함. => Post를
조회하면 거기에 달린 댓글을 확인할 수 있음. 하지만 댓글을 조회한다고 해서 어떤 게시물에 달린 것인지 알 수 없음.

@One(Post)ToMany(Comment) 이기에 Many인 Comment에 FK가 생기고 Comment가 관계의 주인이 됨.


단위테스트

------------------------------------
개발순서

Repository => Dto => Service => Controller

: 이 순서대로 해야 can't solve 빨간줄 안 만나면서 개발을 할 수 있다.

DTO란? Data Transfer Object
프론트와 데이터를 주고 받을 때 엔티티에 있는 모든 데이터를 주고 받으면 효율적이지 않으니 프론트와 교환할 정보만 쏙 빼서 저장하는 오브젝트트


Lombok 어노테이션 정리
@Getter: get 메서드를 자동으로 만듬 ex. dto.getTitle()

@NoArgsConstructor: 기본 생성자(빈 생성자) 특히 Entity를 JPA에 등록할 때 꼭 필요함
@AllArgsConstructor: 명시된 모든 필드의 값을 생성자에 넣고 생성
@RequiredArgsConstructor: final이 붙은 애의 생성자 만들어줌


정적 팩토리 메서드란?

static으로 객체를 생성하는 메서드
그냥 생성자를 써도 되지만 나중에 복잡해지면 가독성이 좋아져서 쓰는 거 같음(이석원 피셜)

Global Exception : 모든 API마다 예외처리를 하면 중복도 많고 복잡해짐 => 글로벌하게 다루자





##Global Exception 처리 후 Swagger 에러가 발생하였음음

전역 예외처리 할 때 사용한 @RestControllerAdvice를 지우니 해결됨.
 Swagger와 충돌이 일어난 것 같음

--------------------------------------------------------
소개
JWT를 이용한 회원가입 로그인 구현

기능
- 회원가입(비밀번호 암포화, 권한 부여)
- 로그인(헤더를 통해 AccessToken 전달)
- 게시글 작성(토큰을 통해 유저 정보 확인 후 작성 가능)


유저 구현 방법

1.
2.
3.
4.