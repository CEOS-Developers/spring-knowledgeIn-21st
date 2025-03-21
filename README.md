# spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project

___
### 네이버 지식인 서비스에 대한 이해

#### - 회원 :
네이버 지식인은 네이버를 통해 로그인을 하므로 네이버에서 필요로 하는 유저 정보를 찾아봤다.
네이버 회원가입할 때 필요한 정보는 이러하다
![](https://velog.velcdn.com/images/mirupio/post/c33480bc-a7bf-4b62-b18d-65fff180af19/image.png)

프로필 수정 페이지에서 수정 가능한 정보는 이러하다
![](https://velog.velcdn.com/images/mirupio/post/dbfb7e32-5987-4105-bacf-ad239412860b/image.png)

따라서 필요한 정보는
- 아이디
- 비밀번호
- 이메일 주소(선택)
- 이름
- 생년월일
- 성별(선택)
- 전화번호

로 정리해봤다

#### - 게시글 :
![](https://velog.velcdn.com/images/mirupio/post/6d00f94f-06fe-402e-b263-f98ebb2e01f7/image.png)
- 제목
- 게시글 내용
- 작성 날짜
- 이미지

그리고 게시글에는 해시태그와 댓글을 추가할 수 있다.


#### - 해시태그

#### - 댓글 :
![](https://velog.velcdn.com/images/mirupio/post/0212103c-5359-4fa2-bbc9-5add0bdea2fb/image.png)
- 댓글 내용
- 작성 날짜
- 좋아요, 싫어요
- 작성자 정보
- 대댓글


___

### ERD
![](https://velog.velcdn.com/images/mirupio/post/52d7a59f-4b2f-410a-8904-e32a09fb1cda/image.png)

### 고민했던 부분
#### **'좋아요'**에 관해..
- '좋아요' 테이블 따로 설계?
  누가 좋아요를 눌렀는지에 대한 좋아요 목록보다는 숫자가 중요하다고 생각
  => 댓글의 일부로 만들고, 타입은 int로 해서 숫자 증가/감소하도록 만들기
- '좋아요' 구현 방법?
  둘 중에 고민
    - 좋아요,안좋아요 -> boolean으로 칼럼 1개 파기
    - 좋아요,싫어요 -> 칼럼 2개 파기
      => 근데 위에서 댓글의 일부로 만들고, 숫자로 저장하는 걸로 정해서 칼럼 2개 파는 걸로 결정

#### **'이미지'**에 관해..
- '게시글 이미지' 테이블을 따로 설계해야할지 고민
  => 매번 이미지 테이블까지 가서 조회하면 오래걸릴거 같아서 '게시글'의 일부로 포함시키기로 했다.

#### **'댓글'**에 관해..
- '대댓글' 구현 방법?
  사실 어떻게 해야할지 감이 잘 안와서...
  일단은 대댓글의 경우, 원댓글이 무엇인지 parent_comment에 저장하고, 그냥 일반 댓글(어떤 댓글에도 속해있지 않은 댓글)의 경우 parent_comment에는 Null을 저장하는 방식으로 하려고 한다.


최종적으로..
### 모델링에 대한 설명

#### 1. User (유저) 테이블 : 사용자 정보 저장
(user는 SQL에서 예약어이기 때문에, 이를 테이블 이름으로 사용할 수 없으므로 테이블 이름은 member로 설정함)

    user_id: 기본 키(Primary Key)로 각 유저를 식별하는 값
    ID: 사용자 아이디
    password: 비밀번호
    email: 이메일 (NULL 허용)
    name: 사용자 이름
    birthdate: 생년월일
    gender: 성별 (NULL 허용)
    phone_number: 전화번호
    profile_image_url: 프로필 이미지 URL

#### 2. Post (게시글) 테이블 : 사용자의 게시글 정보 저장

    post_id: 기본 키
    title: 게시글 제목
    post_content: 게시글 내용
    created_at: 작성 날짜 (기본값: 현재 시간)
    user_id: 작성한 유저의 ID (User 테이블의 user_id와 외래 키 관계)
    Field: 게시글 이미지 URL (NULL 허용)

#### 3. Comment (댓글) 테이블 : 게시글에 달린 댓글 저장

    comment_id: 기본 키
    comment_content: 댓글 내용
    created_at: 작성 날짜
    parent_comment: 원 댓글 ID (대댓글을 위한 컬럼, NULL 허용)
    like_count: 좋아요 수 (기본값 0)
    dislike_count: 싫어요 수 (기본값 0)
    post_id: 해당 댓글이 달린 게시글 ID (Post 테이블과 외래 키 관계)
    user_id: 댓글 작성자 ID (User 테이블과 외래 키 관계)

#### 4. Hashtag (해시태그) 테이블 : 게시글에 사용된 해시태그 정보 저장

    hashtag_id: 기본 키
    hashtag_name: 해시태그 이름

#### 5. Post-Hashtag (게시글-해시태그) 테이블 : 게시글과 해시태그 간 다대다(N:M) 관계를 관리

    post-hashtag_id: 기본 키
    post_id: 게시글 ID (Post 테이블과 외래 키 관계)
    hashtag_id: 해시태그 ID (Hashtag 테이블과 외래 키 관계)


#### 테이블 간 관계

    User - Post: 1:N (한 명의 유저가 여러 개의 게시글을 작성 가능)
    User - Comment: 1:N (한 명의 유저가 여러 개의 댓글을 작성 가능)
    Post - Comment: 1:N (한 개의 게시글에 여러 개의 댓글이 가능)
    Post - Hashtag: N:M (게시글 하나에 여러 개의 해시태그가 있을 수 있고, 하나의 해시태그가 여러 게시글에 사용될 수 있음)




