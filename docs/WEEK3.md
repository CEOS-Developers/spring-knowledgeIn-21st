# ✏️CRUD 기능 구현

## ☁️ERD
![ERD 수정본](src/main/resources/static/ERD_수정본.png)
- BaseEntity 적용
- Post, Answer 엔티티 구분

## 📜API 명세서
[🌱 네이버 지식인 API 명세서](https://furtive-nightshade-8f9.notion.site/1c4cfa529f488060ae20c932e8ba044e?v=1c4cfa529f4880648fe9000ce1110cea&pvs=4)

## 🧐고민했던 지점
### 1️⃣길어지는 URI
예시 상황 : 특정한 답변의 댓글을 삭제하는 경우 <br>
uri : `/posts/{postId}/answers/{answerId}/baseComments/{commentId}` <br>

왜냐하면 특정한 게시글에 대한 답변일 경우 `/posts/{postId}/answers/{answerId}` <br>
즉 이미 앞에 `/post/{postId}`가 붙음.

따라서 특정한 답변에 대한 댓글일 경우, 앞의 post가 누적되어서 uri가 길어짐

🤔<u>**만약 `/answers/{answerId}/baseComments/{commentId}`로 쓸 경우?**</u>

answer마다 고유한 id를 가지기 때문에 어떤 답변에 댓글이 달렸는지 판단하기에는 무리 없음. <br>
그러나 답변에 대한 uri의 통일성은 깨질 수 있음


### 2️⃣Query Parameter VS Path Variable
[[번역] Path Variable과 Query Parameter는 언제 사용해야 할까?](https://ryan-han.com/post/translated/pathvariable_queryparam/)

1. Query Parameter <br>
   ```/users?id=123  # 아이디가 123인 사용자를 가져온다.``` <br>
   파라미터의 이름과 값을 함께 전달하는 방식 <br>
   [토스 페이먼츠 개발자 센터 : 쿼리 파라미터](https://docs.tosspayments.com/resources/glossary/query-param)


2. Path Variable <br>
   ```/users/123  # 아이디가 123인 사용자를 가져온다.```
   경로를 변수로서 사용하는 방식


3. 각각 언제 사용해야 할까? <br>
    ```
    /users  # 사용자 목록을 가져온다.
    /users?occupation=programer  # 프로그래머인 사용자 목록을 가져온다.
    /users/123  # 아이디가 123인 사용자를 가져온다.
    ```
    - 어떤 resource를 **식별**하고 싶다 -> Path Variable
    - **정렬**이나 **필터링=검색**을 하고 싶다 -> Query Parameter


4. 프로젝트에서 적용되는 부분 <br>
    - 해시태그에 따라 게시글을 모아보는 기능 <br>
    - 즉 **필터링 기능**이다 <br>
    - Query Parameter <br>
    - `/posts?hashtag={hashtag}`


### 3️⃣이미지 저장 방식
[백엔드에서 이미지 업로드는 어떻게 하면 좋을까?](https://seungyong20.tistory.com/entry/%EB%B0%B1%EC%97%94%EB%93%9C%EC%97%90%EC%84%9C-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%ED%95%98%EB%A9%B4-%EC%A2%8B%EC%9D%84%EA%B9%8C)

1. blob을 사용하여 db에 저장
2. base64 형태로 db에 저장
3. 백엔드에서 파일을 저장하고 경로를 db에 저장
4. Cloud Storage에 이미지를 업로드하고 경로를 db에 저장
5. 클라이언트가 Cloud Storage에 이미지를 업로드 하고 경로를 db에 저장

현재 erd 설계에서는 이미지 엔티티에 imageUrl을 저장, 즉 경로를 저장하고 있음
검색 결과 S3를 가장 많이 사용하고 있음을 확인
- [Amazon S3란 무엇인가요?](https://docs.aws.amazon.com/ko_kr/AmazonS3/latest/userguide/Welcome.html)
- [[AWS] 📚 S3 개념 & 버킷 · 권한 설정 방법](https://inpa.tistory.com/entry/AWS-%F0%9F%93%9A-S3-%EB%B2%84%ED%82%B7-%EC%83%9D%EC%84%B1-%EC%82%AC%EC%9A%A9%EB%B2%95-%EC%8B%A4%EC%A0%84-%EA%B5%AC%EC%B6%95)

## 🤯구현의 어려움
### 1️⃣게시글 추가 : 이미지, 해시태그 업데이트
Request Body의 형식
```
{
    "title": "내일 눈 오나요?",
    "content": "지금 3월인데 눈이 오지는 않겠죠?",
    "userId": 1,
    "hashtags": [
        "#날씨",
        "#봄"
    ],
    "imageUrls": [
        "https://example.com/snowman.jpg"
    ]
}
```
- 요구 상황 : hashtags와 imageUrls는 Post 테이블의 필드가 아니며 Hashtag와 Imgae 테이블까지 저장해야 함 <br>
- 구현 방식 : `imageService`와 `postHashtagService`에 각각 저장 로직 추가 <br>
- 문제 상황 : 저장된 객체를 반환하는 Response Body에서 hashtags와 imageUrls에서 null을 반환 <br>
- 문제 해결 : **연관 관계 메서드** 사용. Post 객체에 `addImage()`와 `addHashtag()`를 추가 후
  `imageService.saveImageUrls()`와 `postHashtagService.saveHashtag()`에서 해당 메서드를 호출

### 2️⃣무한 참조
[JPA 양방향 Entity 무한재귀 문제해결](https://thxwelchs.github.io/JPA%20%EC%96%91%EB%B0%A9%ED%96%A5%20Entity%20%EB%AC%B4%ED%95%9C%20%EC%9E%AC%EA%B7%80%20%EB%AC%B8%EC%A0%9C%20%ED%95%B4%EA%B2%B0/)
1. Entity로 반환하지 않고, DTO를 적극 활용
2. Json으로 직렬화 할 속성에서 무시 해버리기 (@JsonIgnore)
3. 직렬화할 대상 객체의 toString override하여 재정의하기
4. @JsonManagedReference, @JsonBackReference 어노테이션으로, 직렬화 방향을 설정을 통해 해결
5. @JsonIdentityInfo을 통해 순환참조될 대상의 식별키로 구분해 더이상 순환참조되지 않게 하기

생성된 Post의 id를 반환하고 해결