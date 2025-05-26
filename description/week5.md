시작하기에 앞서... 이번에는 리팩토링에 좀 집중을 해 보았구요. 예 말이 많습니다.. 예 그럽니다 ㅎ

## 🐳 Docker

---

### 📦 Docker란 무엇인가?

**컨테이너 기반 오픈소스 가상화 플랫폼**

Application 과 그 실행 환경(라이브러리, 설정 등)을 **하나의 이미지로 패키징**하여 어디서나 동일하게 실행할 수 있도록 도와준다.

❓ **컨테이너**

*Docker은 표준화된 유닛으로 패키징하며, 이 **컨테이너**에는 라이브러리, 시스템 도구, 코드, 런타임 등 소프트웨어를 실행하는 데 필요한 모든 것들이 포함되어 있습니다*

그래서 **컨테이너**가 뭔데?

→ 격리된 실행 환경이다.

- **전통적인 가상화 (Virtual Machine)**
  - 하드웨어 위에 전체 OS를 설치해 **가상의 컴퓨터**를 만들어 실행하는 방식
  - Hypervisor(가상화 관리자), 게스트 OS(각 VM에 설치된 Linux/Window), 애플리케이션 + 라이브러리 로 구성
  - OS 까지 포함되어 있어 무겁고 느림, 완전한 격리 환경 제공, 리소스 소모가 많다.
- **컨테이너 (Container)**
  - **OS를 공유**하면서 **프로세스 단위**로 애플리케이션을 격리하는 가상화 방식이다.
  - **Docker**은 이 컨테이너 기술을 쉽게 쓰게 해 주는 플랫폼
  - Host OS(Kernel 공유), 컨테이너마다 사용자 공간만 분리, 실행 시점에 파일 시스템/네트워크 격리
  - 부팅이 필요 없기에 가볍고 빠르며, OS 설치가 불필요해 리소스 절약 가능하다.

📌 **Docker의** **특징**

- **가볍다** : 전체 OS를 가상화 하는 것이 아닌, Kernel을 공유하고 프로세스 단위로 실행한다.
- **빠르다** : VM 보다 빠른 부팅과 실행 속도
- **이식성**이 좋다

### 🤔 왜 Docker를 사용하는가?


| 기존 방식 | Docker 방식 |
| --- | --- |
| 환경 차이로 인한 오류 발생 | 환경을 **이미지로 고정** |
| 배포 시 작업이 많다. | 자동화/스크립트화 가능 |
| 테스트 환경 구축이 복잡하다. | `docker run`  한 줄이면 끝이다 |
| VM은 무겁고 느리다. | Docker은 가볍고 빠르다. |



### 🧩 이미지, 컨테이너, 레지스트리, 볼륨 등 핵심 개념

📌 **이미지**

**실행 가능한 모든 것을 포함**하는 불변의 패키지이다.

예를 들어 JDK, Spring application, 설정 파일 등이 포함된다. 레이어 기반 저장 구조로 캐싱과 공유에 유리하다.

📌 **컨테이너**

**이미지를 실행한 프로세스 단위 인스턴스**이다.

application이 실제로 실행되는 환경을 말한다. 격리된 파일 시스템과 네트워크를 가진다.

📌 **레지스트리(Registry)**

Docker **이미지를 저장하고 공유**하는 공간이다.

📌 **볼륨(Volume)**

**컨테이너가 꺼져도 데이터가 유지**되도록 해주는 **저장소**이다. DB 데이터나 로그 저장 등에 주로 사용된다.

## 👾 Docker 기본 명령어

---

### 이미지 관련 명령어 정리

`docker images`

- 현재 로컬에 있는 이미지 목록 확인

`docker pull nginx:latest`

- 원격 레지스트리(Docker hub)에서 이미지 받아오기

`docker build -t my-app .`

- Dockerfile을 기반으로 이미지 생성
- t는 tag, . 은 현재 디렉토리

`docker rml my-app`

- 이미지 삭제

### 컨테이너 실행 및 관리 관련 명령어 정리

`docker run -d -p 8081:8081 —name ceos-app my-app`

- 이미지로 부터 컨테이너 생성 및 실행
- 백그라운드로 포트를 8081로 ceos-app이라는 컨테이너 이름으로 지정해서 생성하고 실행하겠다!

`docker stop ceos-app`

- 위에서 실행한 컨테이너 중지

`docker start ceos-app`

- 중지된 컨테이너 재실행

`docker rm ceos-app`

- remove 컨테이너

### 컨테이너 내부 확인 관련 명령어 정리

`docker ps`

- 현재 실행 중인 컨테이너 목록 보기

`docker ps -a`

- 종료된 컨테이너 포함해서 전체 목록 보기

`docker exec -it ceos-app bash`

- 실행중인 컨테이너 안에서 명령 실행

`docker logs ceos-app`

- 컨테이너 로그 보기

### 그 외 명령어?

`docker system prune -a`

- 사용하지 않는 이미지, 컨테이너, 볼륨 등을 한 번에 정리
- 단, 불필요한 리소스가 모두 삭제되므로 확인 후 명령할 것

### 💥트러블 슈팅

`Error starting userland proxy: listen tcp 0.0.0.0:8080: bind: address already in use`

- 해당 포트를 이미 다른 앱에서 사용하고 있음
  - 포트를 8081 등으로 바꿔서 실행하기 ← 이걸로 일단
  - 또는 해당 포트를 점유 중인 프로세스 확인 후 종료하기

`Unable to find image 'ceos-app:latest' locally`

- 로컬에 해당 이름 이미지가 존재하지 않음
- `docker build`를 안 했군요!
  - 먼저 `docker images` 가 있는지 확인한 후 `docker build -t ceos-app .` 또는 `docker pull myregistry.com/ceos-app:1.0`  로 직접 빌드 또는 pull

`Got permission denied while trying to connect to the Docker daemon socket`

- Docker 명령을 실행할 권한이 없다 (Linux의 경우 많이 생긴다고도 하네요?)
  - 관리자 권한으로 실행
  - Docker 그룹에 사용자 추가

## 📁 Dockerfile 작성법

---

```j
# 1. Base image (JDK 포함된 경량 이미지 선택)
FROM openjdk:17-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사
COPY target/app.jar app.jar

# 4. 애플리케이션 실행 명령
CMD ["java", "-jar", "app.jar"]

```

- 찾아보니까 멀티 스테이지 빌드가 있어서 이를 사용하면 빌드 도구는 제외하고 최종 JAR 만 포함된 가벼운 이미지를 만들 수 있답니다!
- (아직 뭔지 잘 모르겠어요)

## 🏃‍♂️ CI/CD와 Docker

---

간략하게만 정리하자면,

CI(Continuous Integration) → **지속적 통합 :** 코드 변경이 생기면 자동으로 빌드 & 테스트 수행

CD(Continuous Delivery / Deployment) → **지속적 배포 :** 빌드된 결과물을 자동으로 서버 배포 및 운영 환겨에서 자동 반영까지 수행

📌 **왜 CI/CD에 Docker을 반영할까?**

서버마다 환경이 다름 → 이미지에 환경을 포함해 어디서나 동일하게 사용

빌드 결과물 전송이 복잡함 → 이미지 자체가 결과물이기에 깔끔

테스트 환경을 만들기 어려움 → 컨테이너로 손쉽게 테스트 환경 구성

수동 배포 → docker run으로 빠르게 배포 가능

운영 서버 의존성 → 이미지에 의존성 포함

📌 **Docker 기반 CI/CD 구조 간략히만…**

개발자가 푸시 → CI 도구가 소스 코드를 받아서 테스트 실행 && Docker 이미지 빌드

→ 이미지를 Docker hub 에 업로드 → CD 단계에서 서버에서 pull 후 실행

따라서 Github Actions 나 Jenkins 를 많이 사용합니다!

그에 따라 서버 배포하는 과정이 CI/CD + Docker 조합으로 자동화가 가능하다는 말씀!

에 대해서는 다음 스터디를 열심히 들어보자 😄

---

## ❓다시 수정할 거를 고치고 리팩토링을 해볼까?

### ✅ 공통 사항

- 모든 요청/응답 은 application/json 형식
- 인증 필요 필드: 글/댓글/좋아요 작성, 삭제는 JWT 인증 필수

### 👤 User 기능

| 기능 구분 | Method | URI | 설명 |
| --- | --- | --- | --- |
| 회원 가입 | POST | /users | nickname, 이메일, 비밀번호로 회원 생성 |
| 로그인 | POST | /auth/login | 이메일 + 비밀번호로 로그인, JWT 발급 필요 |
| 로그아웃 | POST | /auth/logout | token 무효화 필요(Redis Blacklist) |
| 회원 탈퇴 | DELETE | /users/me | 내 계정 삭제 (soft delete 필요) |
| 내 정보 조회 | GET | /users/me | 현재 로그인한 사용자 정보 조회 |
| username 수정 | PATCH | /users/me | 내 nickname 변경 |
| email 중복 확인 | GET | /users/check-email?email= | 회원 가입 이전 이메일 사용 가능 여부 확인 필수 |

📌**PATCH vs PUT**

- **PATCH**
  - 리소스의 일부를 업데이트한다. 요청에 포함되어 있는 부분만 변경이 된다.
- **PUT**
  - 리소스의 전부를 업데이트 한다. 만약 보내지 않은 값이 있으면 null 로 업데이트가 된다.
  - 만약 요청한 URI에 자원이 존재하지 않을 경우, 서버는 오류 응답을 보내고 정보는 저장되지 않는다.

📌 **REST API URI 규칙**

- 소문자 사용
- 언더바 대신 하이픈 사용
- uri 마지막에는 / 포함 불가
- 계측 관계를 나타낼 시 / 구분자 사용, 대신 행위는 포함하지 않음(get 포함 불가)
- 되도록 명사를 사용할 것, uri에 작성되는 영어는 복수형으로 작성할 것

🔻**근데 URI는 뭐고 URL은 뭔가요?**

- **URI (Uniform Resource Idenrifier)**

  어떤 **리소스를 식별**하기 위한 “통합 식별자”

  https://example.com/posts/123

  → 어디에 이런 자원 이 있다 는 것을 **지칭**

- **URL (Uniform Resource Locator)**

  **리소스의 실제 위치(주소)**를 명시한 URI

  https://example.com/posts/123 이라는 URL이 있다면,

  http로 접근 가능한 웹 리소스이고, 도메인은 example.com, 경로는 /posts/123 이라는 **구체적 위치**를 알려준다.

  즉, **접근 방법 + 경로 를 포함하는 URI**이다.


### ✉️ Post 기능

| 기능 구분 | Method | URI | 기능 설명 |
| --- | --- | --- | --- |
| Post 생성 | POST | /posts | 질문 또는 답변 작성 |
| Post 조회 | GET | /posts/{id} | 질문 또는 답변 세부 조회 |
| Post 삭제 | DELETE | /posts/{id} | Post 삭제 (soft delete), 단 질문 글은 답변이 달렸을 경우 삭제 불가능 |
| Post 수정 | PATCH | /posts/{id} | 질문 또는 답변 수정 |
| 질문 목록 조회 | GET | /posts?type=QUESTION&&page=0&size=10 | 질문 리스트 조회 (paging) |
| 답변 목록 조회 | GET | /posts/{id}/answers | 특정 질문에 대한 답변 목록 조회 |

📌**Post 생성 시, 질문 글과 답변 글로 나눠서 uri를 만들어야 할까?**

우리는 테이블을 하나로 만들었기 때문에, 나눠서 관리를 하지 않는 편이 더 좋을 것 같다.

대신 PostType 필드로 확실히 구분하여 조회 시 분기 로직을 명확히 해야한다.

📌 **질문 목록 조회 uri에 페이징도 추가되어 있는데?**

GET 사용 시 모든 건에 대해서 전체를 불러오는 것은 비효율적이다.

DB 부하, 네트워크 트래픽, 렌더링 속도 모두에 영향을 심하게 미치기에 페이징은 필수이다.

`/posts?type=QUESTION&&page=0&size=10` 에서 QUESTION을 조회할 것이고, 0부터 시작하는 페이지 인덱스이며, 페이지 당 항목 수는 10개로 잡았다.

목록형 API에서는 페이징을 작성하는 것이 좋다.

⭐**근데 중간에 새로운 요청이 생긴다면 페이지는 어떻게 되는게 좋을까? 어떻게 처리할까?**

1. 사용자 A가 1~10번 게시글을 받음
2. 사용자 B가 새로운 게시글 작성
3. 사용자 A가 다음 페이지 요청

→사용자 A는 11~20번 게시글을 보고 싶었는데, 실제로는 10~19 게시글을 받게 됨(**중복/누락 발생**)

✅ 해결 방법

1. Cursor 기반 페이징

   `GET /posts?cursor=17000000000&size=10`

   → createdAt < 170000… 인 게시글 중 최근 10개 조회

   **장점 : 삽입/삭제가 발생해도 결과가 흔들리지 않음, 무한 스크롤, 모바일 환경에서 안정적**

   **단점 : 정렬 기준 필드가 명확해야 하고, 정렬 순서도 일관되어야 함**. 페이지 번호 기반 UI에는 적합하지 않음.

   GitHub API 에서도 /repos?since=354 같은 방법으로 하네요!

2. Offset 기반 페이징(기본)

   `page=0&size=10` 방식

   단순하지만 데이터 변경에 민감하다

   변경이 적은 목록이나 정적 게시판, 관리자 페이지에 적합하다. 단, 위의 예시 같은 문제가 발생할 수 있기에 중복 허용을 감안해서 사용하자.


### 📩 Comment 기능

| 기능 구분 | Method | URI | 기능 설명 |
| --- | --- | --- | --- |
| 댓글 작성 | POST | /posts/{postsId}/comments | 특정 글에 댓글 작성 |
| 댓글 삭제 | DELETE | /comments/{id} | 댓글 삭제 |
| 댓글 목록 조회 | GET | /posts/{postId}/comments | 글의 댓글 전체 조회 |

### 👍 Reaction 기능

| 기능 구분 | Method | URI | 기능 설명 |
| --- | --- | --- | --- |
| 좋아요/싫어요 등록 | POST | /posts/{postsId}/reactions | 답변(PostType = ANSWER)에 대해서 Reaction 등록 가능 |
| Reaction 삭제 | DELETE | /reactions/{id} | 좋아요/싫어요 취소 |

📌 **좋아요/싫어요 등록 시 postId 를 넣어야 할까 말까?**

- **POST /reactions**

  request body에 postId 필요

  간단하고 직관적이고, 클라이언트가 postId를 body에 담기 쉽낟.

  REST 리소스 구조 상 “Reaction은 POST에 종속” 이라는 의미가 약해질 수 있다.

- **POST /posts/{postsId}/reactions**

  Post 가 Reaction의 부모 리소스임을 명확히 표현 가능하다(RESTful)

  URI 만 봐도 이 post에 reaction을 달려는 것이군! 이라는 이해가 가능하다.

  request body가 간결해지나, uri 경로에 id 포함 관리가 필요

  → 우리는 Restful 한 서비스를 목표로 하기에 아래와 같은 구조로 설정하였다.


📌**좋아요 싫어요 등록 및 삭제를 같은 로직에 넣을 수 있을 것 같은데?**

흔히 지식인에서 좋아요/싫어요 등록 및 삭제를 사용자의 사용에 따라 분석해보면

1. 기존에 좋아요/싫어요가 없을 때 이를 누르면 등록된다.
2. 기존에 좋아요/싫어요가 있을 때 다른 리액션을 누르면 수정된다.
3. 기존에 좋아요/싫어요가 있을 때 같은 리액션을 누르면 삭제된다.

### #️⃣ Hashtag 기능

| 기능 구분 | Method | URI | 기능 설명 |
| --- | --- | --- | --- |
| Hashtag 별 질문 조회 | GET | /hashtags/{name}/posts | 특정 hashtag 가 붙은 질문 목록 조회 |
| Hashtag 목록 조회 | GET | /hashtags | 전체 hashtag 목록 (검색 또는 인기 태그) |

---

### Page 객체 vs Slice 객체

둘 다 Spring Data JPA에서 **페이징 처리**를 위해 사용되는 객체이다.

게시글 목록에서 조회할 때 페이지 단위로 잘라서 보여주기 위해서 사용할 예정이다.

| 항목 | `Page<T>` | `Slice<T>` |
| --- | --- | --- |
| 전체 개수 (`total count`) | 포함됨 (`getTotalElements()`) | 없음 |
| 전체 페이지 수 계산 | 가능 (`getTotalPages()`) | 불가능 |
| 다음 페이지 여부 | 계산 가능 | 계산 가능 |
| 무거움/느림 | 더 무거움 (count 쿼리 수행) | 더 가벼움 |
| 용도 | 페이징 UI (페이지 번호, 총 개수 등) | "무한 스크롤" 방식 등 |
| 사용 시점 | 전체 페이지 정보 필요할 때 | 다음 페이지 유무만 필요할 때 |

내 생각에는 위에서 가자 큰 차이점이 전체 페이지 수 계산, 쿼리 수행 속도 등 인거 같은데, 음.. 어떨 때 써야할까?

- 게시판, 페이징 UI, 전체 페이지 표시 → **Page**
- 모바일 앱 무한 스크롤 → **slice**
- 성능 민감, 전체 개수 필요 없음 → **Slice**
- 관리자 페이지 등 전체 정보 제공 → **Page**

📌 **그래 결정했어!**

게시판처럼 **전체 페이지를 표시하고 개수를** 알아야 할 때 `Page`를 써야겠다!

모바일 앱의 **무한 스크롤**이나 **다음 페이지 유무**만 중요할 때는 `Slice`를 써야겠다!

---

- CustomDetailsService 에서 loadUserByUsername 의 파라미터를 email로 잡아야 할지, userId로 잡아야 할 지 고민임.
  - **email** → 어쩔 수 없이 email로 해야겟다! email은 당연히 고유하고, 중복으로 로그인은 불가능하니까!
  - **username** → 메서드 이름과 동일, 그러나 unique가 아니여서 불가능! 현재는 식별 불가능하기에 로그인 충돌 발생
    - 나중에는 username을 unique하게 만들어서 메소드 이름과 동일하게 가는 게 좋을 것 같아!
  - **userid → 권장 x, 숫자 ID는 보통 인증 토큰 안에서만 사용 (로그인 x)**

- JWT 발급을 회원 가입 하자마자 할지 말지?
  - 회원 가입 후 로그인 해야지 할 수 있도록 처리 (왜냐면 자꾸 내가 까먹고 회원 가입 후 로그인을 하기 때문이지 ㅎㅎ)

- token provider 2번 호출 되는 거 고침
- 그리고 아니 request라 써야할지 DTO로 써야할지 requestDTO라 써야할지 Dto로 써야할지 아직 모르겠어서 그냥 requestDTO, responseDTO로 다 바꿔놓음. 뭐가 좋을까 흠ㅇㅅㅇ

P.s.추가… 준형님 코드를 사실 많이 보고 있는데, 파일 구조에서 post와 user로 나누어서 보기 쉽게 관리했더라고요! 저는 모든 것을 나눠서 comment, post, reaction,user 등등으로 나누고 있었는데… 그래서 저도 참고해서 수정했습니다!!! 잘 보고 갑니다 (여기까지 누가 볼려나ㅎ)