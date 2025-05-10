# (1) spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project

<br>

# (2) Spring Security와 로그인 - 5주차
---

### 1) 저번 과제에서의 오류
1. spring security 의존성을 추가해서 테스트하다가 지운 버젼으로 커밋을 올렸습니다 (결론 : security 추가는 했었지만 제대로 못 쓴 모양..)

2. spring security 의존성이 추가된 상태에서 다른 라이브러리가 import가 안 되어.. 토큰 테스트도 못함
<br>

### 2) 문제 해결 과정
1. spring security 의존성 제대로 추가

2. vscode의 개발 환경에 확장팩 설치 (lombok, Java Extension Pack, Spring Boot Extension Pack 등 - 설치해야 JWT, security 등 라이브러리가 인식됨)

3. user api 작성하고 회원가입, 로그인 토큰 테스트
 <br>
 
### 3) 설명
1. 의존성은 추가했습니다.

2. 제가 처음에 개발환경을 인텔리제이로 하지 않고 vscode로 했는데.. 이미 프로젝트가 많이 진행됐다고 생각하여 따로 바꾸지 않았습니다.

     (결론은 바꾸는게 훨씬 좋았음을.. vscode는 spring 관련된 확장팩 n개를 설치하지 않으면 import도 제대로 안 되었습니다.) -> 앞으로는 스프링부트를 할 때 모두가 이용하는 인텔리제이를 사용하기로!
3. 그렇게 토큰 테스트를 할 수 있었습니다.

4. 사진 첨부
![image](https://github.com/user-attachments/assets/9e480d18-b0f5-4919-8632-4499a4b04cb0)
![image](https://github.com/user-attachments/assets/e4ddacbf-2f80-4a8d-affe-6c64f532e79b)
<br>




---

# (3) Docker - 5주차

- 이미지 : 실행 가능한 컨테이너를 만들기 위한 거푸집
- 컨테이너 : 도커가 실행하는 격리된 환경과 그 내부

### 1. ![image](https://github.com/user-attachments/assets/c0cb1579-1a57-4cb0-8627-b93eee6403c4)

드래그 한 부분은 컨테이너에서 출력한 메세지가 아니라 docker engine이 출력한 메세지.
현재 로컬에서 갖고 있는 이미지 중 hello-world라는 이미지를 찾을 수 없으니까, docker herb에서 pulling을 해서 이미지를 가져오겠다고 함.
따라서 그 아래에 있는 내용들은 컨테이너 내에서 출력된 메세지임.


### 2. ![image](https://github.com/user-attachments/assets/616be0b8-8c83-4717-a87d-ad6f8ceb307a)
다시 docker run hello-world를 해보면,
이제 위의 드래그한 메세지는 나오지 않는다.
왜? 이미 다운로드 해 와서 이미지가 로컬 내에 있으므로 그걸 출력하겠다는 것.



### 3. 실험
1. docker ps -a 를 통해 실행중인, 또는 실행중이지 않아도 남아있는 컨테이너를 살펴보기 (cf. docker ps 하면 실행중인 컨테이너만 뜸)

2. 필요없는 컨테이너 및 이미지 삭제 - 컨테이너 삭제 시 docker rm, 이미지 삭제 시 docker rmi 명령어를 앞에 붙일 것. 삭제는 명령어 뒤에 컨테이너의 아이디나 이름을 붙이면 할 수 있다.
주의사항 : 특정 이미지를 사용하고 있는 컨테이너가 있는데 이미지를 삭제하려고 하면 에러가 뜬다. 그럴 때에는 컨테이너를 먼저 삭제하고 이미지를 삭제할 것.

3. apache 서버의 httpd 이미지를 가져와서 실행시켜 보기 - docker run httpd
라는 명령어를 통해 실행 -> ip주소를 가져올 수 있음(나 같은 경우 using 172.17.0.3. 으로 떠서 크롬에다가 실행시킴)

4. 본래는? It works라는 페이지가 떠야하지만.. 나는 뜨지 않음
(이유는 모르겠다 - 아마 포트가 제대로 매핑이 안되었나 싶다. 또는 httpd 컨테이너가 실행이 제대로 안 됐거나 - 그럼? httpd 컨테이너를 실행하고 싶으니 포트를 지정해주면 될 것 같다)

5. 포트를 지정하기 전에 포트가 지정되었는지 살펴보자
-> ![image](https://github.com/user-attachments/assets/0ab13361-0898-4274-9058-3eb32b312c5e)

6. 어라? 포트가 지정되어 있지 않다(강의에서와 다르게 .. 포트 지정을 따로 해야하는듯 하다.. <- 추측)

7. 포트 지정하기 전에 기존의 만들어져 있던 httpd 컨테이너를 삭제해보자
docker rm db87e732055a

8. 그럼 포트를 지정하여 새 컨테이너를 생성해보자
명령어는 대충
docker run -d -p 8080:80 httpd
-> 해석
-d >> 백그라운드 실행(detach mode)
-p 8080:80 >> 호스트의 포트 8080을 컨테이너의 80번 포트에 매핑

![image](https://github.com/user-attachments/assets/5e31015d-76a5-4ce6-a254-99e9ff2ab9ae)

9. 명령어 입력하고 docker ps로 컨테이너 상태를 확인해보자
브라우저에서 http://localhost:8080로 접속해보기!
![image](https://github.com/user-attachments/assets/de2a27c4-56a3-42ff-b27c-154c578fe44a)


된다!
---
