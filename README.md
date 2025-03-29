# spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project

ERD : https://www.erdcloud.com/d/imJTEt7o9kBRxLzeB

네이버 지식인 구조에 대해 좀 더 자세히 조사해본 뒤에 엔티티 구조를 확 바꾸었다.

---
## WIL3

API 명세서: https://heather-pink-26a.notion.site/API-1c1a28ca99b1808891f7f9a496664da8?p=1c4a28ca99b180668127f0e9af423c09&pm=s

일단 대충 만들고 싶었던 Api들에 대한 명세서인데 시간이 부족해서 댓글이랑 좋아요/싫어요 api는 완성하지 못한 상태이다.
구현하면서 좀 바뀐 부분이 있는데 힘들어서 명세서는 따로 수정 안할것같다

스웨거도 연동됐고, 다음과 같은 기능이 잘 수행되는 것을 확인했다.
- 중복회원가입 방지
- 질문과 답변 생성, 삭제, 수정
- 답변이 존재하는 게시글 삭제/수정 불가
- 각 게시글의 내용이나 해시태그 리스트 추가/삭제 반영
- 존재하는 해시태그 사용 시 해당 해시태그 사용, 없으면 새로 만들어서 사용

아직 만들지 못한 기능은
- 댓글 관련 기능
- 반응 관련 기능
- 아무도 사용하지 않는 해시태그 삭제

...등이 있다

-----
### Swagger
스웨거로 이미지 파일과 함께 RequestBody를 받으려고 했는데 `Content-Type 'application/octet-stream' is not supported` 에러가 떴다.
찾아보니 postman 에서는 각각의 part마다 Content-Type을 지정해줄 수 있어서,
RequestBody쪽의 타입을 application/json으로 설정해주면 되지만 스웨거에서는 이것이 불가능하기 때문에
파트 타입이 누락된 부분이 octet-stream으로 처리되어 들어오기 때문이라고 한다.

해결방법은 `application/octet-stream` 타입을 담당하는 컨버터를 커스텀으로 생성해주면 된다고 하여 그렇게 했다.

참고 블로그: https://seondays.tistory.com/79

### 예외처리
지금까지 누가 써둔거 사용만 하고 귀찮아서 원리를 뜯어볼 생각을 절대 안했는데 이번 기회에 해봤다.

일단 예외처리는 스프링의 AOP 기술의 대표격인것 같다.
`@Controller`나 `@RestController` 어노테이션을 사용한 곳에서 에러가 나면(point cut)
`@ControllerAdvice`나 `@RestControllerAdvice` 어노테이션이 적힌 곳으로 이동하여 에러를 처리하는 것.

~~사실 예전 동아리에서 에러 처리 관련 워크북을 줬었는데 안읽다가 지금 읽어봄~~

워크북에 ExceptionAdvice 외에도 파일이 무슨 ErrorStatus, ApiResponse, BaseCode,GeneralException 등 꽤 많아서 더 헷갈렸다.
대강 각 클래스들의 역할은 감을 잡고 있지만 정확하게 이게 어떻게 돌아간다라고 설명할 수 있는 정도는 아닌것 같아서 뜯어보기로 했다.


- **GeneralException** : 일반 적인 에러에는 내가 원하는 정보, 내가 읽기 쉬운 정보가 담겨 있지 않으니까
  내게 필요하고 읽기 쉬운 구조로 에러 정보를 담을 수 있는 Exception 객체를 만든 것.
  그래서 RuntimeException 을 상속받는다.


- **ErrorStatus** : GeneralException에 담을 내가 알고싶은 정보. 에러 정보는 엄청 많기 때문에
  Enum타입으로 만들고 각 enum들은 단순히 NOT_FOUND보다는 자세한 '코드'라는 필드를 가지고 있게한다.
  이 분류 코드에 자세한 정보를 적음으로서 Member쪽 에러인지, Comment쪽 에러인지 등을 쉽게 구분할 수 있게 한다.
  마지막으로 메시지는 에러가 났을 때 자연어로 사람이 읽기 편하게 "이미 존재하는 회원입니다" 등의 상황에 맞는 메시지를 넣는다.
 

- **ApiResponse** : Api 통신할 때 어떨 땐 이렇게 오고, 어떨땐 저렇게 오고 하면 프론트엔드 개발자는 너무 화가 난다.
  그래서 응답 형식을 통일해줘야할 필요가 있으므로 앞으로 Api응답을 반환할 때는 항상 ApiResponse 객체를 보낼 것이다.
  요청에 성공했을 때(Controller에서 정상 반환 시)는 ApiResponse 객체를 반환하는 스태틱 메서드인 `ApiResponse.onSuccess(result)`를 이용한다.
  - **ApiResponse.onFailure(code,message,result)** : 실패했을 때 쓰는 메서드니까 Advice 쪽에서 씀. 성공했을 때는 다 SUCCESS인 Status만 사용해서
    프론트에 제공해줄 데이터만 파라미터로 받았는데, 실패하면 실패 분류 코드도, 실패 메시지도 다 다르니까 이걸 다 파라미터로 받아야한다.
    예외가 발생해 전달해줄 데이터가 없을 가능성이 많지만 혹시의 상황을 대비해 result도 줄려면 줄 순 있다.

이렇게 분석은 끝냈는데 정작 Advice를 쓰려고 하니까 막막했다.

뭐 다른 사람들이 쓴 Advice 보니까 함수가 4-5개정도인데..
저게 그래서 각각 무슨 예외를 처리하는 처리 로직이지? 수많은 예외 종류 중 처리할 예외 몇가지를 정하는 기준은 뭐지?
하는 의문에 또 각 메서드를 뜯어봤다.

함수 로직은 대충:
>각 로직 위에 붙인 `@ExceptionHandler(value=특정 예외 클래스)` 어노테이션의 value 값에 따라
>에러 클래스에 맞는 로직이 실행된다. 로직 안에서는 해당 예외의 성격에 맞는 ErrorStatus를 지정해준 뒤에
> ApiRepsponse.onFailure에 그 값을 담아서 반환한다.

그리고 종류는:
>1. 데이터 검증 실패(MethodArgumentNotValidException)
>2. URL 파라미터 검증 실패(ConstraintViolationException)
>3. 리소스 없음(EntityNotFoundException)
>4. 비즈니스 로직 위반(GeneralException(커스텀 에러))
>5. 권한 없음(AccessDeniedException)
>6. 기타 모든 예외(Exception)

이런 식인 것 같다.
나는 이 전 프로젝트에서 3번과 5번은 모두 4번 내에서 처리했으므로
1,2,4,6 이렇게 만들었다.

그리고 기타 모든 예외를 처리하기 위해 `GlobalExceptionHandler`는 `ResponseEntityExceptionHandler` 라는 것을
상속받는게 좋다고 한다. 저건 스프링의 기본 예외 핸들러 중 하나인데, 이것을 상속 받고 응답을 커스텀함으로써
내가 명시하지 않은, 그리고 너무 많아서 다 명시하기도 너무 힘든 나머지 에러들을 묶어서 하나의 응답 형식으로 반환할 수 있다.

자세한 설명은 여기서 : https://dev.gmarket.com/83

