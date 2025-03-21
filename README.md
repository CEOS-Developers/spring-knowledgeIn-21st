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