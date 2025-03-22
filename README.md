# spring-knowledgeIn-21st
ceos back-end 21st naver knowledge-in clone coding project

---


## 1. 과제 설명
1. 아래의 요구사항에 맞추어 개발 초기 환경 구축하기
2. 요구사항에 포함된 엔티티 객체들의 repository 파일 만들기
3. 만든 repository 파일의 단위테스트 실행하기

### - 요구사항
> 구현 기능
1. 게시글 조회
2. 게시글에 사진과 함께 글, 해시태그 작성하기 
3. 게시글에 댓글 및 대댓글 기능
4. 게시글 댓글에 좋아요, 싫어요 기능
5. 게시글, 댓글, 좋아요 삭제 기능
6. 회원 기능

---

## 2. ERD 참고 자료 - dbdiagram.io 활용
![image](https://github.com/user-attachments/assets/2f5d2f03-5be9-491b-8bc9-9a8ff4520ee8)
((수정이 조금 필요합니다))

## 3. 설계 고민 및 결론 지점
(1) DB ERD를 설계하는 과정 : 처음에는 여러 테이블에 likes(좋아요 기능) 속성을 추가했는데 추가 테이블이 만들어질 것 같아서 그냥 likes 테이블을 하나 만들었습니다.

(2) domain 디렉토리 구조 짤 때 Service / Controller / repository 등의 폴더를 만들고 각 기능 파일을 추가했는데, 이번에는 기능 개수가 많을 것 같아서 기능별로 폴더를 만들었습니다. (예시 : comments, tags, posts,likes 등등) 어떤 방법을 택하는게 좋을까요?

(3) 스프링을 거의 처음 써봐서 그런지 편리하지만 많은 라이브러리를 써서 초기설정이 쉽지 않은 것 같습니다 (그래도 initializer가 있어서 정말 다행입니다..) (ㅜ.ㅜ..) initializer에서는 보통 어떤 기능들을 많이 쓰시나요?

---
## 4. 단위테스트 결과창 (신기해서 첨부)
![image](https://github.com/user-attachments/assets/8f963ee4-8e14-40c5-a00b-1b3e58539062)

---



