# 🔐 Spring Security와 로그인

## 🔥 JWT 인증 방식 알아보기

### 📌 로그인 인증 흐름

1. 클라이언트가 로그인 요청을 보낸다.
2. `UsernamePasswordAuthenticationFilter`가 요청을 가로채고, 사용자 입력값으로 `UsernamePasswordAuthenticationToken` 객체를 생성한다.  
   이때 생성된 토큰은 아직 인증되지 않은 상태이다.
3. `AuthenticationManager`가 해당 토큰을 전달받아 처리하며, 내부적으로는 `DaoAuthenticationProvider`가 이를 담당한다.
4. `DaoAuthenticationProvider`는 `UserDetailsService`를 호출하여 DB에서 사용자 정보를 조회하고, 이를 기반으로 `UserDetails` 객체를 생성한다.
5. 사용자가 입력한 비밀번호와 DB에 저장된 비밀번호를 비교하고 일치할 경우, 인증이 완료된 `UsernamePasswordAuthenticationToken` 객체를 생성한다.  
   이 객체는 `isAuthenticated = true` 상태지만, 클라이언트에게 직접 전달되지는 않는다.
6. 인증이 완료된 토큰은 다시 `AuthenticationManager`를 통해 `UsernamePasswordAuthenticationFilter`로 반환된다.
7. `successfulAuthentication()` 메서드가 실행되며, 다음과 같은 작업이 수행된다:
    - `SecurityContextHolder.setContext(...)`: 인증된 사용자 정보를 `SecurityContext`에 저장하여 이후 요청에서도 인증 상태를 유지한다.
    - `JwtUtil.createToken(...)`: 사용자 정보를 기반으로 JWT 토큰을 생성한다.
    - 생성된 JWT 토큰을 응답의 헤더 또는 바디에 담아 클라이언트에게 전달한다.  
      클라이언트는 이 토큰을 저장하고, 이후 요청 시 `Authorization: Bearer <token>` 형식으로 헤더에 포함시켜 서버에 전송한다.

### 📌 로그인 이후 요청 흐름

로그인 이후 클라이언트는 JWT 토큰을 포함해 서버에 요청을 보내고, 서버는 이를 통해 사용자의 인증 여부를 판단한다.

1. 요청이 들어오면 Spring Security의 필터 체인이 실행된다.
2. 로그인 시 사용되었던 `UsernamePasswordAuthenticationFilter`는 건너뛰고, `JwtAuthenticationFilter`가 실행된다.
3. `JwtAuthenticationFilter`는 다음과 같은 과정을 거친다:
    - **JWT 토큰 추출:**  
      `JwtUtil.resolveToken()`을 통해 HTTP 요청 헤더의 `Authorization: Bearer <token>`에서 토큰을 꺼낸다.
    - **토큰 유효성 검증:**  
      `JwtUtil.validateToken()`을 통해 서명 위조 여부, 만료 여부 등을 확인한다.
    - **사용자 정보 추출:**  
      `JwtUtil.getUserInfoFromToken()`을 통해 토큰의 클레임에서 `username`을 추출한다.
    - **사용자 조회:**  
      `UserDetailsService.loadUserByUsername(username)`을 호출하여 DB에서 사용자 정보를 조회하고, `UserDetails` 객체를 생성한다.
    - **인증 객체 생성:**  
      수동으로 `UsernamePasswordAuthenticationToken`을 생성하고 `isAuthenticated = true` 상태로 설정한다.
    - **SecurityContext 등록:**  
      인증 객체를 `SecurityContextHolder`에 설정하여 인증 상태를 유지한다.  
      JWT는 **stateless(무상태)** 구조이므로, `SecurityContextRepository.saveContext()`를 호출하지 않는다.  
      서버는 인증 상태를 기억하지 않기 때문에, 클라이언트는 **매 요청마다 JWT 토큰으로 인증**해야 한다.



## 🔁 Refresh Token 발급 로직

로그인 시 서버는 클라이언트에게 **Access Token**과 **Refresh Token**을 함께 발급한다.
- Access Token은 짧은 유효기간(15~30분)을 가지며,
- Refresh Token은 상대적으로 긴 유효기간(7일~30일)을 가진다.

서버는 **Refresh Token만 DB에 저장**하며, 클라이언트는 두 토큰을 로컬/세션 저장소 또는 쿠키에 저장한다.  
이후 요청 시 Access Token과 함께 Refresh Token도 전송하게 된다.

Access Token이 만료되었을 경우, 서버는 함께 전달된 Refresh Token을 사용해 다음 과정을 수행한다:
1. DB에 저장된 Refresh Token과 일치하는지 검증한다.
2. 일치한다면 새로운 Access Token을 재발급하여 응답한다.



## 🐾 로그아웃 처리 방식

로그아웃은 보통 **DB에서 Refresh Token을 삭제**함으로써 처리할 수 있다.  
그러나 다음과 같은 문제가 발생할 수 있다:

- Refresh Token은 삭제되었지만, **Access Token은 여전히 유효**할 수 있다.  
  → 이 경우, 사용자가 계속해서 보호된 리소스에 접근할 수 있게 된다.

이러한 보안상의 문제를 `블랙리스트(Blacklist)`를 활용하여 해결할 수 있다.

- 만료되지 않은 Access Token을 **블랙리스트에 등록**하여 무효화시킨다.
- 블랙리스트는 보통 Redis 등을 활용하여 저장되며, **토큰의 남은 유효시간만큼 저장**하고 이후 삭제한다.
- 서버는 매 요청마다 **토큰이 블랙리스트에 포함되어 있는지 확인**하고, 포함되어 있다면 인증을 거부한다.

이 방법은 보안을 강화할 수 있다는 장점이 있지만,
결국 서버가 상태를 저장하게 되므로 JWT의 stateless 구조와는 반대되는 개념이다. <br>
또한 블랙리스트는 토큰의 유효기간 동안만 임시로 저장하며, 이후 자동으로 제거되도록 설정할 수 있다.
그러나 대규모 사용자 요청이 몰릴 경우, 블랙리스트 조회 자체가 병목이 될 수 있다는 단점도 있다.

따라서 다음과 같은 전략을 택한다.
- Access Token의 만료 시간을 **짧게 설정(15~30분)** 하여 보안 위험을 줄인다.
- Refresh Token은 **긴 유효기간(7일 이상)** 으로 설정해 사용자 편의성을 보장한다.
- 블랙리스트는 **필요할 경우에만 선택적으로 도입**하는 것이 일반적이다.

