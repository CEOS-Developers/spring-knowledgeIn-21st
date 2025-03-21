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
        userPost.getPostImages().forEach(postImage -> {
            Optional<PostImage> findPostImage = postImageRepository.findById(postImage.getId());
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
        List<PostImage> postImageList = make2PostImages();
        Post newPost = makeTestPostWithImages(postImageList);
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
