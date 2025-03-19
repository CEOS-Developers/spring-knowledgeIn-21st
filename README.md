### 네이버 지식인

**구현 기능**
1. 게시글 조회
2. 게시글에 사진과 함께 글, 해시태그 작성하기 
3. 게시글에 댓글 및 대댓글 기능
4. 게시글 댓글에 좋아요, 싫어요 기능
5. 게시글, 댓글, 좋아요 삭제 기능

#### 네이버 지식인 구조
1. 질문 (Post)
<img src="https://github.com/user-attachments/assets/7c03d81a-2dc6-4525-a813-80c91426f3e0" width="50%">

2. 답변 (Answer)
<img src="https://github.com/user-attachments/assets/a648fd62-186e-49f9-a412-3beb63e4be2b" width="50%">

4. 좋아요/싫어요 (Like_dislike) + 댓글 (Comment)
<img src="https://github.com/user-attachments/assets/251a48a4-0e74-4030-bde4-0b8d74e4e200" width="50%">

## Mission 1️⃣ 데이터 모델링 
**ERD**![Image](https://github.com/user-attachments/assets/e1c66816-b435-4335-80f9-a36cbd603e03)**1. User**
- 한 명의 user은 여러개의 **Post, Aswer, like_dislike, comment**를 작성 가능 (User와 1:N 관계)

**2. Post**
- 하나의 Post에는 여러개의 **Comment, Answer, like_dislike, image** 작성 가능 (Post와 1:N 관계)
- Post와 **Hashtag**는 N:M 관계 -> 중간에 PostHash table 설정

**3. Answer**
- 하나의 Answer에는 여러개의 **comment, Image, like_dislike** 작성 가능 (Answer과 1:N 관계)

### Entity 설계 

**1. LikeDislike**
```
@Enumerated(EnumType.STRING)
   private LikeStatus likestatus;

@Enumerated(EnumType.STRING)
    private TargetStatus targetstatus;
```
- 이 부분에서, LikeStatus는 Enum으로 관리하여 Like, Dislike 설정
- 좋아요/싫어요는 Post와 Answer에 달 수 있으므로 TargetStatus에서 Post, Answer로 관리

**2. Comment**
```
  @Enumerated(EnumType.STRING)
    private TargetStatus targetStatus;
```
- Comment 또한 Post와 Answer에 각각 작성 가능하므로 TargetStatus를 이용하여 하나의 테이블에서 관리

## Mission 2️⃣ Repository 단위 테스트 (Post Entity 사용)

**1. User 생성**
```
@BeforeEach
    public void setUp() {
        // 테스트에 사용할 사용자 데이터 생성
        user = User.builder()
                .nickname("dohyun")
                .email("dohyun@naver.com")
                .password("1234")
                .build();

        userRepository.save(user);
    }
```
![Image](https://github.com/user-attachments/assets/8b2227f0-43b7-4b36-ae46-accc9386423d)

**2. 작성자를 기준으로 FindPost**
- 첫번째 Post 생성 
```
@Test
    public void testFindByWriter() {
        // given

        //첫번째 질문글 (사진 X)
        Post post1 = Post.builder()
                .title("Post 1")
                .content("hello")
                .writer(user)
                .build();
        postRepository.save(post1);

```
![Image](https://github.com/user-attachments/assets/dffeab57-e437-48eb-ace8-fc28a72cc7af)

- 두번째 Post 생성 
```
 Image image = Image.builder()
                .imageUrl("image.jpg") // 이미지 URL 설정
                .post(null)  // 아직 Post와 연결되지 않음
                .build();

        //2번째 질문글 (사진 1장)
        Post post2 = Post.builder()
                .title("Post 2")
                .content("one picture")
                .images(Collections.singletonList(image))
                .writer(user)
                .build();
        image.setPost(post2);
        postRepository.save(post2);
```
![Image](https://github.com/user-attachments/assets/a45d367d-4a33-4cc7-9504-4db1a22590cb)

- 세번째 Post 생성
```
 //3번쨰 질문글 (사진 2장)
        Post post3 = Post.builder()
                .title("Post 3")
                .content("two pictures")
                .images(Arrays.asList())
                .writer(user)
                .build();
        postRepository.save(post3);

        Image image1 = Image.builder()
                .imageUrl("image_url_1")
                .post(post3)
                .build();

        Image image2 = Image.builder()
                .imageUrl("image_url_2")
                .post(post3)
                .build();

        imageRepository.save(image1);
        imageRepository.save(image2);

```
![Image](https://github.com/user-attachments/assets/5e5e52df-bfa7-459e-a5b9-5ecf8d3dd19b)

- Post DB
![Image](https://github.com/user-attachments/assets/fe378097-02c1-4153-979f-ea16c396b5f2)
- Image DB![Image](https://github.com/user-attachments/assets/e5232e8e-39c2-48da-80d2-0445e0744e42)

- 나머지 when/then
```
// when
        List<Post> posts = postRepository.findByWriter(user);

// then
        assertThat(posts).hasSize(3);
        assertThat(posts).extracting(Post::getTitle).containsExactly("Post 1", "Post 2","Post 3");
```

