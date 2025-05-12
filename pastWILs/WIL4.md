# spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project

---
## Spring Security 동작 원리
- 동작원리
    1. 요청 수신
    2. AuthenticationFilter: UsernamePasswordAuthenticationToken(=인증용 객체) 생성
    3. AuthenticationManager 호출, 이 클래스가 여러 Provider의 목록을 가지고 있다.
       그 중 해당 Token을 처리할 수 있는 적절한 Provider 선택
    4. 선택한 Provider에게 인증객체를 넘겨주고 해당 Provider는 UserDetailsService를 통해
       로그인한 사용자의 정보를 비교하여 인증한다.
    5. 인증이 완료되면 SecurityContextHolder에 인증 객체를 담는다.

위의 과정은 기본적인 formLogin 방식에서의 로그인 인증 절차이다.
JWT를 이용하려면 요청이 Controller로 들어왔을 때 로그인 로직을 작성한 Service단에서
직접 인증 객체를 만들고 authenticate()를 통해 인증 과정을 거친다.
이때 말하는 인증 과정이란 단순히 '사용자가 맞는지'를 검증하는 것으로, 처음 인증객체를 생성했을 때는 authenticated가 false값이지만
매니저의 authenticate()를 실행하고 나서 사용자의 유저네임과 비밀번호가 일치하면 authenticated가 true인 상태로 반환된다.

이 후에 해당 인증객체를 가지고 jwt토큰을 생성할 수 있다.

### UserDetails
해당 클래스는 Spring Security에서 유저의 정보를 담기 위해 사용하는 클래스이다.
스프링 시큐리티에 Entity 객체를 바로 넘겨주면 안되고, UserDetails형으로 변환해서 전달해야한다.
때문에 UserDetails를 상속하는 커스텀 UserDetails 클래스를 만들고, 해당 객체에 Entity를 담을 수 있도록
할 수 있다.
#### 유저 엔티티가 UserDetails클래스를 상속하게 하면 안될까?
UserDetails클래스와 일반 엔티티 클래스는 분리하는 것이 좋다고 한다. 유저 엔티티는 DB에 영향을 주는 클래스, UserDetails는 시큐리티단에 영향을 주는
클래스이기 때문에 분리를 해두는 것이 적절하다.
## Security Config 작성
- **csrf, formLogin, BasicHttp 보안 disable**
    - REST API로 JWT방식을 사용하기 때문에 위와같은 공격에 대한 보안은 필요없다고 함

## Controller단
- **@AuthenticationPrincipal**
    - 로그인한(인증된) 사용자의 정보를 가져올 수 있는 어노테이션.
    - 로그인이 필요하지 않은 요청에서 이 어노테이션을 쓴다면 null이 주입되면서 NullpointException이 일어날 수 있으므로 주의가 필요하다.
- **@ParameterObject로 Pageable 객체 받기**
    - 여러개의 쿼리 파라미터를 하나의 객체로 묶어서 받을 수 있게 해주는 어노테이션
    - Pageable 객체는 page 번호, 한 페이지 당 size(row 수), sort 방법을 캡슐화한 객체

