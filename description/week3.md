### 저번 주 ERD 고치기

![image]([https://github.com/hyesuhan/spring-knowledgeIn-21st/blob/hyesuhan/ceos사진/3주차 사진1.png](https://github.com/hyesuhan/spring-knowledgeIn-21st/blob/hyesuhan/ceos%EC%82%AC%EC%A7%84/3%EC%A3%BC%EC%B0%A8%20%EC%82%AC%EC%A7%841.png))


- 지식인을 다시 분석하니 …
    - 질문글에 해시태그를 다는데, 해시태그가 미리 묶여있는 것도 있고, 사용자가 알아서 넣을 수도 있음!
    - 질문글에는 좋아요 누를 수가 없고 댓글만 달 수 있다!
    - 답변 글은 질문 글이랑 비슷하지만 좋아요를 누를 수 있다
    - 답별 글, 질문 글 다 댓글을 달 수 있는데, 댓글에 대한 대댓글은 불가능하다!
    - 해시태그 와 질문글은 N:M 관계이므로 중간 테이블을 놔서 관리한다.
    - 위를 바탕으로 다시 erd를 만들었다!

정리하자면, 질문글, 답변글, 댓글을 다 따로 만들었다.

|  | 대댓글 유무 | 좋아요/싫어요 | 해시태그 |
| --- | --- | --- | --- |
| 질문글 | 가능 | 불가능 | 가능 |
| 답변글 | 가능 | 가능 | 불가능 |
| 댓글 | 불가능 | 불가능 | 불가능 |

은근 특성이 다르기 때문에 필요 없는 것까지 할 필요 없다 생각함

추가로 서치를 한다면 질문글의 제목 위주로 찾기 때문에 둘을 묶을 이유가 없다고 생각이 들었음

그리고 생각보다 답변글과 댓글은 형태 자체가 다름. (답변글은 이미지가 가능한데, 댓글은 이미지 없이 그냥 글만 간단히 쓰고 내 생각에는 글자 제한이 필요해보임.)

→ 이거 읽고 자유롭게 의견 부탁드립니다!!

### 2️⃣ 지식인의 4가지 HTTP Method API 만들어봐요

<aside>
<img src="/icons/forward_gray.svg" alt="/icons/forward_gray.svg" width="40px" /> 모델링 과제 결과 중 서비스에 가까운 모델을 우선으로 구현하면 좋습니다.
회원 모델은 4주차 Spring Security와 로그인 부분에서 구현할 예정으로 다른 것부터 구현해주세요.
URI는 모델에 따라 수정 부탁드립니다.

</aside>

- 나는 Question (질문글)에 대해서 CRUD를 작성했습니다!
- 질문글 생성, 모든 질문글 불러오기, 질문글 userId에 따라 불러오기, 해시태그로 불러오기,
- 질문글 삭제, 업데이트를 작성했습니다.
- Swagger로 통합 테스트만 진행했고 잘 나왔네요!
- 참고로 N:M 관계로 만들기 싫어서 저번에 중간 테이블 놨던 개념 이용해서 구현해 보았습니다. ㅎ


### 트러블 슈팅

![image]([https://github.com/hyesuhan/spring-knowledgeIn-21st/blob/hyesuhan/ceos사진/3주차 사진2.png](https://github.com/hyesuhan/spring-knowledgeIn-21st/blob/hyesuhan/ceos%EC%82%AC%EC%A7%84/3%EC%A3%BC%EC%B0%A8%20%EC%82%AC%EC%A7%842.png))


스웨거 설정 후 로그인 창으로 리다이렉트 되는 상황

지금 내가 security를 설정을 안 해줘서 그런데.. 귀찮으니까!

```
spring:
	security:
		user:
			name: root
			password:1234
```

음? 로그를 보니까 버전 문제라는데??

https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui

최신 버전으로 바꿔주었음요

에잇 내가 security 의존성 주입해놔서 그런거네 ㅡㅡ 어짜피 지금 안 쓸거니까 끕시당

++ 하다보니 또 문제 생김

어짜피 지금 user Security 관련해서 필요 없으니 아직은! 그냥 spring security 삭제해 주었습니다.

++ entity를 너무 잘 못짜서 어케할까 생각이 많았는데 ㅎ

여기까지 뭔가 시간이 엄청 오래 걸렸는데! 여기서 부터는 이제 로직은 다 비슷하니까~~ ㅎㅎ
