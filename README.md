# spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project


## 설계

DB와 JPA를 짬뽕시키며 생각 정리한 그림

<img width="573" alt="Image" src="https://github.com/user-attachments/assets/87cc0293-a0c6-471f-ba83-48d6647f5325" />

그림에서 싫어요/좋아요 여부는 <br>
boolean으로 그렸는데<br>
구현하면서 Enum으로 변경했다.

빨간색 글씨로 적은 부분은 cascade 관련인데<br>
밑에서 살펴보자.

과제에 작성된 것 외에 디테일은<br>
- 생성, 업데이트 시간 기록 (BaseTimeEntity)
- 조회수
- 닉네임 공개 / 비공개
- 채택 여부
- 대댓글 자기 참조
- 사진 업로드명, storage명 분리
- cascade

<img width="497" alt="Image" src="https://github.com/user-attachments/assets/0bb6f2b9-0dca-492f-87da-226964217e54" />

개인적으로 좀 덤벙대고 잘 까먹어서..<br>
그림으로 그리는게 최고

## 네이버 지식인

<img width="535" alt="Image" src="https://github.com/user-attachments/assets/c7a61dc2-abec-42bd-b761-6f449e53c027" />

- 제목
- 내용
- 작성자명 공개/비공개
- 작성일
- 해시태그
- 조회수


<img width="902" alt="Image" src="https://github.com/user-attachments/assets/983def4d-0f80-40c9-8a91-73886320f42d" />

- 댓글
- 대댓글
- 싫어요/좋아요
- 작성일

## Cascade

이번 과제에서 주요 고민 포인트!

<img width="464" alt="Image" src="https://github.com/user-attachments/assets/fa085381-e296-4971-b93f-b7a0f1050879" />


<img width="537" alt="Image" src="https://github.com/user-attachments/assets/a66cb776-ca62-4d11-93fa-618446984376" />


### Remove

하나씩 보면

유저 탈퇴 - soft delete를 위해 모두 remove를 걸지 않았다.<br>
조금 기획적인 측면이기는 하지만,<br>
많은 서비스에서 가입 약관에 적어놓고<br>
탈퇴해도 데이터를 보관한다.

유저가 우리 서비스에 작성해준 글도 자산!이라고 생각한다.<br>
굳이 지우고 탈퇴하는게 아니라면 우리가 삭제해줄 이유가 없다.

글 삭제 - 글 삭제시 사진은 같이 삭제<br>
글이 없어진 사진은 의미가 없는 것 같아서 cascade remove를 걸었다.

답변을 다는 입장에서 글이 삭제 되었다고<br>
내가 달았던 답변을 못보면 조금 이상할 것 같아서 걸지 않았다.

해시태그도 remove를 걸지 않았다.<br>
마찬가지로 해시태그로 최근 인기 있는 주제가 무엇인지<br>
쉽게 파악할 수 있는 지표이고 좋은 자산이기 때문에<br>
지우지 않는게 좋다고 생각했다.

댓글 삭제 - 해당되는 대댓글은 같이 삭제<br>
댓글에 달린 싫어요, 좋아요 같이 삭제<br>
이게 제일 고민할 거 없이 직관적이다.

잘 되는지 테스트를 해보면

```java
    @Test
    @DisplayName("Cascade 작동 확인: Post 삭제시 PostImage도 삭제")
    @Transactional
    void deletePostAndCascadeDeleteImages() {
        // given
        postRepository.delete(userPost);
        // when
        Optional<Post> findPost = postRepository.findById(userPost.getId());
        // then
        assertThat(findPost).isEmpty();
        userPost.getPostImages().forEach(image -> {
            Optional<PostImage> findPostImage = postImageRepository.findById(image.getId());
            assertThat(findPostImage).isEmpty();
        });
    }
```


### Persist

글을 작성하면 대부분 해시태그나 사진을 같이 업로드한다.<br>
이건 바로 코드를 보자

```java
    @Test
    @DisplayName("Cascade 작동 확인: 연관관계가 있는 Post save")
    @Transactional
    void savePostTestWithRelation() {
        // given
        List<PostImage> imageList = make2PostImages();
        Post newPost = makeTestPostWithImages(imageList);
        // when
        Post savedPost = postRepository.save(newPost);
        // then
        assertThat(newPost).isEqualTo(savedPost);
        assertThat(savedPost.getPostImages().size()).isEqualTo(2);
    }
```

Post만 save 했는데?<br>

<img width="636" alt="Image" src="https://github.com/user-attachments/assets/18def7d6-0532-418b-8e84-ff2ae83a9acc" />

<img width="1243" alt="Image" src="https://github.com/user-attachments/assets/79522c91-96a3-4984-abe4-7017acc332e7" />

insert 쿼리가 잘 나가는 것을 볼 수 있다.<br>
cascade 안걸어주면<br>
post image 하나 하나 save save save 해야한다.

## 결과

Comment는 데이터베이스 예약어로 걸려 있다고 해서 <br>
Reply로 이름을 변경했다

<img width="451" alt="Image" src="https://github.com/user-attachments/assets/b5e8e919-3400-47c4-af71-3062c19aa926" />

커버리지

<img width="709" alt="Image" src="https://github.com/user-attachments/assets/6d5b5d75-d6a5-4643-a911-c459193aa6ac" />

## 개인적인 생각 - 실무

직접 경험을 한 것은 아니지만<br>
이야기를 조금 들어보면 실무는 우리가 공부하는 것과 조금 다르다고 한다.

특히 JPA 연관관계 매핑이나 DB foreign key, cascade 설계 부분

<img width="1007" alt="Image" src="https://github.com/user-attachments/assets/b43f50c8-3eda-4a4a-bece-f737c4c14348" />

<br>
fk가 걸려 있으면 테이블 삭제, 이동이 힘들다. (아마 경험이 있을 것)<br>
또한 운영 환경에서 데이터가 잘못 삭제되거나<br>
정합성이 깨지는 이유로 fk를 잘 잡지 않는다고 한다.

마찬가지로<br>
JPA를 사용할때 연관관계를 잘 잡지 않을 것 같다.

이번 과제에서 JPA를 공부해보기 위해<br>
연관관계를 모두 잡았지만, 다른 방법을 사용하는게 좋을 것 같다. 
무엇보다 연관관계로 스트레스 안받고 개발할 수 있다.

나의 경우 느슨한 결합 (DB) + 단방향 매핑 (JPA)<br>
방법을 애용하고 있다.


<img width="591" alt="Image" src="https://github.com/user-attachments/assets/7de38332-bb63-46dc-ade1-a413b51feb70" />

<br>
<br>

연관관계가 없는 것으로 보이지만<br>
pk가 아닌 식별자를 만들어서<br>
해당 컬럼을 참조하고 있다.

이렇게 구현해도 Data Grip을 사용한다면<br>
virtual foreign key를 지원하기 때문에<br>
다이어 그램으로 참조 관계를 확인할 수 있다.

인텔리제이에서 이런 기능을 만들어 놓은것이<br>
많이들 이렇게 한다는 증거가 아닐까?

<img width="195" alt="Image" src="https://github.com/user-attachments/assets/2b5beee0-3210-43b5-ae13-109fb7ae21f0" />

<br>
<br>

<img width="744" alt="Image" src="https://github.com/user-attachments/assets/ba8c2b3e-4fea-4e72-b232-cc1cc0b4b3b8" />


## PUT VS PATCH



<https://mangkyu.tistory.com/251>


## Path variable VS Query parameter



## DELETE : Soft delete & Response

<https://hongong.hanbit.co.kr/http-%EC%83%81%ED%83%9C-%EC%BD%94%EB%93%9C-%ED%91%9C-1xx-5xx-%EC%A0%84%EC%B2%B4-%EC%9A%94%EC%95%BD-%EC%A0%95%EB%A6%AC/>


## DTO 불변성 feat. Record


Record type
- final 제한자 추가
- 생성자
- equals
- getter
- hashcode, toString

## N+1 문제를 실전적으로 해결해 보자!

결론부터

나의 경우 default batch size를 걸고 일단 넘어간다.<br>
스트레스 받으면서 모든걸 튜닝하고 싶지 않기도 하고.. 그정도로 필요하다고 생각하지 않는다.<br>
병목이 발생해 문제가 생기면 그 부분만 튜닝하면 그만!<br>

default batch size는 한번에 가져오는 레코드 수를 뜻하고<br>
fetch join만큼 강력하게 한방 쿼리는 아니지만<br>
N+1을 1+1로 해결하는 느낌이다.

다시말해 연관관계가 x가 있으면 x+1<br>
나의 경우 post에 연관관계가 image, hashtag, reply<br>
3+1 -> 4번의 쿼리가 나갈것이다.<br>

실제로 확인해 보자

<img width="1559" alt="Image" src="https://github.com/user-attachments/assets/9b8352e0-2ef0-4104-a008-c74edf201545" />


14개의 post, 3개의 이미지, 7개의 해시태그를 모두 불러오는데 쿼리 딱 4번이면 괜찮은 것 같다.<br>
(내가 설정한 100개를 넘지 않으면 계속 4번으로 유지)

```
select p1_0.id,p1_0.content,p1_0.created_at,p1_0.deleted_at,p1_0.nickname_public,p1_0.title,p1_0.updated_at,p1_0.user_id,p1_0.view_count from post p1_0 where (p1_0.deleted_at IS NULL);
select i1_0.post_id,i1_0.id,i1_0.created_at,i1_0.reply_id,i1_0.storage_url,i1_0.updated_at,i1_0.upload_file_name from image i1_0 where i1_0.post_id in (2,3,4,5,6,7,8,9,10,11,12,13,14,15,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
select ht1_0.post_id,ht1_0.id,ht1_0.tag from hashtag ht1_0 where ht1_0.post_id in (2,3,4,5,6,7,8,9,10,11,12,13,14,15,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
select r1_0.post_id,r1_0.id,r1_0.accepted,r1_0.content,r1_0.created_at,r1_0.parent_id,r1_0.updated_at,r1_0.user_id from reply r1_0 where r1_0.post_id in (2,3,4,5,6,7,8,9,10,11,12,13,14,15,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
```

## 다시 돌아온 AOP - 공통 응답 포맷 + 예외처리

** AOP라고 했지만 Spring AOP를 사용하는 기술이 아님<br>
**응답 포맷에 대한 관심사**에 대해 논하는 것이기 때문에 AOP라고 표현!!


프론트엔드와 협업해 본 경험이 있다면,<br>
통일된 공통 응답 포맷으로 주세요~<br>
라는 요청을 들어본 적이 있을 것이다.

보통 ApiResponse(CommonResponse)를 응답으로 사용해 이를 해결했을텐데,<br>
RestControllerAdvice를 활용해 처리하는 방법에 대해 소개해 보겠다!!

이번에도 가상의 상황을 세워보고 좋은 해결책에 대해 생각해 보자<br>

우리는 백엔드 팀장이고<br>
다른 부서에서 공통 응답 포맷 요구사항이 있어 열심히 Api Response를 만들어<br>
팀원들에게 이 포맷을 사용해 달라고 했다.

하지만 팀원들의 코드는 다음과 같았다.

```java

@GetMapping("/test")
public String test(){
    return "test";
}

@GetMapping("/test2")
public ResponseEntity<String> test2(){
    return ResponseEntity.status(OK).body("test2");
}

@GetMapping("/test3")
public ResponseEntity<CommonResponse<String>> test3(){
    return ResponseEntity.status(OK).body(new CommonResponse<>());
}
```

누구는 String, 누구는 ResponseEntity, 누구는 ApiResponse를 body로..

실행결과<br>
<img width="842" alt="Image" src="https://github.com/user-attachments/assets/712d5990-865a-4060-982d-6f49a074cbeb" />

<img width="834" alt="Image" src="https://github.com/user-attachments/assets/ecebef9e-ee91-470c-a674-fbdcd0ba3bdd" />

<img width="856" alt="Image" src="https://github.com/user-attachments/assets/66674c23-0e05-4f10-93b8-c1736929f785" />

우리는 이걸 어떻게 해결해야 할까?<br>
팀원들을 한명 한명 찾아가 수정을 부탁해야 할까?<br>
시간도 많이 들고 한명 한명 말하며 갈등이 생길 가능성은?

그리고 방심한 사이에 코드가 push 되어 다른 부서에서 이상한 응답을 이미 봐버렸다면?<br>
책임은 누가 져야 할까?<br>
아마 팀장인 우리가 지게 될 것이다.. 

나는 기술적으로 안전장치를 만들어<br>
프로젝트를 성공적으로 이끄는 것이 최고라고 생각한다.<br>
이제 코드를 보자
```java

@RestControllerAdvice
public class ResponseInterceptor implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof CommonResponse) {
            return body;
        }

        int status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();

        // swagger 제외
        String path = request.getURI().getPath();
        if (path.contains("swagger") || path.contains("api-docs") || path.contains("webjars")) {
            return body;
        }

        // 조건부 메시지 처리: 2xx -> "Success", 그 외 -> "Error"
        String message = (status >= 200 && status < 300) ? "OK" : "Error";

        CommonResponse<Object> commonResponse = new CommonResponse<>();
        commonResponse.setStatus(status);
        commonResponse.setMessage(message);
        commonResponse.setData(body);

        // 응답을 String으로 내는 경우 따로 예외처리
        if (body instanceof String) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(commonResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("String response conversion error", e);
            }
        }
        return commonResponse;
    }

}
```
응답 종류와 상관 없이 응답을 가로채<br>
공통 응답 포맷으로 만들어주는 코드이다.

적용 후 실행결과

<img width="851" alt="Image" src="https://github.com/user-attachments/assets/3a35fdfd-0cb8-4a39-ba0d-6da70548da45" />

<img width="852" alt="Image" src="https://github.com/user-attachments/assets/af922fa5-7f60-484e-a756-289c07ccd182" />

<img width="825" alt="Image" src="https://github.com/user-attachments/assets/1ef47ee3-9cf9-45af-a8a8-681560afd468" />

세가지 방식 모두 통일된 응답 포맷으로 나간다.

이를 적용한 지식인 프로젝트 응답은 이렇게 생겼다
```json
{
  "status": 200,
  "message": "OK",
  "data": [
    {
      "title": "string",
      "content": "string",
      "viewCount": 9007199254740991,
      "nicknamePublic": true,
      "images": [
        {
          "storageUrl": "string",
          "uploadFileName": "string"
        }
      ],
      "hashTags": [
        {
          "tag": "string"
        }
      ],
      "replies": [
        {
          "content": "string",
          "accepted": true,
          "images": [
            {
              "storageUrl": "string",
              "uploadFileName": "string"
            }
          ],
          "replyChildren": [
            {
              "content": "string"
            }
          ]
        }
      ]
    }
  ]
}
```
예외처리도 마찬가지

```json
{
    "status": 400,
    "message": "Error",
    "data": {
        "path": "/api/post/v1",
        "messageDetail": "요청 본문이 누락되었거나 올바르지 않습니다.",
        "errorDetail": "Required request body is missing: public org.springframework.http.ResponseEntity<com.ceos21.knowledgein.post.dto.PostDto> com.ceos21.knowledgein.post.controller.PostController.createPost(com.ceos21.knowledgein.post.dto.request.RequestCreatePost,java.lang.Long)"
    }
}
```
```json
{
    "status": 404,
    "message": "Error",
    "data": {
        "path": "/api/post/v1/123",
        "messageDetail": "게시글을 찾을 수 없습니다.",
        "errorDetail": null
    }
}
```

<img width="855" alt="Image" src="https://github.com/user-attachments/assets/c6dd4639-4ad6-48b7-8e34-b83964d4fcb9" />

<img width="855" alt="Image" src="https://github.com/user-attachments/assets/654518bc-6bbd-4ce7-8c45-bc9d5007bdaa" />

곧 있을 아이디에이션 프로젝트를 개발할때<br>
프론트엔드에게 이렇게 응답을 깔끔하게 주면 엄청나게 사랑 받을 것 같지 않은가?!?<br>
거기다 실수를 방지할 수 있는 시스템으로 백엔드 팀원의 사랑은 덤<br>

우리 모두 다른 팀원에게 사랑받는 개발자가 되자! 

## 결과

Create

<img width="790" alt="Image" src="https://github.com/user-attachments/assets/cebb9f60-c896-4113-89fc-fa2da28bd402" />
<img width="489" alt="Image" src="https://github.com/user-attachments/assets/86d0ba0e-28b8-4a1b-836d-833aa5d4df49" />

현재 업로드한 이미지는 로컬에 저장, 베포할때 경로를 버킷으로 변경하면 됨 


Read

<img width="688" alt="Image" src="https://github.com/user-attachments/assets/cc497900-75d7-45e9-9d63-ce0928d20283" />
<img width="804" alt="Image" src="https://github.com/user-attachments/assets/4c18902a-daa2-4b67-8dfb-ebaad1f7f5ad" />

Soft Delete

<img width="431" alt="Image" src="https://github.com/user-attachments/assets/0145d1ef-d744-4fc5-80e7-fa4175214388" />
<img width="597" alt="Image" src="https://github.com/user-attachments/assets/41634786-f4df-4075-a15b-85b02ac58f1d" />

시간 부족이라는 핑계로 인해 로깅+테스트 코드 작성은 못했다<br>
다음에 꼭 작성하겠습니다ㅜㅜ

## 인증 방식

### 세션 인증 방식

로그인을 하면 세션 저장소에 사용자 세션을 저장하고 <br>
세션 ID를 쿠키로 클라이언트에게 전송하는 방식.<br>

쿠키는 브라우저상에서 작동하고 자동적으로 요청에 포함되기 때문에 엄청 간편하다!!<br>
Spring Security 기본 구현 방식이 세션 인증 방식이다.<br>

하지만 세션 저장소에 세션을 저장하기 때문에 부하가 생길 수 있고 <br>

쿠키를 사용하지 못하는 환경이라면? <br>
즉 브라우저를 기반으로 작동하지 않는다면? <br>

사용할 수 없다.<br>
특히!! 분산 환경에서 모든 서버에 똑같은 세션을 저장해야 하기 때문에 오히려 관리가 힘들다<br>
이를 해결하기 위해 나온 것이 JWT이다.

### JWT

JWT가 무엇인지는 인터넷에 너무너무 많으니 넘어가고<br>
처음 접했을때 refresh token, black list, rotate refresh, refresh token 서버 저장, refresh token 클라이언트에게 전송 등등..<br>
너무 많은 내용과 다양한 구현 방식 때문에 혼란스러웠는데<br>
겪었던 과정 속에서 고민했던 내용들과 개인적인 생각을 적어 보겠다.

### JWT - Access Token

JWT 토큰을 발급하고 나면 이 토큰이 우리가 발급한 것이 맞는지 수학적으로 검증하고, 만료되었는지만 검사한다.<br>
좀 어렵게 말하면 Verify Signature 부분을 우리의 Secret key로 복호화 해 조작되었는지 검증하는 것이다.<br>
이게 코드상에서 우리가 구현한 Signature Exception을 발생시키는 부분이다.
쉽게 말하면 누구에게 발급했는지 기억하지도 않고, 검증하지 않는다는 것.<br>

그럼 누구에게 발급한 토큰인지 확인하지 않기 때문에 토큰이 털린다면 해당 access token이 유효한 시간동안 계속해서 공격을 받을 수 밖에 없다.<br>
이 해결 방법으로 access token의 유효 시간을 짧게 설정 하는 방법이 고안된 것이다. (예를들어 30분)<br>
하지만 사용자는 이 경우에 30분마다 다시 로그인을 해야 하기 때문에 불편할 것이다.<br>
그래서 나온 것이 Refresh Token.

### JWT - Refresh Token: 클라이언트에게 전송

Refresh Token은 Access Token을 발급할 때 함께 발급되는 토큰이다.<br>
refresh token은 access보다 유효기간이 길고(예를들어 1달)<br>
사용자가 refresh token을 가지고 있다면 만료된 access token을 재발급 받을 수 있다.<br>
즉 다시 로그인 하지 않아도 refresh token 유효기간 동안 세션을 유지할 수 있는 것이다.<br>
(정확히는 세션이 아니지만 세션이라고 표현하겠다)

여기서 refresh token을 가지고 있다면 -> 이 말은 클라이언트가 우리(서버)에게 계속해서 전송을 해야 한다는 뜻이니<br>
Refresh token을 쿠키에 실어 전송하고, access token을 헤더에 실어 전송하는 방식으로 구현할 수 있다.<br>
(검색하면 이 자료도 참 많이 나왔던 것 같다)

그런데 처음에 JWT가 나온 배경에서 쿠키를 사용하지 못하는 환경을 해결하기 위해 도입한 것인데,<br>
refresh token을 위해 쿠키를 사용한다는 것이 조금 이상하지 않나? 생각이 들었다.<br>

그래서 개인적으로 access, refresh 모두 헤더에 실어 전송하는 방식이 더 합리적이라고 생각한다.<br>

그런데 한가지 더!<br>
Access token이 탈취될까봐 유효기간을 짧게 설정한 것인데<br>
refresh token을 클라이언트에게 전송한다면 탈취당할 위험이 있는거 아닌가?<br>
그럼 오히려 더 긴 기간동안 공격받는거 아닌가??<br>

그래서 나온 방법이 blacklist, rotate refresh token이다.<br>

### JWT - Refresh Token: Rotate And BlackList

이 방법은 refresh token 탈취를 대비해 access 토큰을 재발급할 때마다 새로운 refresh token을 발급하고<br>
기존 refresh token을 블랙리스트에 등록하는 방법이다.<br>

로직으로 설명하면
- access token과 refresh token을 발급한다.
- refresh token으로 재발급 요청들어오면
- refresh token이 블랙리스트에 있는지 확인
- 블랙리스트에 없다면 새로운 refresh token과 access token을 발급
- 기존 refresh token을 블랙리스트에 등록
- 추가로 로그아웃시 refresh token을 블랙리스트에 등록

그럴듯 한지만?<br>
Refresh Token을 저장한다는 것 자체가 이상하다..<br>
생각해보면 토큰 관련해서 저장하지 않는 Stateless 방식이 JWT의 장점인데<br>
여기부터 그 장점이 없어지기 시작한다.<br>

그리고 로직도 무지 복잡하다<br>
이렇게 토큰을 저장할거라면 그냥 refresh token을 서버에 저장하는게 낫지 않나?<br>
그래서 refresh token을 서버에서 관리하는 방법이 나왔다.

### JWT - Refresh Token: 서버 저장

이 방법은 Refresh token을 클라언트에게 보내지 않고 서버에 저장하는 방법이다.<br>
사용자 id와 refresh token을 매핑해 저장하고 access token을 재발급할 때 refresh token이 있는지 확인하는 방법이다.<br>
대신 부하를 줄이기 위해 refresh token 저장에 Redis를 많이 사용한다.

위 방법보다는 간단하지만 사실 세션 인증 방식이랑 다를게 없다..<br>

개인적으로 내린 결론은<br>
모놀로식 + 브라우저를 사용하는 웹 서비는 세션 인증 방식<br>
분산 환경 or 앱을 개발한다면 JWT를 사용하는게 적절하다고 생각한다.


## 회원가입 / 로그인

회원가입은 직접 엔드포인트를 만들고 OAuth도 간단하게 추가했다.
각각 구현할 것은 
- Controller, Service
- OAuth dto, OAuth2UserService (핸들러 재사용)

![](https://velog.velcdn.com/images/grammi_boii/post/02a026a9-9fcd-4d50-8538-fd09f4b2aee7/image.png)

로그인은 UsernamePasswordAuthenticationFilter를 상속받아 구현했다. <br>

```java
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken authenticationToken;

        try {
            RequestLogin requestUser = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            authenticationToken = new UsernamePasswordAuthenticationToken(requestUser.email(), requestUser.password());
        } catch (IOException e) {
            throw new AuthenticationServiceException("Authentication failed", e);
        }

        setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
```

successfulAuthentication 메서드를 직접 구현하는 방법도 있는데, 나는 클래스를 분리하는걸 좋아하기도 하고 OAuth success handler와 로직이 동일하기 때문에
분리하고 스프링 빈으로 등록해 재사용했다.

![](https://velog.velcdn.com/images/grammi_boii/post/dc9fd2c7-d3f2-45ce-bc0c-c7258d85625e/image.png)

![](https://velog.velcdn.com/images/grammi_boii/post/2eef79b8-3ca6-432b-8c2b-2fee3c215593/image.png)

## 테스트

토큰이 없을때<br>
![](https://velog.velcdn.com/images/grammi_boii/post/745aecf1-e686-472d-a624-58a4654b5842/image.png)

토큰 있을때<br>
![](https://velog.velcdn.com/images/grammi_boii/post/69f73898-dc5f-42fd-8a2b-a39a66b97716/image.png)

### Postman 자동화

처음에 로그인 후 받은 access token을 복붙을 참 많이 했는데<br>
이번에 자동화 시키는 방법을 알게 되었다!.!

![](https://velog.velcdn.com/images/grammi_boii/post/6bd0ad76-1a23-4656-a7a8-46308e00e70c/image.png)

Environments에서 환경변수 만들어 주고<br>

![](https://velog.velcdn.com/images/grammi_boii/post/6aacfc0b-af13-40f8-82db-331babcfb97a/image.png)

컬렉션에서 환경변수 선택

![](https://velog.velcdn.com/images/grammi_boii/post/a8ca01c3-79af-4a66-9637-5176c47f0d84/image.png)

토큰을 받아오는 엔드포인트 (로그인)에 script를 적어준다<br>
```javascript
var token = pm.response.headers.get("access");
pm.environment.set("access", token);
```

![](https://velog.velcdn.com/images/grammi_boii/post/faff45ad-02dd-4937-9280-11cf0040e8a5/image.png)

마지막으로 access token이 필요한 엔드포인트에 하드코딩하지 말고 {{access}}로 적어준다<br>
아주 편하다


### docker 적용

특별한 건 없었다!<br>
docker compose를 사용했는데 컨테이너끼리 포트 지정해주는 부분이 헷갈렸어서 정리해봤다.<br>
![](https://velog.velcdn.com/images/grammi_boii/post/d95ae0c5-8c3b-490f-a4a7-70e9762e9e80/image.png)

로컬에서 db가 5432 포트를 사용하고 있으니<br>
같은 db 컨테이너를 띄우기 위해서는 5431 포트를 사용했고<br>
Redis도 마찬가지로 6379를 사용하고 있으니 6378로 포트 포워딩을 해주었다.<br>

그렇다면 8080:8080으로 포워딩한 백엔드 컨테이너는 도커 db, 도커 redis를 사용하기 위해서 어떤 포트를 사용해야 할까?<br>
그림처럼 5432, 6379이다.<br>
사실 생각해보면 간단한데 도커 네트워크 안에서 통신하는 거니까..<br>


### docker-compose.prod

application-prod 처럼 profile을 분리하는 것이라고 한다.<br>
이와 관련해서<br>
\1. DB 컨테이너 삭제 2. nginx 구현 3. Docker hub <br>
3가지가 젹혀있는데 이게 무슨 말인지 설펴보면서 내 생각을 정리해보겠다



### Docker 실제 활용

1. DB 컨테이너 삭제<br>
AWS로 생각해보면 하나의 ec2 인스턴스 안에서 컨테이너 3개(spring, db, redis) 모두가 돌아가고 있는 상황.<br>
CPU, Memory 사용량이 남아나지 않을 것이다.<br>
첫 프로젝트를 이렇게 했다가 서버가 계속 죽었던 기억이 난다.<br>
2. Nginx 구현<br>
로드밸런싱을 위해 사용.<br>
ELB로 대체할 수 있다고 생각했는데, 작동하는 계층이 다르기 때문에 둘다 사용하는게 좋고 일반적이라는 글을 봐서 뒤에서 생각과 검증을 해보겠다.
3. Docker hub
도커 허브에 이미지를 올려 배포하는 방법이다.<br>
이러면 prod 환경에서는 docker hub pull, docker compose up -d, docker image prune -f만 해주면 된다.<br>
이것도 마찬가지로 첫 프로젝트때 백엔드 prod 인스턴스 안에서 git clone, git pull, build image까지 했던 기억이 난다..<br>
지금 생각해보면 git action을 사용하면 git action상에서 빌드하고 docker hub push 하거나 jenkins를 사용한다면 CI 서버에서 빌드, push 하는게 맞다.

정리하면, prod 환경에서는 RDS 같은 제품을 사용해 DB를 따로 관리하고<br>
로드밸런싱 신경써주고<br>
CI 서버는 따로 두자.

### Web Server, Web Application Server

완전 도커에 관련된 내용은 아니지만 엔진엑스를 보다가 문득 생각이 나서 정리해봤습니다

![](https://velog.velcdn.com/images/grammi_boii/post/463b587b-9d8f-42a7-8006-ac227682cd45/image.png)

구글에 검색하면 이런 그림이 나오는데,<br>
Web Server는 정적 자원을 제공하고, WAS는 동적 자원을 제공한다고 나와 있다.<br>
정적 자원은 HTML, CSS, JS, 이미지 등등<br>
그래서 Webserver의 예시로 Nginx, Apache가 나와있고<br>
WAS의 예시로 Tomcat, Jetty 등이 있다고 한다.

약간 와닿지 않는데, 계산이 들어가는 등 비즈니스 로직을 처리하는 놈은 WAS이고<br>
WAS는 WebServer가 하는 일을 수행할 수 있지만<br>
고급 기능을 수행할 수 있는 WAS 자원이 아까우니 서빙만 하면 되는 일은 WebServer에게 시키자는 것으로 생각하면 될 것 같다.<br>

#### Tomcat, Spring, SpringBoot

처음 이를 보고 든 의문은 우리가 개발하는건 Spring인데 왜 WAS가 tomcat이지? 였다<br>
~~사실 너무 당연하다고 생각하는 분들이 있을 수 있지만 ㅎㅎ<br>~~

![](https://velog.velcdn.com/images/grammi_boii/post/81d75442-f732-4b1b-b0ec-ef97ba579d02/image.png)

이부분은 김영한님의 스프링 부트 강의를 듣고 조금 이해가 되었다. - 이 강의 추천 (후반부에는 스프링 액추에이터, 그라파나, 프로메테우스 등 모니터링 내용도 나옴!)<br>
여기서 직접 war로 빌드해 톰캣 서버에 스프링 파일을 올리는 방식으로 배포하는 경험을 해봤다.<br>
그러고 나니 톰캣이 내장되어 있다, 즉 내장 서버가 있다라는 말때문에 **스프링 안에 톰캣이 있다고 착각**했다는 사실을 알게 되었다.<br>

![](https://velog.velcdn.com/images/grammi_boii/post/9d686adf-9b08-43fd-964b-0fe59eb6edaa/image.png)

그냥 Tomcat 소프트웨어 의존성을 포함하고 있는거라고 생각하면 될 듯 하다.<br>

강의에서는 외장 톰캣 설치<br>
서블릿 컨테이너 초기화를 사용해 필요한 서블릿을 등록<br>
스프링 컨테이너를 생성해서 등록<br>
스프링 MVC가 동작하도록 DispatcherServlet을 등록<br>
흐름으로 진행된다.<br>

내장 톰캣은<br>
위 과정들은 똑같지만 톰캣을 의존성으로 관리<br>
이를 jar 파일로 관리하는데, jar(spring) 내에 jar(tomcat)을 포함할 수 없기때문에 Fatjar 사용<br>
내장 톰캣 실행, 스프링 컨테이너 생성, 디스패처 서블릿 등록등 모든 과정을 처리해 주는 부트 클래스를 만듬<br>
이것이 바로 Spring Boot

빠르게 강의를 보며 여기까지 이해했었는데, 이번 기회로 이론적인 내용을 조금 더 파보겠다<br>
특히 서블릿을 등록한다는게 뭔지, DispatcherServlet이 뭔지<br>

HTTP 요청을 처리하기 위해 Servlet 인터페이스를 구현후 서블릿 컨테이너에 등록해야 정상적으로 작동한다.<br>
옛날에는 이를 web.xml 파일에 직접 등록했다.<br>
즉 HTTP 요청을 처리하기 위한 하나의 방식인 것이다.<br>
하지만!! Spring MVC가 등장하면서 요청을 Controller 단에서 처리하기 시작했고 DispatcherServlet이 등장했다.<br>
DispatcherServlet은 Front Controller 패턴을 사용해 HTTP 요청을 처리하는 서블릿이다.<br>

쉽게 말해 디스패처 서블릿은 핸들러 매핑 정보를 스캔해 요청에 적절한 **서블릿 OR 컨트롤러를 호출**해 주는 것.<br>
즉 우리는 서블릿을 등록할 필요 없이 @Controller로 스프링 빈을 등록해 놓으면<br>
디스패처 서블릿이 해당 컨트롤러를 호출해주는 것이다.<br>

![](https://velog.velcdn.com/images/grammi_boii/post/eb9847af-686d-43ac-8de9-e44f06f4a3fb/image.png)

이 그림이 가장 이해가 잘 가는 것 같다.<br>

### DispatcherServlet

요청을 처리할 컨트롤러를 찾아서 위임(호출) 한다는 것이 뭘까?<br>
과정은 이렇다고 한다.
1. 클라이언트 요청을 디스패처 서블릿이 받음
2. 위임할 컨트롤러를 찾음
3. 요청을 컨트롤러로 위임할 핸들러 어댑터를 찾아 전달
4. 핸들러 어댑터가 컨트롤러로 요청을 위임
5. 비즈니스 로직 처리
6. 컨트롤러가 반환값을 반환
7. 핸들러 어댑터가 반환값 처리
8. 서버 응답을 클라이언트에게 반환

같은 순서로 좀더 자세하게 보자 (보실 분들을 위해 : tomcat-embed-core 패키지에 있습니다)

1. 클라이언트 요청을 디스패처 서블릿이 받음<br>
    1-1. request, response를 캐스팅
      ![](https://velog.velcdn.com/images/grammi_boii/post/5799b799-56d8-4bfe-9417-b5273abf88d5/image.png)
    1-2. method에 따라 진행
       ![](https://velog.velcdn.com/images/grammi_boii/post/6e7ebf4e-8b5b-4d60-a7bf-3fee52fcc572/image.png)<br>
        재밌는 점은 2022년에 작성된 망나니 개발자 블로그에는 PATCH 메서드가 늦게 나와서 발생한 예외처리에 대한 내용이 나와있는데, 지금은 업그레이드 된 것 같다. HTTP_SERVLET_METHODS 안에 PATCH 메서드도 들어가 있다.<br> 
    **1-3. 내부에 있는 processRequest 메서드**<br>
        드디어 DispatcherServlet이 등장했다. processRequest 메서드에서 do service 메서드를 호출하고 있는데, 이는 추상 메서드로 DispatcherServlet에 구현되어 있다.
        ![](https://velog.velcdn.com/images/grammi_boii/post/27f0dd6f-c6eb-4aa1-9cf8-64682517d8b6/image.png)
        ![](https://velog.velcdn.com/images/grammi_boii/post/1bfbbdce-8ef7-4aa5-9898-289518825eda/image.png)
        doService 메서드 내부에서는 성공 로직인 doDispatch가 핵심이므로 바로 넘어가자

2. 위임할 컨트롤러를 찾음
3. 요청을 컨트롤러로 위임할 핸들러 어댑터를 찾아 전달<br>
    2, 3 -1. doDispatch 메서드 내부에서 핸들러 어댑터를 찾음 <br> ![](https://velog.velcdn.com/images/grammi_boii/post/9160e267-36b9-4abb-ad7d-857e43b35cfd/image.png)
   (이 부분 내부는 생략 하겠다..)

4. 핸들러 어댑터가 컨트롤러로 요청을 위임<br>
   4-1. 핸들러 어댑터를 통해 컨트롤러 메서드를 호출
   ![](https://velog.velcdn.com/images/grammi_boii/post/3d422e84-913a-41d3-a5d9-25dafacb7789/image.png)
   4-2. 구현체인 AbstractHandlerMethodAdapter의 handle 메서드 내부를 보면<br>
   ![](https://velog.velcdn.com/images/grammi_boii/post/7149f108-bcdc-4877-9b28-f3cbc217178c/image.png)
   반환 타입이 ModelAndView인데 MVC의 냄새가 나는게 느껴지는가?<br>

5. 비즈니스 로직 처리
6. 컨트롤러가 반환값을 반환
7. 핸들러 어댑터가 반환값 처리
8. 서버 응답을 클라이언트에게 반환

4번 이후 과정들은 따라가보지 못했는데, 아마 역순이라 비슷하지 않을까 싶다.<br>
어렵네요 하하 잘못된 내용 있으면 지적해주세요!!

이렇게 흐름을 따라가 보니 디스패처 서블릿이 뭔지 헷갈리지는 않을 것 같다.<br>
GPT에게 스프링 컨테이너에서 관리하는거냐.. 빈이냐 물어봤던 지난날은 안녕~<br>




너무 너무 좋았던 참고자료 (망나니 개발자 최고! 완전 추천)<br>
<https://velog.io/@ddangle/Spring-Servlet-%EA%B3%BC-Servlet-Container><br>
<https://mangkyu.tistory.com/216>

추가로 저번 코드리뷰때 exception filter를 추가적으로 구현해야 하는 이유를 생각해보라는 댓글을 보고 답변이 바로 생각이 안나서.. 반성의 의미로 다시 찾아봤다. 벨로그 땡글이님이 정리를 참 잘해놓으셨다.<br>
<https://velog.io/@ddangle/Spring-Filter-vs-Interceptor>





### 배포, 에러났던 부분

```yaml
services:

  cache:
    container_name: redis
    image: redis:alpine
    ports:
      - 6379:6379
    volumes:
      - redis_data:/data
    networks:
      - knowledge_network

  application:
    container_name: knowledge_be
    image: knowledge_be
    ports:
      - 8080:8080
    depends_on:
      - cache
    env_file:
      - .env
    networks:
      - knowledge_network

volumes:
    redis_data:

networks:
  knowledge_network:
    driver: bridge
```


```shell
'ACCESS_TOKEN_EXPIRE_TIME': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: "1_800_000"
```
yml과 다르게 .env에서는 100_000 이렇게 구분이 안된다고한다<br>
ACCESS_TOKEN_EXPIRE_TINE=100000 <br>
이렇게 수정

이외에 별다른 에러는 없었다

### 결과

Redis도 elastic cache를 사용하면 좋지만 이번 과제는 docker container로 대체했다


ec2
![](https://velog.velcdn.com/images/grammi_boii/post/e99cc4ec-689f-47e0-a464-493f1c932346/image.png)

rds
![](https://velog.velcdn.com/images/grammi_boii/post/be27a634-fe46-48e4-96b1-2a5876af4b92/image.png)

ec2 docker ps
![](https://velog.velcdn.com/images/grammi_boii/post/f4b154df-3721-47d8-a8d8-4fb04c4bde71/image.png)

ec2 docker be container 내부 로그
![](https://velog.velcdn.com/images/grammi_boii/post/41c9482e-5a44-4e20-b3db-a12e09e575a3/image.png)

회원가입
![](https://velog.velcdn.com/images/grammi_boii/post/148031cf-a5d0-4a64-ae52-eabbf30786cc/image.png)

로그인
![](https://velog.velcdn.com/images/grammi_boii/post/0c918015-0ee6-4713-9c1f-5f29c68c33aa/image.png)

글 작성
![](https://velog.velcdn.com/images/grammi_boii/post/36b59591-c892-4ca6-8aa6-df3679d1c42d/image.png)

글 찾기
![](https://velog.velcdn.com/images/grammi_boii/post/d32921fb-3d35-4147-9c39-c8d8b5379cd6/image.png)

docker hub
![](https://velog.velcdn.com/images/grammi_boii/post/a2340e0c-8dae-41cb-9c1e-c518fe024c0e/image.png)


### 다이어그램

![](https://velog.velcdn.com/images/grammi_boii/post/2177442b-781d-4484-9d6a-de7949dc0858/image.png)

간단한 프로젝트여서 이렇게 했지만 좋은 구조는 아니다<br>

다른 프로젝트 아키텍쳐는 이렇게 설계했다
![](https://velog.velcdn.com/images/grammi_boii/post/52e3b254-17da-4fe3-af0f-80ec622da731/image.png)

개선점은 ec2도 프라이빗 서브넷에 있는게 좋고 그러려면 nat gateway가 필요하다.<br>
![](https://velog.velcdn.com/images/grammi_boii/post/8942aa69-26c6-4d5d-8df0-875eb0c960cc/image.png)
<br>
이런느낌

이번에 새로 해봐서 완전히 이해하지 못했지만<br>
가용영역 두개에 걸쳐 있는 이유는 크게 두가지 이유인데
1. 장애 발생시 가용영역을 바꿔서 서비스를 계속 제공하기 위해
2. 로드밸런싱을 위해(부하 분산)

추가 설명으로<br>
rds, elastic cache 부분에 인스턴스 여러개 떠있는게<br>
많이 봤을 다중AZ 옵션이다

### BootJar

bootjar했을때
![](https://velog.velcdn.com/images/grammi_boii/post/d427dccd-1777-4d34-826c-7e9d4d98d93f/image.png)

bootjar 태스크는 실행가능한 jar 파일을 만든다.<br>

jar했을때
![](https://velog.velcdn.com/images/grammi_boii/post/aea0d584-16c4-4564-a82e-495d53dcb8ea/image.png)<br>
jar 태스크는 클래스 파일을 만든다.

build했을때
![](https://velog.velcdn.com/images/grammi_boii/post/131e428b-5bb5-410b-bb27-3261a8887d03/image.png)<br>
Build할때 plain jar, jar파일 2개가 생성되는데<br>
각각 위에서 본 jar, bootjar 태스크를 실행했을때의 파일이다.<br>

배포시에는 보통 실행 가능한 파일이 필요하기 때문에 bootjar 했을때 (plain 아닌 jar) 파일을 많이 사용한다.

뭔지 대충 알았으니까 build랑 bootjar가 뭐가 다른지 보자

```shell
오후 10:25:44: Executing 'bootJar'...

> Task :compileJava UP-TO-DATE
> Task :processResources UP-TO-DATE
> Task :classes UP-TO-DATE
> Task :resolveMainClassName UP-TO-DATE
> Task :bootJar UP-TO-DATE

BUILD SUCCESSFUL in 518ms
4 actionable tasks: 4 up-to-date
오후 10:25:44: Execution finished 'bootJar'.

```

![](https://velog.velcdn.com/images/grammi_boii/post/c8c83181-c514-4e80-9edd-4c471712882a/image.png)
각 단계는 다음과 같은 태스크라고 한다.<br>
컴파일은 말그대로 바이트코드로 컴파일한거고<br>
processResources는 리소스 파일을 복사하는 작업이다.<br>
application.yml같은 파일 말하는 것 같다. -> 이부분 저번 세션 시간에 질문했던 부분!!! 발견했다 뒤에서 정리해보겠다<br>
classes는 앞선 두 단계 결과를 모아두는 단계<br>



```shell
오후 10:24:48: Executing 'build'...

> Task :compileJava UP-TO-DATE
> Task :processResources UP-TO-DATE
> Task :classes UP-TO-DATE
> Task :resolveMainClassName UP-TO-DATE
> Task :bootJar UP-TO-DATE
> Task :jar UP-TO-DATE
> Task :assemble UP-TO-DATE
> Task :compileTestJava NO-SOURCE
> Task :processTestResources
> Task :testClasses
> Task :test NO-SOURCE
> Task :check UP-TO-DATE
> Task :build UP-TO-DATE

BUILD SUCCESSFUL in 490ms
6 actionable tasks: 1 executed, 5 up-to-date
오후 10:24:48: Execution finished 'build'.
```

build 태스크에서 bootjar, jar 태스크 포함하는거 말고 다른점은<br>
- assemble<br>
    jar/bootjar 같은 패키징 태스크를 묶어두는 태스크라고 하는데 뭔말인지 잘 모르겠다  
- compileTestJava<br>
    테스트 코드를 컴파일하는 태스크
- processTestResources<br>
    테스트 리소스 파일을 복사하는 태스크 (src/test/resources -> build/resources/test)
- testClasses<br>
    테스트 실행 준비를 마무리하는 태스크
- test<br>
    테스트를 실행
- check<br>
    code quality를 검사하는 태스크라고 하는데, jacocoTestReport -> 친숙한 이름이 보였다.<br>
    전 프로젝트에서 CI과정에 커버리지 테스트와 코드 품질검사를 포함시켜서 sonar cloud를 사용했었는데<br>
    이때 jacoco가 사용되었다.. 이것도 다음에 좀더 파보자
- build<br>
    assemble, check 태스크를 포함한다. 최종적으로 build를 수행(바이트코드 생성).



실제로 압축을 풀어보면<br>
![](https://velog.velcdn.com/images/grammi_boii/post/ece62cdb-5a90-4854-8312-16b967d55df1/image.png)<br>
![](https://velog.velcdn.com/images/grammi_boii/post/84fad648-290e-4ec3-9ae5-e5114cf7949c/image.png)


참고자료<br>
<https://www.devkuma.com/docs/gradle/bootjar-jar/>


### bootjar시 환경변수 주입

![](https://velog.velcdn.com/images/grammi_boii/post/3ddf43ea-992a-49bb-b31a-76cf764ae564/image.png)

![](https://velog.velcdn.com/images/grammi_boii/post/85225f27-c2fb-4ddf-ab17-11510dc18af4/image.png)

include 부분을 주석처리하고 jar파일을 만들어도<br>
우려한대로 비밀값들이 다 들어가있다.<br>

단, 저 환경변수들을 ${} 부분에 주입(bake) 하지는 않는다고 한다.<br>
그렇기 때문에 docker compose에서 .env 파일을 사용해 주입하는게 우선순위를 가지는 것 같다.<br>

결론은 docker image를 퍼블릭으로 올릴거면 application-prod 같은 파일에 환경변수를 넣지 말고<br>
.env 파일을 사용하는게 좋은 것 같다.<br>

이번 지식인에서 사용한 env 파일 예시

```shell
POSTGRESQL_USERNAME=postgres
POSTGRESQL_PASSWORD=XXXXXXXXXX
POSTGRESQL_HOST=ceos-knowledge-db.XXXXXXXXX.ap-northeast-2.rds.amazonaws.com
POSTGRESQL_PORT=5432
POSTGRESQL_DATABASE=knowledge

BASE_URL=ec2-X-X-X-X-X.ap-northeast-2.compute.amazonaws.com
FRONT_URL=http://localhost:3000

REDIS_HOST=redis
REDIS_PORT=6379

DIR=/Users/leo/knowledgein_images/

# access - 30분
ACCESS_EXPIRATION=1800000
# refresh - 30일
REFRESH_EXPIRATION=2592000000
JWT_SECRET="eawfiwsfefafsdljlewfw4ennnsd412asdffalskfwnld123aiiiewnsfdwenwekewnlewnklenwlkewlewnlwewenl3101ln4223jndsfnfl1sdfwfeljk134"

GOOGLE_CLIENT="클라이언트 id"
GOOGLE_SECRET="클라이언트 시크릿"
```

**추가적으로!!**<br>
profiles.active를 사용하지 않았는데 이걸 사용해야 빌드시에도 다른 프로필 환경변수는 포함되지 않도록 작동한다고 한다.<br>
코드가 좀 마음에 안들어서 include로 맨날 했는데 다음번에 적용해봐야겠다


### Nginx



### Load Balancing

### AWS ELB - ALB, NLB, CLB, GWLB

### Nginx + ELB ???

