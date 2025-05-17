# spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project

---
## EC2+docker 배포 과정
### 순서
1. 이미지 빌드해서 도커 허브에 업로드
2. EC2 인스턴스 생성
3. EC2 우분투에 필요한 소프트웨어 설치
4. 콘솔에서 도커 로그인 후 도커허브의 이미지 다운로드
5. 해당 이미지로 컨테이너 실행하도록 docer-compose.yml 작성 후 docker compose up 실행

이미지 배포는 도커 허브로 푸시 한 뒤에 EC2에서 다운 받는 식으로만 할 수 있는 건 줄 알았는데, 미션 내용을 살펴보니 다음과 같은 다양한 방법으로 배포를 할 수 있는 것 같다.

- **ECR** : AWS에서 제공하는 도커 허브와 비슷한 개념으로 관리형 컨테이너 이미지 레지스트리 서비스. S3로 도커 이미지를 관리하므로 고가용성을 보장한다고 한다.
- **Elastic Beanstalk** : 애플리케이션 생성 후, 애플리케이션 버전을 업로드하면 EB가 자동으로 환경 실행(용량 프로비저닝, 로드 밸런싱, 조정, 모니터링 등)
- **App Runner** : 컨테이너화 된 어플리케이션을 쉽게 배포/운영할 수 있게 해주는 서비스. 사용자가 인프라를 직접 설정할 필요 없이, 코드나 컨테이너 이미지를 제공하면 자동으로 어플리케이션을 빌드하고 배포해준다고 함..

### 성공 화면
![화면 캡처 2025-05-17 173233](https://github.com/user-attachments/assets/a91248aa-603f-4c0d-b2c7-27ca41495c50)

ec2의 퍼블릭 주소로 들어가서 살펴본 스웨거

![화면 캡처 2025-05-17 173342](https://github.com/user-attachments/assets/90fe5f0d-18ae-44c1-9c3b-9b9bee13b6f4)

스웨거에서 회원가입 성공
![화면 캡처 2025-05-17 173618](https://github.com/user-attachments/assets/c1490aeb-6b47-45b4-8e95-43ef43d6ab21)

포스트맨에서 로그인 성공

따로 스크린샷을 찍지는 않았지만 질문, 답변 작성과 조회도 잘 되는 것 확인

### 트러블 슈팅
1. **또 컨테이너 이름 이슈** : 저번에 컨테이너 생성 시 자동으로 할당되는 기본 이름으로 spring-어쩌고(내 프로젝트명)로 db url을 저장해놨는데 이번에는 docker-compose.yml 파일의 위치가 내 플젝 디렉토리가 아니라 ubuntu 유저 디렉토리 바로 아래에 있어서 ubuntu-db-1로 설정됐다.. 그래서 또 오류가 났는데.. 처음에는 web 컨테이너가 아예 실행이 안되고 있길래 왜지?! 했지만 `sudo docker logs web` 명령어로 왜 오류가 난건지 금방 알 수 있었다.

게다가 나는 env 파일로 빼지도 않았다 지금 와서 spring의 yml을 새로 작성하기도 뭐해서

도커 컴포즈 파일의 위치는 매번 달라질 수 있으니 앞으로 컨테이너 이름을 꼭 지정해야겠다..

2. **포트를 잘 확인하자** : EC2의 보안 그룹 인바운드 규칙에서 분명 IPv4랑 IPv6 둘 다 모든 주소에 대해 개방해놨는데 왜 EC2 퍼블릭 주소에서 스웨거 접근이 안되지? 했는데 알고보니 HTTP,SSH가 사용하는 포트만 열어놔서 8080으로는 접속이 안됐다. 8080포트 열어주니까 접속이 잘 됐다. 바보같다

3. **DB 한글 인코딩** : 스에거에서는 잘 되는데, 포스트맨으로 하려니까 ![화면 캡처 2025-05-17 180735](https://github.com/user-attachments/assets/9880c78c-814e-4e81-9251-d41e69af5ebb)

이런 에러기 자주 떴다. requestBody에 영어만 담아 보내면 잘 보내지길래 조사해봤더니 데이터베이스의 인코딩 타입이 UTF-8로 제대로 설정이 안되어서 발생하는 에러라고 한다. 내 로컬 MySQL로는 잘 됐는데 아무래도 도커의 기본 mySQL 이미지로 빌드해서 그런거 아닐까 싶은..
해결 방법은 docker-compose.yml 에서 mySQL 이미지를 받아오는 부분에 아래의 옵션을 붙여주면 된다고 한다.
```
command:
  --character-set-server=utf8mb4
  --collation-server=utf8mb4_unicode_ci
  --skip-character-set-client-handshake
```

## 다이어그램 그려보기
![ceos](https://github.com/user-attachments/assets/473051de-03b6-48fe-ba8d-15bc7e036233)

이렇게 그리는게 맞는지는? 모르겠다..어려버

RDS 설정하긴 했는데 연결은 안했고..도커로 DB를 띄워서 사용중인데 일단 저렇게 그렸다 ㅋㅋ
전에 RDS 연결은 한번 해봐서..하면 금방할것 같긴 하다..
