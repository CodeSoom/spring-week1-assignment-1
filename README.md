# ToDo REST API 만들기

## 과제 목표

Java를 이용해서 ToDo REST API를 만들어봅니다.

![API](https://user-images.githubusercontent.com/14071105/103476206-0456f280-4df7-11eb-89c4-d61845ef45ec.png)

- ToDo 목록 얻기 - `GET /tasks`
- ToDo 상세 조회하기 - `GET /tasks/{id}`
- ToDo 생성하기 - `POST /tasks`
- ToDo 제목 수정하기 - `PUT/PATCH /tasks/{id}`
- ToDo 삭제하기 - `DELETE /tasks/{id}`

## 요구 사항

- 모든 API 테스트를 통과해야 합니다.

## 실행하기

```bash
./gradlew run
```

## 테스트

### 설치하기

```bash
$ cd tests
$ npm install
```

### 테스트 실행하기

테스트는 실제로 동작하는 서버에 테스트하므로 서버가 동작하고 있는 상태여야 올바르게 동작합니다.

```bash
$ npm run test
```

---

## Week1 - ToDo REST API 만들기

### 과제 목표
Java를 이용해서 ToDo REST API를 만든다.

### 공식적인 과제통과 조건

- [x] 모든 API 테스트를 통과
- [ ] 주간회고 공유

### 개인적인 과제 통과 조건

- [ ] 1주차 배운 내용들에 대해서 블로그에 '나의 언어로' 글쓰고 - 공유
  - [ ] java 개발환경 구축하기 : jdk, gradle, intelliJ
  - [ ] REST API : HTTP, response status code, request method, resource, httpie
  - [ ] Jackson : JSON, Jackson
- [ ] 조금이라도 헷갈리는 개념들 질문 - 정리 - 공유


### 작업 목록

#### 프로젝트 환경설정

- [ ] 자바 15버전을 설치한다

#### 웹서버 세팅

- [x] Http server 만들기 : no connection -> 404 -> 500 넘어가는 흐름 파악
- [x] handler 생성
- [x] header, body response & log 세팅
- [x] models package 생성 > Task model 생성
- [x] Jackson lib dependency
- [x] task to json / json to task 공통 매소드 만들기

#### ToDo 목록 얻기 - `GET /tasks`

- [x] tasks 목록 응답

#### ToDo 상세 조회하기 - `GET /tasks/{id}`

- [x] path 의 id에 해당하는 task 내용 응답
- [x] id가 0 이하일 때, 오류메시지 응답

#### ToDo 생성하기 - `POST /tasks`

- [x] 마지막 id + 1에 해당하는 신규 task 추가
- [x] task title 형식이 안맞을 때 오류 응답 : 문자가 아니거나 비어있으면 안됨

#### ToDo 제목 수정하기 - `PUT/PATCH /tasks/{id}`

- [x] id에 맞는 task 찾아서 제목 수정하기
- [x] 예외처리 - id 에 맞는 task 가 존재하지 않을 때 오류 응답
- [x] 예외처리 - 수정하려는 값이 적절하지 않을 때 오류 응답 : 문자가 아니거나 비어있으면 안됨

#### ToDo 삭제하기 - `DELETE /tasks/{id}`

- [x] id에 맞는 값을 찾아서 삭제한다
- [x] 예외처리 - id 에 맞는 task 가 존재하지 않을 때 오류 응답

#### tests > 전체 테스트

- [x] 전체 테스트를 통과한다


### 추가 작업/공부 목록 (from feedback)

#### 2022.03.22

- [x] git PR 프로세스 제대로 알고 있는지 한번 더 확인하기 

#### 2022.03.22

- [ ] Object Mapper 이용하지 않고 json - task 간 parsing 하도록 수정하기
- [x] 경로 Path validation - GET /tasks/ 처럼 뒤쪽에 `/` 붙은 경로들 대응하기
- [x] 일반 숫자, 문자들 상수로 분리하기
- [x] `import *` 처럼 전체 import 사용하지 않고 필요한 라이브러리만 import 하기

#### 2022.03.23

- [x] git PR 상단에 TODO list 만드는 법 조사 후 적용해보기
- [x] HttpMethod - [http method 정의된 RFC 문서](https://datatracker.ietf.org/doc/html/rfc7231#section-4.3) 링크 주석으로 남겨둬서 편하게 읽을 수 있도록 하기
- [x] HttpMethod - [응답 메시지 RFC 7231에 명시된](https://datatracker.ietf.org/doc/html/rfc7231#section-6.1) Reason-Phrase를 적어주는 방식으로 수정하기
- [x]  HttpMethod - response code : http method 에 정의해준 enum 활용하기
- [ ] TodoHttpHandler > matchPathDepthOne, matchPathDepthTwo
   - [ ] `PATH_REGEX` : 매소드 인자로 받게 고치기
   - [ ] 매소드 이름 좀 더 일반적인 의미로 수정하기
   - [ ] 해당 매소드 다른 클래스로 옮기고 public 으로 접근제한 풀기
   - [ ] Junit test code 작성하기
   - [ ] Junit > @Nested 사용해서 계층형 테스트 코드 작성하기

#### 2022.03.25

- [ ] REST API - POST/PUT 매소드에 대해서 response 관례/규약 질문 후 내용 정리하기 


#### 2022.03.26

- [ ] [JavaDoc 태그 사용해보기](https://github.com/CodeSoom/spring-week1-assignment-1/pull/82#discussion_r835233263)
  - [ ] [필요없는 문서 주석 삭제하기](https://github.com/CodeSoom/spring-week1-assignment-1/pull/82#discussion_r835233823)
- [ ] [reqBody에 재할당을 하지 않고 문제를 해결할 수 있는 방법을 고민하기](https://github.com/CodeSoom/spring-week1-assignment-1/pull/82#discussion_r835235129)
- [ ] TodoHttpHandler.java > [Path 처리 > 2 : 코드만 보고 논리적으로 근거를 떠올릴 수 있도록 개선하기](https://github.com/CodeSoom/spring-week1-assignment-1/pull/82#discussion_r835236008)
- [ ] Task.java > [Null or Empty 검사하는 매소드에 JavaDoc 태그 사용해보기](https://github.com/CodeSoom/spring-week1-assignment-1/pull/82#discussion_r835237287)
- [ ] TaskList.java > ? : 를 사용하지 말고 평범한 if문을 사용하는 방식으로 바꿔보기
- [ ] [Logger 관련 RFC 5242 문서 읽기](https://github.com/CodeSoom/spring-week1-assignment-1/pull/82#discussion_r835239690)
  - [ ] JavaDoc 태그로 주석달기
- [TodoHttpHandlerTest.java > 파일 마지막 라인에 공백 추가하기 & 종립님의 관련 글 보고 공부하기](https://github.com/CodeSoom/spring-week1-assignment-1/pull/82#discussion_r835240867)

