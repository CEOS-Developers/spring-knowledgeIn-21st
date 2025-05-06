# (1) spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project


# (2) Spring Security와 로그인 - 4주차
---

### 1) SpringSecurity
- 정의 : Spring 기반 application의 인증(Authentication)과 인가(Authorization)을 담당하는 보안 프레임워크
- csrf, cors, https 보안 설정, OAuth2, JWT, session 등 다양한 인증 전략 연동 가능
- 동작 방식
1) 클라이언트가 요청을 보낼 때 SecurityFilterChain을 통해 인증 및 권한을 검사
2) 인증이 완료되면 SecurityContextHolder에 인증 객체(Authentication)를 저장해 이후 요청에서 재사용
3) 세션 기반 인증 혹은 JWT 기반 인증 방식에 따라 인증 객체를 관리함
- 연동
기본은 세션 기반 인증, AuthenticationFilter을 만들어 JWT 필터를 등록하면 JWT 기반 인증 가능

---

### 2) JWT와 로그인
---
- JWT와 Session
공통점 : 사용자 인증 상태를 유지하는 방식
차이점 : 작동 방식, 저장 위치, 확장성, 보안 처리 방식 등에서 차이가 있음

- 공통점
목적 : 인증(Authorization)된 사용자의 상태를 유지하기 위함
흐름 : 로그인 → 인증 완료 → 토큰 또는 세션 생성 → 이후 요청에 인증 정보 포함
사용처 : 웹 서비스에서 로그인 상태 유지, 사용자 인증 필요 API 호출 등에서 사용됨

- 차이점
1) JWT
- 저장 위치 : 클라이언트(브라우저의 localStorage, sessionStorage, cookie 등)
- 토큰 형태 : Base64 인코딩된 JSON 문자열
- 서버 상태 : stateless (서버가 토큰 내용을 저장하지 않음)
- 확장성 : 서버 간 공유 필요 없음 (토큰 자체에 정보가 포함) → 수평 확장에 유리
- 보안 : 탈취되면 유저 행세 가능 → 짧은 만료 시간 + HTTPS + Refresh Token 필요
- 만료 관리 : 토큰에 직접 만료 시간 지정
- 로그아웃 처리 : 토큰은 클라이언트에서 삭제만 가능

2) Session
- 저장 위치 : 서버 메모리, DB, Redis
- 토큰 형태 : 고유한 세션 ID
- 서버 상태 : stateful (서버가 세션 상태를 저장)
- 확장성 : 서버 또는 중앙 세션 저장소 필요 → 확장성에 불리
- 보안 : 서버 관리 하에 세션 무효화 가능 (조작은 어려움)
- 만료 관리 : 서버 측에서 세션 유효시간 관리
- 로그아웃 처리 : 서버에서 무효화하려면 별도 블랙리스트 필요

3) 적합성
- Session : 보안성과 무효화 기능이 중요한 서비스 구현 시 사용
- JWT : 서버 부담을 줄이고 클라이언트에 인증 상태를 넘기고 싶은 경우 사용

---