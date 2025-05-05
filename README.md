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


