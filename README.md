# 🐋 Docker
## 🌊 왜 H2가 아닌 MySQL을 쓸까?

지식인 서비스를 클론 코딩하면서 H2 데이터베이스를 사용해 왔다. 
H2를 선택했던 건 Spring Boot 프로젝트를 처음 만들 때 쉽게 포함할 수 있고 설정도 간편해서 개발 초반에 사용하기 좋기 때문이다.

하지만 이번 Docker로 과제를 수행하면서 문제가 생겼다!

### H2 연결 오류
기존에 H2를 localhost로 연결해서 쓰고 있었다. 그런데 Docker로 Spring Boot 애플리케이션을 실행하자마자 터졌다. 
DB 연결 오류가 발생하면서 애플리케이션이 실행되지 않았다. <br>
Docker 컨테이너는 격리된 실행 환경이다. 우리가 흔히 쓰는 localhost는 개발자 로컬 컴퓨터를 의미하는데, 
**Docker 컨테이너 내부에서 localhost는 컨테이너 자기 자신**을 가리킨다. 
그런데 우리가 쓰던 H2는 개발자 컴퓨터, 즉 컨테이너 외부에 있었고 컨테이너 내부에서는 당연히 그것을 찾을 수 없었던 것이다.

컨테이너 입장에서 보면 `내부에 존재하지 않는 DB를 찾으려고 한 것`이고, 이것 때문에 오류가 발생한 것이다. 

이 문제를 해결하는 방법은 크게 두 가지가 있다.
1. H2를 **embedded 모드 (파일 기반)** 으로 전환하는 것이었다. 
즉, 애플리케이션 내부에서 H2 파일을 생성하도록 JDBC URL을 수정하고, 컨테이너 외부와 volume으로 연결하는 방식이다.
`oscarfonts/h2`와 같은 비공식 Docker 이미지를 통해 실행할 수는 있다. <br>
실제로 DockerHub에도 존재한다. [참고](https://hub.docker.com/r/oscarfonts/h2)

하지만 이건 H2 공식에서 제공하는 방식이 아니다. 

### 공식적으로 제공되는 DB

DockerHub를 보면 공식적으로 제공되는 데이터베이스는 다음과 같다.
- MySQL 
- PostgreSQL 
- MongoDB

### MySQL의 클라이언트-서버 구조
MySQL은 전통적인 RDBMS로 기본적으로 `서버-클라이언트` 구조를 따른다. 서버는 항상 열려 있고, 클라이언트는 여기에 접속해 쿼리를 날리는 구조다. 
다양한 애플리케이션, 백엔드, API, 관리 도구들이 MySQL과 연동될 수 있도록 설계되어 있으며, 여러 연결을 동시에 처리할 수 있는 멀티스레드 구조도 지원한다.

반면 H2는 애초에 테스트/개발을 목적으로 만들어진 `인메모리 DB`다. 물론 서버 모드로 전환해 사용하거나 TCP로도 연결할 수는 있지만, 이는 예외적인 사용법이고, 일반적으로는 JVM 내부에서 같이 실행되는 구조를 갖는다. 
즉, Docker의 분리된 컨테이너 환경과는 자연스럽지 못하다!

### 도커에서 MySQL을 쓰면 좋은 이유들
1. **격리와 일관성 (Isolation and consistency)**

도커의 철학과 잘 맞는다. 컨테이너 단위로 격리된 환경에서 독립적으로 실행되고, 환경 간 일관성을 보장한다.

2. **버전 관리 (Version control)**

mysql:8.0처럼 명확히 버전을 고정할 수 있어, 개발/운영/테스트 환경이 항상 동일하다.

3. **확장성과 리소스 제어 (Scalability and resource control)**

컨테이너에 메모리, CPU 제한을 걸 수 있고, 필요 시 복제나 클러스터도 쉽게 구성할 수 있다.

4. **의존성 관리 (Dependency management)**

로컬 컴퓨터에 MySQL을 직접 설치하지 않아도 된다. 컨테이너 안에서만 동작하니 내 개발 환경은 깨끗하게 유지된다.

5. **데이터 영속성 (Persistence)**

`/var/lib/mysql` 폴더에 Docker volume을 마운트하면 컨테이너가 꺼져도 데이터는 그대로 남는다.

### Reference
- 🌐https://docs.docker.com/guides/databases/
- 🌐https://www.geeksforgeeks.org/mysql-vs-h2/
- 🌐[https://www.oracle.com/kr/mysql/what-is-mysql/#:~:text=MySQL Database는 다양한 백엔드%2C 클라이언트 프로그램 및,지원하는 멀티스레드 SQL 서버로 구성된 클라이언트/서버 시스템입니다](https://www.oracle.com/kr/mysql/what-is-mysql/#:~:text=MySQL%20Database%EB%8A%94%20%EB%8B%A4%EC%96%91%ED%95%9C%20%EB%B0%B1%EC%97%94%EB%93%9C%2C%20%ED%81%B4%EB%9D%BC%EC%9D%B4%EC%96%B8%ED%8A%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8%20%EB%B0%8F,%EC%A7%80%EC%9B%90%ED%95%98%EB%8A%94%20%EB%A9%80%ED%8B%B0%EC%8A%A4%EB%A0%88%EB%93%9C%20SQL%20%EC%84%9C%EB%B2%84%EB%A1%9C%20%EA%B5%AC%EC%84%B1%EB%90%9C%20%ED%81%B4%EB%9D%BC%EC%9D%B4%EC%96%B8%ED%8A%B8/%EC%84%9C%EB%B2%84%20%EC%8B%9C%EC%8A%A4%ED%85%9C%EC%9E%85%EB%8B%88%EB%8B%A4). 
- 🌐https://www.datacamp.com/tutorial/set-up-and-configure-mysql-in-docker
- 🌐https://medium.com/@nuwanwe/mysql-on-docker-a-comprehensive-guide-e807fdcbcd48
- 🌐https://github.com/metabase/metabase/issues/8467
- 🌐https://www.metabase.com/docs/latest/installation-and-operation/running-metabase-on-docker