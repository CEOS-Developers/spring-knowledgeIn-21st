# spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project

ERD : https://www.erdcloud.com/d/imJTEt7o9kBRxLzeB

과제에서 요구하는 주요 기능은 **게시글, 댓글, 대댓글, 좋아요/싫어요** 등이 있었다.

그런데 지식인을 살펴보았을 때, 답변에도 사진 등을 넣을 수 있어서
질문과 답변 모두 게시글의 성격을 띄고 있길래 게시글끼리 관계를 맺어야 하나 고민했었다.
하지만 사실 지식인에는 대댓글 기능도 없고 댓글에 대한 좋아요도 없어서 과제가 지식인이 작동하는 방식 그대로를 요구하진 않는다고 판단했다.
그래서 지식인을 그대로 구현하기 보다는 에브리타임같은 커뮤니티의 질문 게시판 같은 느낌으로 구현해야겠다고 생각했다.

- ERD 설명
    - 기본 엔티티: 댓글, 게시글, 회원, 해시태그, 이미지
    - 매핑 엔티티: 좋아요/싫어요, 포스트-해시태그 매핑

---
## WIL2
### Spring 패키지 구조
- **계층형**
  - controller, repository, service 등 스프링의 계층에 따라 폴더를 만들고 관리하는 방법
  - 초심자가 프로젝트 구조를 한눈에 파악하기 쉽다.
  - 하나 안에 여러 클래스가 모여서 구분이 어려워진다.
  - 도메인 별 응집도가 낮아서 어플리케이션을 기능별로 구별짓지 못한다.
- **도메인형**
  - 프로젝트에서 사용하는 도메인을 기준으로 폴더를 나누고 관리하는 방법
  - 예시: Member 폴더 아래 Member엔티티와 MemberController, MemberService 등을 둔다.
  - 도메인 별 응집도가 높아져 기능을 구분하기 좋다.
  - 프로젝트의 흐름을 따라가기 어렵고, 개발자에 따라 특정 기능을 어느 도메인으로 분류해야할지 애매한 상황이 생길 수 있다.

나는 이전 프로젝트에서 계층형 패키지 구조를 사용해보아서, 이번에는 도메인형 패키지 구조로 개발을 해보려고 한다.

### base entity
id, createdAt, updatedAt과 같은 속성은 모든 엔티티들이 공통적으로 가지는 속성이다.
따라서 추상 클래스로 선언해서 모든 엔티티들이 상속하도록 작성하면 편하다!

그리고 이 base entity를 상속하는 엔티티들이 생성되거나 변경되었을 때 이를 createdAt, updatedAt에 반영하기 위해
`@EnableJpaAuditing`이라는 어노테이션을 Spring 어플리케이션에 적용해줄 수 있다.(또는 따로 JPA config 파일을 만들어 해당 파일에서 적용시킬 수도 있다.)
base entity에는 `@EntityListeners`라는 어노테이션을 추가해주면 된다.

### 양방향 연관관계
실제로 데이터베이스에서는 외래키를 참조하고 있는 테이블과 참조되는 테이블이 JOIN을 통해 서로를 참조할 수 있다.
하지만 우리는 자바 코드로 데이터 베이스 구조를 설계하고 있는 것이기 때문에, 객체 간 참조를 이용해야한다.

그러나 자바코드에서는 외래키를 가지고 있는 객체만 자신이 참조하고 있는 객체에 대한 정보를 알 수 있다.
이를 해결하기 위해, 자신을 참조하는 객체를 알 수 있도록 '양방향 연관관계'를 사용한다.

게시글과 댓글의 일대다 매핑 경우를 살펴보자.

>Comment 객체에서 `@ManyToOne(fetch=FetchType.LAZY)`private Post post;
로 Post를 참조할 때, Post 객체에서는 `@OneToMany(mappedBy="post")` private List\<Comment> comments;
를 사용해 Comment를 참조할 수 있다.

이 때 mappedBy는 내가 연관관계의 주인(FK를 가지고있는 쪽) 내에서 어떤 변수이름과 연관관계를 맺고 있는지 알려주는 역할이다.
Comment 객체에서 post라는 이름의 변수가 Post 객체이므로 mappedBy = "post" 라고 적어주면 된다.

**하지만 양방향 연관관계를 무조건 적용하면 클래스의 복잡성을 늘릴 수 있으니,
단방향으로 성정한 뒤 양방향으로 설정하는 것이 필요한 경우에만 바꿔주는 것을 권장한다고 한다.**
### `@Data` 어노테이션
나는 @Data 어노테이션을 한번도 사용해본 적 없는데, 이번에 새로 사용해볼까 하고 조사를 해보았다.
- `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@EqualsHashCode`, `@ToString` 을 모두 포함하고 있는 어노테이션

이 부분만 읽었을 때는 굉장히 간편하고 좋아보였다. 그런데 왜 지금까지 주변에서는 아무도 안썼을까? 해서
단점을 찾아 본 결과...
- `@Setter` 어노테이션 남용으로 인해 안정성 위협
- `@RequiredArgsConstructor`는 필드 선언 순서에 따라 생성자의 파라미터 순서를 지정하는데,
  만약 같은 타입의 필드를 선언한 곳의 유지보수 과정에서 두 필드의 순서를 바꾸면 의도치 않은 상황이 발생할 수 있음

이 두가지가 나왔는데, 솔직히 첫번째 이유가 가장 큰 단점인 것 같다.
2번째 단점은 빌더 패턴으로 해결할 수 있을 것 같다.

그래서 나는 `@Data` 어노테이션은 사용하지 않는 쪽으로 결정했다!

### JPA 영속성 컨텍스트
테스트 코드를 짜면서 새로운 사실을 알게 되었다.
현재 post와 user가 서로 양방향 연관관계를 맺게 해놓았는데, post를 작성해도 해당 user의 postList 길이가 계속 0인 것이었다.
작성한 코드는 아래와 같다.

        //given
        Member member = Member.builder().name("박채연").email("pcy@naver.com").password("1234").nickname("사랑먼지").build();
        Member savedMember = memberRepository.save(member);
        Post post1 = Post.builder().title("테스트1").member(savedMember).content("테스트 해봅니다").isAnonymous(false).build();
        
        //when
        Post savedPost1 = postRepository.save(post1);
        Post foundPost1 = postRepository.findById(savedPost1.getId()).orElseThrow(()->new RuntimeException("해당 게시글을 찾을 수 없습니다"));
       
        //then
        Member foundMember = memberRepository.findById(savedMember.getId()).orElseThrow(()->new RuntimeException("회원을 찾을 수 없습니다"));
        Assertions.assertThat(foundMember.getPosts().size()).isEqualTo(1); <--실패하는 코드


    }
순간적으로 '이 부분에서도 연관관계 편의 메서드를 지정해줬어야 했나?' 하고 헷갈리기 시작했다.
그리고 당연하게도 이 전에 만든 프로젝트 코드를 뒤져도 그런 코드는 없었다.

이런 상황이 발생하는 이유는 **JPA 영속성 컨텍스트가 연관관계의 주인만 관리하기 때문**이다.
즉, Member에서 OneToMany로 Post를 참조하는 건 단순히 읽기 전용이며, 자신(member)을 참조하는 새로운 Post가 등록된다고 하더라도
member쪽의 필드 내용을 갱신하는 작업을 JPA 영속성 컨텍스트에서 해주지 않는다는 뜻이다.

그런데 예전에는 잘 업데이트 됐던 것 같은데.. 혹시나 해서 controller를 만들어서 post 저장 api와
해당 글 작성자의 postList size를 반환하는 api를 각각 만들어보았다.

### 잘 된다;;

왜 잘되는건지 검색이 잘 안돼서 챗 지피티한테 물어봤다 ㅎ..

>commit을 하면 DB까지 가서 member의 id와 동일한 user_id를 가지는 게시글을 조회한 후 하나하나 List<Post>에
저장하기 때문에(즉, DB에서 내용을 갱신해오기 때문) 잘 된다고 한다.

어.....왜??? commit 전에 flush 가 일어나고, 영속성 컨텍스트의 내용이 DB에 반영된다는 사실은 알고있다.
그러나 그 방향은 컨텍스트->DB지 DB->컨텍스트가 아니다.
게다가 영속성 컨텍스는 1차 캐시의 역할을 하는데, member는 이미 컨텍스트 안에 존재하므로
굳이 DB까지 갈 일이 없지 않나? post도 flush 전부터 컨텍스트 안에 있었고
뭐 바뀐 조건이 하나도 없는것 같은데...

그럼 바뀐 조건이 없는데 어떻게 DB에 간다는 걸까?

Commit에 대해 좀 더 알아보았더니,commit 후에 clear()를
통해 영속성 컨텍스트를 비워준다고 한다! 아..
이해완ㅜㅜ


flush는 트랜잭션을 commit 하기 직전에 자동으로 일어나며, 수동으로 해줄 수도 있지만 트랜잭션 외부에서 알 수 없다고 해서 지양해야하지 않을까 싶다.


관련해서 참고하면 좋을 것 같은 블로그 글이다: https://kth990303.tistory.com/343