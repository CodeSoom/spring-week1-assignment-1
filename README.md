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

## 작업 목록 및 순서
1. 초기 세팅
   1. Http server 만들기 : no connection -> 404 -> 500 넘어가는 흐름 파악
   2. handler 생성
   3. header, body response & log 세팅
   4. models package 생성 > Task model 생성
      1. id
      2. title 
   5. Jackson lib dependency 
   6. task to json / json to task 공통 매소드 만들기
2. ToDo 목록 얻기 & 테스트
   1. 경로 : GET /tasks
   2. 기능 : tasks 목록 응답
3. ToDo 상세 조회하기 & 테스트
   1. 경로 : GET /tasks/{id}
   2. 기능 : path 의 id에 해당하는 task 내용 응답 
   3. 예외처리
      1. id 가 0 이하일때 오류메시지 응답
4. ToDo 생성하기 & 테스트
   1. 경로 : POST /tasks
   2. 기능 : 마지막 id + 1에 해당하는 신규 task 추가
   3. 예외처리
      1. task title 형식이 안맞을 때 오류 응답 : 문자가 아니거나 비어있으면 안됨
5. ToDo 제목 수정하기 & 테스트
   1. 경로 : PUT/PATCH /tasks/{id}
   2. 기능 : id에 맞는 task 찾아서 제목 수정하기
   3. 예외처리
      1. id 에 맞는 task 가 존재하지 않을 때 오류 응답
      2. 수정하려는 값이 적절하지 않을 때 오류 응답 : 문자가 아니거나 비어있으면 안됨
6. ToDo 삭제하기 & 테스트
   1. 경로 : DELETE /tasks/{id}
   2. 기능 : id에 맞는 값을 찾아서 삭제한다. 
   3. 예외처리
      1. id 에 맞는 task 가 존재하지 않을 때 오류 응답
7. tests > 전체 테스트

## 추가 작업 목록 (from feedback)
### 2022.03.22
1. Object Mapper 이용하지 않고 json - task 간 parsing 하도록 수정하기
2. 경로 Path validation 
   1. GET /tasks/ 처럼 뒤쪽에 `/` 붙은 경로들 대응하기
3. 일반 숫자, 문자들 상수로 분리하기
4. `import *` 처럼 전체 import 사용하지 않고 필요한 라이브러리만 import 하기

### 2022.03.23
1. git PR 상단에 TODO list 만드는 법 조사 후 적용해보기
2. HttpMethod
   1. http method 정의된 RFC 문서 링크 주석으로 남겨둬서 편하게 읽을 수 있도록 하기
      1. https://datatracker.ietf.org/doc/html/rfc7231#section-4.3
   2. 응답 메시지 : RFC 7231에 명시된 Reason-Phrase를 적어주는 방식으로 수정하기
      1. https://datatracker.ietf.org/doc/html/rfc7231#section-6.1
   3. response code : http method 에 정의해준 enum 활용하기
3. TodoHttpHandler > matchPathDepthOne, matchPathDepthTwo 
   1. Path regex : 매소드 인자로 받게 고치기
   2. 매소드 이름 좀 더 일반적인 의미로 수정하기 
   3. 해당 매소드 다른 클래스로 옮기고 public 으로 접근제한 풀기
   4. Junit test code 작성하기
   5. Junit > @Nested 사용해서 계층형 테스트 코드 작성하기

